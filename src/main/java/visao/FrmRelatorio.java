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