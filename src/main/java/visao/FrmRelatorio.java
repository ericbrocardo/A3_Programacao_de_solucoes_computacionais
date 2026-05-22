package visao;

import dao.CategoriaDAO;
import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import modelo.Categoria;
import modelo.Produto;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

/**
 * Tela de relatórios do sistema de controle de estoque.
 * Exibe relatórios de lista de preços, balanço físico/financeiro,
 * produtos abaixo do mínimo, produtos por categoria e
 * produtos mais movimentados.
 */
public class FrmRelatorio extends JFrame {

    private JPanel painelConteudo;
    private JLabel lblTitulo;
    private JLabel lblRodape;
    private JLabel lblDestaque;
    private JSpinner spinnerTop;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final MovimentacaoDAO movDAO = new MovimentacaoDAO();

    /**
     * Construtor da tela de relatórios.
     * Inicializa os componentes visuais da tela.
     */
    public FrmRelatorio() {
        initComponents();
    }

    /**
     * Inicializa e organiza todos os componentes visuais da tela,
     * incluindo os botões de navegação entre relatórios e o spinner Top N.
     */
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

        pnlBotoes.add(new JLabel("Top:"));
        spinnerTop = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
        spinnerTop.setPreferredSize(new Dimension(55, 24));
        pnlBotoes.add(spinnerTop);

        add(pnlBotoes, BorderLayout.NORTH);

        lblTitulo = new JLabel("Selecione um relatório acima.");
        add(lblTitulo, BorderLayout.CENTER);

        painelConteudo = new JPanel(new BorderLayout());
        add(painelConteudo, BorderLayout.CENTER);

        lblRodape = new JLabel("", SwingConstants.RIGHT);
        add(lblRodape, BorderLayout.SOUTH);

        lblDestaque = new JLabel(" ");
        lblDestaque.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblDestaque.setForeground(new Color(180, 60, 0));

        JPanel pnlSul = new JPanel(new BorderLayout());
        pnlSul.add(lblDestaque, BorderLayout.NORTH);
        pnlSul.add(lblRodape, BorderLayout.SOUTH);
        add(pnlSul, BorderLayout.SOUTH);
    }

    /**
     * Exibe o relatório de lista de preços de todos os produtos,
     * com nome, preço, unidade e categoria.
     */
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

    /**
     * Exibe o relatório de balanço físico e financeiro do estoque,
     * mostrando a quantidade em estoque e o valor total por produto.
     */
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

    /**
     * Exibe o relatório de produtos com estoque abaixo da quantidade mínima,
     * mostrando o nome, o mínimo definido e o estoque atual.
     */
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

    /**
     * Exibe o relatório de quantidade de produtos por categoria,
     * listando apenas categorias que possuem produtos cadastrados.
     */
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

    /**
     * Exibe o relatório dos produtos mais movimentados, limitado pelo valor
     * definido no spinner Top N. Destaca o produto campeão em dourado
     * e exibe um resumo no rodapé da tela.
     */
    private void relMaisMovimentados() {
        lblDestaque.setText(" ");

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"#", "Código", "Produto", "Entradas", "Saídas", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        try {
            int limite = (int) spinnerTop.getValue();
            List<Object[]> dados = movDAO.getTopProdutosMaiorMovimentacao(limite);

            if (dados.isEmpty()) {
                Mensagem.aviso("Nenhuma movimentação registrada.");
                return;
            }

            int pos = 1;
            for (Object[] linha : dados) {
                m.addRow(new Object[]{ pos++, linha[2], linha[1], linha[3], linha[4], linha[5] });
            }

            Object[] top = dados.get(0);
            lblDestaque.setText(String.format(
                "🏆  Maior movimentação: %s  |  Entradas: %s  |  Saídas: %s  |  Total: %s",
                top[1], top[3], top[4], top[5]));

        } catch (Exception e) {
            Mensagem.erro(e.getMessage());
        }

        JTable tabela = new JTable(m);
        tabela.setRowHeight(24);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getColumnModel().getColumn(0).setMaxWidth(40);

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (row == 0) {
                    setBackground(sel ? new Color(200, 170, 0) : new Color(255, 230, 100));
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setBackground(sel ? new Color(200, 220, 255)
                                     : (row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250)));
                    setFont(getFont().deriveFont(Font.PLAIN));
                }
                setHorizontalAlignment(col == 2 ? LEFT : CENTER);
                return this;
            }
        });

        painelConteudo.removeAll();
        painelConteudo.add(new JScrollPane(tabela));
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    /**
     * Exibe um modelo de tabela no painel central da tela.
     *
     * @param m Modelo de tabela a ser exibido
     */
    private void exibirTabela(DefaultTableModel m) {
        painelConteudo.removeAll();
        painelConteudo.add(new JScrollPane(new JTable(m)));
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }
}