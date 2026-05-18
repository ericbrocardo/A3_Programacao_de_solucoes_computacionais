package visao;

import dao.CategoriaDAO;
import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import modelo.Categoria;
import modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmRelatorio extends JFrame {

    private JPanel painelConteudo;
    private JLabel lblTitulo;
    private JLabel lblRodape;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final MovimentacaoDAO movDAO = new MovimentacaoDAO();

    public FrmRelatorio() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Relatórios");
        setSize(750, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 8));

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        pnlBotoes.setBackground(new Color(236, 240, 241));

        JButton btn1 = new JButton("📋 Lista de Preços");
        JButton btn2 = new JButton("⚖ Balanço Físico/Financeiro");
        JButton btn3 = new JButton("⚠ Abaixo do Mínimo");
        JButton btn4 = new JButton("🏷 Produtos por Categoria");
        JButton btn5 = new JButton("🔝 Mais Entrada / Saída");

        btn1.addActionListener(e -> relListaPrecos());
        btn2.addActionListener(e -> relBalanco());
        btn3.addActionListener(e -> relAbaixoMinimo());
        btn4.addActionListener(e -> relPorCategoria());
        btn5.addActionListener(e -> relMaisMovimentados());

        pnlBotoes.add(btn1);
        pnlBotoes.add(btn2);
        pnlBotoes.add(btn3);
        pnlBotoes.add(btn4);
        pnlBotoes.add(btn5);

        add(pnlBotoes, BorderLayout.NORTH);

        lblTitulo = new JLabel("Selecione um relatório acima.");
        add(lblTitulo, BorderLayout.CENTER);

        painelConteudo = new JPanel(new BorderLayout());
        add(painelConteudo, BorderLayout.CENTER);

        lblRodape = new JLabel("", SwingConstants.RIGHT);
        add(lblRodape, BorderLayout.SOUTH);
    }

    private void relListaPrecos() {
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"Nome", "Preço", "Unidade", "Categoria"}, 0);

        try {
            for (Produto p : produtoDAO.listarTodos()) {
                m.addRow(new Object[]{
                        p.getNome(),
                        p.getPreco(),
                        p.getUnidade(),
                        p.getCategoria() != null ? p.getCategoria().getNome() : ""
                });
            }
        } catch (Exception e) {
            Mensagem.erro(e.getMessage());
        }

        exibirTabela(m);
    }

    private void relBalanco() {
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"Nome", "Estoque", "Preço", "Total"}, 0);

        try {
            for (Produto p : produtoDAO.listarTodos()) {
                double total = p.getPreco() * p.getQtdEstoque();
                m.addRow(new Object[]{
                        p.getNome(),
                        p.getQtdEstoque(),
                        p.getPreco(),
                        total
                });
            }
        } catch (Exception e) {
            Mensagem.erro(e.getMessage());
        }

        exibirTabela(m);
    }

    private void relAbaixoMinimo() {
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"Nome", "Min", "Estoque"}, 0);

        try {
            for (Produto p : produtoDAO.listarTodos()) {
                if (p.getQtdEstoque() < p.getQtdMinima()) {
                    m.addRow(new Object[]{
                            p.getNome(),
                            p.getQtdMinima(),
                            p.getQtdEstoque()
                    });
                }
            }
        } catch (Exception e) {
            Mensagem.erro(e.getMessage());
        }

        exibirTabela(m);
    }

    private void relPorCategoria() {
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"Categoria", "Qtd"}, 0);

        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            List<Categoria> categorias = categoriaDAO.listarTodas();

            for (Categoria c : categorias) {
                long count = produtos.stream()
                        .filter(p -> p.getCategoria() != null &&
                                p.getCategoria().getId() == c.getId())
                        .count();

                if (count > 0) {
                    m.addRow(new Object[]{c.getNome(), count});
                }
            }
        } catch (Exception e) {
            Mensagem.erro(e.getMessage());
        }

        exibirTabela(m);
    }

    private void relMaisMovimentados() {
        try {
            String entrada = movDAO.produtoMaisEntrada();
            String saida = movDAO.produtoMaisSaida();

            JOptionPane.showMessageDialog(this,
                    "Mais entrada: " + entrada + "\nMais saída: " + saida);

        } catch (Exception e) {
            Mensagem.erro(e.getMessage());
        }
    }

    private void exibirTabela(DefaultTableModel m) {
        painelConteudo.removeAll();
        painelConteudo.add(new JScrollPane(new JTable(m)));
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }
}