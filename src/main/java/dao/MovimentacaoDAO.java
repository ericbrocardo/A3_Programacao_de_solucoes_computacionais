package dao;

import modelo.Categoria;
import modelo.Movimentacao;
import modelo.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {

    public void inserir(Movimentacao m) {
        String sql = "INSERT INTO movimentacao (produto_id, data, quantidade, tipo) VALUES (?, ?, ?, ?)";
        Connection conn = ConnectionFactory.getConnection();
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
            ConnectionFactory.closeConnection(conn);
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

    public List<Movimentacao> listarTodos() {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT m.*, p.nome AS nome_prod, p.preco, p.unidade, "
                   + "p.qtd_estoque, p.qtd_minima, p.qtd_maxima, p.categoria_id, "
                   + "c.nome AS nome_cat, c.tamanho, c.embalagem "
                   + "FROM movimentacao m "
                   + "JOIN produto p ON m.produto_id = p.id "
                   + "JOIN categoria c ON p.categoria_id = c.id "
                   + "ORDER BY m.data DESC";
        Connection conn = ConnectionFactory.getConnection();
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
                    rs.getInt("produto_id"),
                    rs.getString("nome_prod"),
                    rs.getDouble("preco"),
                    rs.getString("unidade"),
                    rs.getDouble("qtd_estoque"),
                    rs.getDouble("qtd_minima"),
                    rs.getDouble("qtd_maxima"),
                    cat
                );
                Movimentacao mov = new Movimentacao(
                    rs.getInt("id"),
                    p,
                    rs.getDate("data").toLocalDate(),
                    rs.getDouble("quantidade"),
                    Movimentacao.Tipo.valueOf(rs.getString("tipo"))
                );
                lista.add(mov);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar movimentações: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return lista;
    }
}