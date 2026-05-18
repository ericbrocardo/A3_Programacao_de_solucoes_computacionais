package dao;

import modelo.Movimentacao;
import modelo.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {

    public void inserir(Movimentacao m) {
        String sql = "INSERT INTO movimentacao (produto_id, data, quantidade, tipo) VALUES (?, ?, ?, ?)";
        Connection conn = ConexaoDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, m.getProduto().getId());
            ps.setDate(2, Date.valueOf(m.getData()));
            ps.setDouble(3, m.getQuantidade());
            ps.setString(4, m.getTipo().name());
            ps.executeUpdate();

            atualizarEstoque(conn, m);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir movimentação: " + e.getMessage(), e);
        } finally {
            ConexaoDB.closeConnection(conn);
        }
    }

    private void atualizarEstoque(Connection conn, Movimentacao m) throws SQLException {
        String sinal = m.getTipo() == Movimentacao.Tipo.ENTRADA ? "+" : "-";
        String sql = "UPDATE produto SET qtd_estoque = qtd_estoque " + sinal + " ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, m.getQuantidade());
        ps.setInt(2, m.getProduto().getId());
        ps.executeUpdate();
    }

    public List<Movimentacao> listarTodas() throws SQLException {
        String sql = "SELECT * FROM movimentacao ORDER BY data DESC";
        List<Movimentacao> lista = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }

        return lista;
    }

    public String produtoMaisEntrada() throws SQLException {
        String sql = """
        SELECT p.nome, SUM(m.quantidade) AS total
        FROM movimentacao m
        JOIN produto p ON p.id = m.produto_id
        WHERE m.tipo = 'ENTRADA'
        GROUP BY m.produto_id
        ORDER BY total DESC
        LIMIT 1
    """;

        try (Connection conn = ConexaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString("nome") + " (" + rs.getDouble("total") + ")";
            }
        }

        return "Nenhuma movimentação registrada";
    }

    public String produtoMaisSaida() throws SQLException {
        String sql = """
        SELECT p.nome, SUM(m.quantidade) AS total
        FROM movimentacao m
        JOIN produto p ON p.id = m.produto_id
        WHERE m.tipo = 'SAIDA'
        GROUP BY m.produto_id
        ORDER BY total DESC
        LIMIT 1
    """;

        try (Connection conn = ConexaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString("nome") + " (" + rs.getDouble("total") + ")";
            }
        }

        return "Nenhuma movimentação registrada";
    }

    // ✅ MÉTODO QUE FALTAVA (ESSENCIAL)
    private Movimentacao mapear(ResultSet rs) throws SQLException {
        Movimentacao m = new Movimentacao();

        m.setId(rs.getInt("id"));

        Produto p = new Produto();
        p.setId(rs.getInt("produto_id"));
        m.setProduto(p);

        m.setData(rs.getDate("data").toLocalDate());
        m.setQuantidade(rs.getDouble("quantidade"));
        m.setTipo(Movimentacao.Tipo.valueOf(rs.getString("tipo")));

        return m;
    }
}