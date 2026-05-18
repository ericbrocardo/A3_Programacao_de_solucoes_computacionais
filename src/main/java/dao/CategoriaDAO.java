package dao;

import modelo.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

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

    public void excluir(int id) {
        String sql = "DELETE FROM categoria WHERE id = ?";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir categoria: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
    }

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

    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setTamanho(Categoria.Tamanho.valueOf(rs.getString("tamanho")));
        c.setEmbalagem(Categoria.Embalagem.valueOf(rs.getString("embalagem")));
        return c;
    }
}