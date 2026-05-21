package dao;

import modelo.Categoria;
import modelo.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco de dados da entidade Produto.
 * Realiza operações de inserir, atualizar, excluir, listar, buscar,
 * atualizar estoque e reajustar preços dos produtos.
 */
public class ProdutoDAO {

    /**
     * Insere um novo produto no banco de dados.
     *
     * @param p Objeto Produto a ser inserido
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public void inserir(Produto p) {
        String sql = "INSERT INTO produto (nome, preco, unidade, qtd_estoque, qtd_minima, qtd_maxima, categoria_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getPreco());
            ps.setString(3, p.getUnidade());
            ps.setDouble(4, p.getQtdEstoque());
            ps.setDouble(5, p.getQtdMinima());
            ps.setDouble(6, p.getQtdMaxima());
            ps.setInt(7, p.getCategoria().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
    }

    /**
     * Atualiza os dados de um produto existente no banco de dados.
     *
     * @param p Objeto Produto com os dados atualizados
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public void atualizar(Produto p) {
        String sql = "UPDATE produto SET nome = ?, preco = ?, unidade = ?, "
                   + "qtd_estoque = ?, qtd_minima = ?, qtd_maxima = ?, categoria_id = ? "
                   + "WHERE id = ?";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getPreco());
            ps.setString(3, p.getUnidade());
            ps.setDouble(4, p.getQtdEstoque());
            ps.setDouble(5, p.getQtdMinima());
            ps.setDouble(6, p.getQtdMaxima());
            ps.setInt(7, p.getCategoria().getId());
            ps.setInt(8, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
    }

    /**
     * Remove um produto do banco de dados pelo seu ID.
     *
     * @param id Identificador do produto a ser removido
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public void excluir(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
    }

    /**
     * Retorna uma lista com todos os produtos cadastrados no banco de dados,
     * incluindo os dados da categoria de cada produto.
     *
     * @return List com todos os objetos Produto encontrados
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nome AS nome_cat, c.tamanho, c.embalagem "
                   + "FROM produto p JOIN categoria c ON p.categoria_id = c.id "
                   + "ORDER BY p.nome";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria cat = new Categoria(
                    rs.getInt("categoria_id"),
                    rs.getString("nome_cat"),
                    Categoria.Tamanho.valueOf(rs.getString("tamanho")),
                    Categoria.Embalagem.valueOf(rs.getString("embalagem"))
                );
                Produto p = new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getString("unidade"),
                    rs.getDouble("qtd_estoque"),
                    rs.getDouble("qtd_minima"),
                    rs.getDouble("qtd_maxima"),
                    cat
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
        return lista;
    }

    /**
     * Busca um produto no banco de dados pelo seu ID,
     * incluindo os dados da categoria associada.
     *
     * @param id Identificador do produto a ser buscado
     * @return Objeto Produto encontrado ou null se não existir
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public Produto buscarPorId(int id) {
        String sql = "SELECT p.*, c.nome AS nome_cat, c.tamanho, c.embalagem "
                   + "FROM produto p JOIN categoria c ON p.categoria_id = c.id "
                   + "WHERE p.id = ?";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categoria cat = new Categoria(
                    rs.getInt("categoria_id"),
                    rs.getString("nome_cat"),
                    Categoria.Tamanho.valueOf(rs.getString("tamanho")),
                    Categoria.Embalagem.valueOf(rs.getString("embalagem"))
                );
                return new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getString("unidade"),
                    rs.getDouble("qtd_estoque"),
                    rs.getDouble("qtd_minima"),
                    rs.getDouble("qtd_maxima"),
                    cat
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
        return null;
    }

    /**
     * Atualiza diretamente a quantidade em estoque de um produto,
     * definindo o novo valor informado como saldo atual.
     *
     * @param produtoId Identificador do produto a ter o estoque atualizado
     * @param novaQtd Novo valor do estoque a ser definido
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public void atualizarEstoque(int produtoId, double novaQtd) {
        String sql = "UPDATE produto SET qtd_estoque = ? WHERE id = ?";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, novaQtd);
            ps.setInt(2, produtoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar estoque: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
    }

    /**
     * Reajusta o preço de todos os produtos cadastrados com base
     * em um percentual informado.
     * Exemplo: percentual 10 aumenta 10% em todos os preços.
     *
     * @param percentual Percentual de reajuste a ser aplicado
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
    public void reajustarPrecos(double percentual) {
        String sql = "UPDATE produto SET preco = preco * (1 + ? / 100)";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, percentual);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao reajustar preços: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
    }
}