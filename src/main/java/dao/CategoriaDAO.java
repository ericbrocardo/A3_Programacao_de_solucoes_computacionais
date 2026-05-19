package dao;

import modelo.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco de dados da entidade Categoria.
 * Realiza operações de inserir, atualizar, excluir, listar e buscar categorias.
 */
public class CategoriaDAO {

    /**
     * Insere uma nova categoria no banco de dados.
     *
     * @param c Objeto Categoria a ser inserido
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public void inserir(Categoria c) {
        String sql = "INSERT INTO categoria (nome, tamanho, embalagem) VALUES (?, ?, ?)";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getTamanho().name());
            ps.setString(3, c.getEmbalagem().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir categoria: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
    }

    /**
     * Atualiza os dados de uma categoria existente no banco de dados.
     *
     * @param c Objeto Categoria com os dados atualizados
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public void atualizar(Categoria c) {
        String sql = "UPDATE categoria SET nome = ?, tamanho = ?, embalagem = ? WHERE id = ?";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getTamanho().name());
            ps.setString(3, c.getEmbalagem().name());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
    }

    /**
     * Remove uma categoria do banco de dados pelo seu ID.
     * Exclui em cascata as movimentações e produtos vinculados
     * antes de remover a categoria, utilizando transação para garantir
     * que tudo seja desfeito em caso de erro.
     *
     * @param id Identificador da categoria a ser removida
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public void excluir(int id) {
        Connection conn = ConexaoDB.getConnection();

        String sqlExcluirMovimentacoes =
                "DELETE FROM movimentacao " +
                "WHERE produto_id IN (SELECT id FROM produto WHERE categoria_id = ?)";
        String sqlExcluirProdutos =
                "DELETE FROM produto WHERE categoria_id = ?";
        String sqlExcluirCategoria =
                "DELETE FROM categoria WHERE id = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psMov = conn.prepareStatement(sqlExcluirMovimentacoes)) {
                psMov.setInt(1, id);
                psMov.executeUpdate();
            }
            try (PreparedStatement psProd = conn.prepareStatement(sqlExcluirProdutos)) {
                psProd.setInt(1, id);
                psProd.executeUpdate();
            }
            try (PreparedStatement psCat = conn.prepareStatement(sqlExcluirCategoria)) {
                psCat.setInt(1, id);
                psCat.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao desfazer exclusão: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao excluir categoria: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                // Ignora erro ao restaurar auto commit
            }
            ConexaoDB.closeConnection(conn);
        }
    }

    /**
     * Retorna uma lista com todas as categorias cadastradas no banco de dados.
     *
     * @return List com todos os objetos Categoria encontrados
     * @throws SQLException se ocorrer erro na operação com o banco
     */
    public List<Categoria> listarTodas() throws SQLException {
        String sql = "SELECT * FROM categoria ORDER BY nome";
        List<Categoria> lista = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /**
     * Busca uma categoria no banco de dados pelo seu ID.
     *
     * @param id Identificador da categoria a ser buscada
     * @return Objeto Categoria encontrado ou null se não existir
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public Categoria buscarPorId(int id) {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar categoria: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
        return null;
    }

    /**
     * Mapeia uma linha do ResultSet para um objeto Categoria.
     *
     * @param rs ResultSet posicionado na linha a ser mapeada
     * @return Objeto Categoria preenchido com os dados da linha
     * @throws SQLException se ocorrer erro ao ler o ResultSet
     */
    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setTamanho(Categoria.Tamanho.valueOf(rs.getString("tamanho")));
        c.setEmbalagem(Categoria.Embalagem.valueOf(rs.getString("embalagem")));
        return c;
    }
}