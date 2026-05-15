package dao;

import modelo.Categoria;
import modelo.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

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
}