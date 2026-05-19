package dao;

import modelo.Movimentacao;
import modelo.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco de dados da entidade Movimentacao.
 * Registra entradas e saídas de estoque e atualiza automaticamente
 * a quantidade em estoque do produto correspondente.
 */
public class MovimentacaoDAO {

    /**
     * Insere uma nova movimentação no banco de dados e atualiza
     * automaticamente o estoque do produto relacionado.
     *
     * @param m Objeto Movimentacao a ser inserido
     * @throws RuntimeException se ocorrer erro na operação com o banco
     */
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

    /**
     * Atualiza a quantidade em estoque do produto após uma movimentação.
     * Soma a quantidade para ENTRADA e subtrai para SAIDA.
     *
     * @param conn Conexão ativa com o banco de dados
     * @param m Objeto Movimentacao com os dados da operação
     * @throws SQLException se ocorrer erro na operação com o banco
     */
    private void atualizarEstoque(Connection conn, Movimentacao m) throws SQLException {
        String sinal = m.getTipo() == Movimentacao.Tipo.ENTRADA ? "+" : "-";
        String sql = "UPDATE produto SET qtd_estoque = qtd_estoque " + sinal + " ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, m.getQuantidade());
        ps.setInt(2, m.getProduto().getId());
        ps.executeUpdate();
    }

    /**
     * Retorna uma lista com todas as movimentações registradas no banco de dados,
     * ordenadas pela data mais recente.
     *
     * @return List com todos os objetos Movimentacao encontrados
     * @throws SQLException se ocorrer erro na operação com o banco
     */
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

    /**
     * Retorna o nome e a quantidade total do produto com maior
     * volume de entradas registradas.
     *
     * @return String com nome do produto e total de entradas, ou mensagem
     *         informando que não há movimentações registradas
     * @throws SQLException se ocorrer erro na operação com o banco
     */
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

    /**
     * Retorna o nome e a quantidade total do produto com maior
     * volume de saídas registradas.
     *
     * @return String com nome do produto e total de saídas, ou mensagem
     *         informando que não há movimentações registradas
     * @throws SQLException se ocorrer erro na operação com o banco
     */
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

    /**
     * Mapeia uma linha do ResultSet para um objeto Movimentacao.
     *
     * @param rs ResultSet posicionado na linha a ser mapeada
     * @return Objeto Movimentacao preenchido com os dados da linha
     * @throws SQLException se ocorrer erro ao ler o ResultSet
     */
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