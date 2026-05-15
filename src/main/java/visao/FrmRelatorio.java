package visao;

import dao.CategoriaDAO;
import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import modelo.Categoria;
import modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FrmRelatorio extends JFrame {

    private JPanel    painelConteudo;
    private JLabel    lblTitulo;
    private JLabel    lblRodape;

    private final ProdutoDAO      produtoDAO   = new ProdutoDAO();
    private final CategoriaDAO    categoriaDAO = new CategoriaDAO();
    private final MovimentacaoDAO movDAO       = new MovimentacaoDAO();

    public FrmRelatorio() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Relatórios");
        setSize(750, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 8));

        // ── Botões de seleção ────────────────────────
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        pnlBotoes.setBackground(new Color(236, 240, 241));
        pnlBotoes.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        JButton btn1 = new JButton("📋 Lista de Preços");
        JButton btn2 = new JButton("⚖ Balanço Físico/Financeiro");
        JButton btn3 = new JButton("⚠ Abaixo do Mínimo");
        JButton btn4 = new JButton("🏷 Produtos por Categoria");
        JButton btn5 = new JButton("🔝 Mais Entrada / Saída");

        estilizarBotao(btn1, new Color(41, 128, 185));
        estilizarBotao(btn2, new Color(142, 68, 173));
        estilizarBotao(btn3, new Color(192, 57, 43));
        estilizarBotao(btn4, new Color(22, 160, 133));
        estilizarBotao(btn5, new Color(211, 84, 0));

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

        // ── Título do relatório ──────────────────────
        lblTitulo = new JLabel("Selecione um relatório acima.", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(52, 73, 94));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(lblTitulo, BorderLayout.CENTER);

        // ── Painel de conteúdo ───────────────────────
        painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setBackground(Color.WHITE);
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(painelConteudo, BorderLayout.CENTER);

        // ── Rodapé ───────────────────────────────────
        lblRodape = new JLabel("", SwingConstants.RIGHT);
        lblRodape.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRodape.setForeground(new Color(52, 73, 94));
        lblRodape.setBorder(BorderFactory.createEmptyBorder(4, 10, 8, 10));
        add(lblRodape, BorderLayout.SOUTH);
    }

    private void estilizarBotao(JButton btn, Color cor) {
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Métodos vazios para compilação inicial
    private void relListaPrecos() {}
    private void relBalanco() {}
    private void relAbaixoMinimo() {}
    private void relPorCategoria() {}
    private void relMaisMovimentados() {}
}

    // ─────────────────────────────────────────────
    // Relatório 1 — Lista de Preços
    // ─────────────────────────────────────────────
    private void relListaPrecos() {
        lblTitulo.setText("📋 Lista de Preços — produtos em ordem alfabética");
        lblRodape.setText("");

        DefaultTableModel m = new DefaultTableModel(
            new String[]{"Nome", "Preço Unitário", "Unidade", "Categoria"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            for (Produto p : produtoDAO.listarTodos()) {
                m.addRow(new Object[]{
                    p.getNome(),
                    String.format("R$ %.2f", p.getPreco()),
                    p.getUnidade(),
                    p.getCategoria() != null ? p.getCategoria().getNome() : ""
                });
            }
        } catch (SQLException e) {
            Mensagem.erro("Erro: " + e.getMessage());
        }

        exibirTabela(m, new int[]{280, 120, 80, 160});
    }

    // ─────────────────────────────────────────────
    // Relatório 2 — Balanço Físico/Financeiro
    // ─────────────────────────────────────────────
    private void relBalanco() {
        lblTitulo.setText("⚖ Balanço Físico/Financeiro");

        DefaultTableModel m = new DefaultTableModel(
            new String[]{"Nome", "Qtd. Estoque", "Preço Unit.", "Valor Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        double totalGeral = 0;
        try {
            for (Produto p : produtoDAO.listarTodos()) {
                double total = p.getPreco() * p.getQtdEstoque();
                totalGeral += total;
                m.addRow(new Object[]{
                    p.getNome(),
                    p.getQtdEstoque(),
                    String.format("R$ %.2f", p.getPreco()),
                    String.format("R$ %.2f", total)
                });
            }
        } catch (SQLException e) {
            Mensagem.erro("Erro: " + e.getMessage());
        }

        lblRodape.setText(String.format("Valor total do estoque: R$ %.2f", totalGeral));
        exibirTabela(m, new int[]{260, 110, 110, 120});
    }

    private JTable criarTabela(DefaultTableModel m, int[] larguras) {
        JTable tabela = new JTable(m);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(26);
        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
        return tabela;
    }

    private void exibirTabela(DefaultTableModel m, int[] larguras) {
        painelConteudo.removeAll();
        painelConteudo.add(new JScrollPane(criarTabela(m, larguras)), BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    // ─────────────────────────────────────────────
    // Relatório 3 — Abaixo do mínimo
    // ─────────────────────────────────────────────
    private void relAbaixoMinimo() {
        lblTitulo.setText("⚠ Produtos abaixo da quantidade mínima");
        lblRodape.setText("");

        DefaultTableModel m = new DefaultTableModel(
            new String[]{"Nome", "Qtd. Mínima", "Qtd. em Estoque"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            List<Produto> lista = produtoDAO.listarTodos().stream()
                .filter(p -> p.getQtdEstoque() < p.getQtdMinima())
                .toList();

            for (Produto p : lista) {
                m.addRow(new Object[]{
                    p.getNome(),
                    p.getQtdMinima(),
                    p.getQtdEstoque()
                });
            }
        } catch (SQLException e) {
            Mensagem.erro("Erro: " + e.getMessage());
        }

        // Destaca quantidade em estoque de vermelho
        JTable tabela = criarTabela(m, new int[]{300, 120, 140});
        tabela.getColumnModel().getColumn(2).setCellRenderer(
            (t, value, isSelected, hasFocus, row, col) -> {
                JLabel lbl = new JLabel(value != null ? value.toString() : "");
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                if (isSelected) {
                    lbl.setBackground(t.getSelectionBackground());
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(Color.WHITE);
                    lbl.setForeground(new Color(192, 57, 43));
                    lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
                }
                return lbl;
            }
        );

        if (m.getRowCount() == 0) {
            exibirMensagem("✅ Nenhum produto abaixo do estoque mínimo.");
        } else {
            painelConteudo.removeAll();
            painelConteudo.add(new JScrollPane(tabela), BorderLayout.CENTER);
            painelConteudo.revalidate();
            painelConteudo.repaint();
        }
    }

    // ─────────────────────────────────────────────
    // Relatório 4 — Produtos por categoria
    // ─────────────────────────────────────────────
    private void relPorCategoria() {
        lblTitulo.setText("🏷 Quantidade de produtos distintos por categoria");
        lblRodape.setText("");

        DefaultTableModel m = new DefaultTableModel(
            new String[]{"Categoria", "Qtd. de Produtos"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            List<Produto>   produtos    = produtoDAO.listarTodos();
            List<Categoria> categorias  = categoriaDAO.listarTodas();

            for (Categoria cat : categorias) {
                long count = produtos.stream()
                    .filter(p -> p.getCategoria() != null
                            && p.getCategoria().getId() == cat.getId())
                    .count();
                if (count > 0) {
                    m.addRow(new Object[]{ cat.getNome(), count });
                }
            }
        } catch (SQLException e) {
            Mensagem.erro("Erro: " + e.getMessage());
        }

        if (m.getRowCount() == 0) {
            exibirMensagem("Nenhuma categoria com produtos cadastrados.");
        } else {
            exibirTabela(m, new int[]{400, 200});
        }
    }

    private void exibirMensagem(String msg) {
        painelConteudo.removeAll();
        JLabel lbl = new JLabel(msg, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(new Color(100, 100, 100));
        painelConteudo.add(lbl, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }