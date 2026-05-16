package visao;

import dao.CategoriaDAO;
import dao.ProdutoDAO;
import modelo.Categoria;
import modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FrmProduto extends JFrame {

    private JTextField txtNome, txtUnidade, txtPreco;
    private JTextField txtQtdEstoque, txtQtdMinima, txtQtdMaxima;
    private JComboBox<Categoria> cmbCategoria;
    private JTable tabela;
    private DefaultTableModel modelo;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private Produto produtoSelecionado = null;

    public FrmProduto() {
        initComponents();
        carregarCategorias();
        carregarTabela();
    }

    private void initComponents() {
        setTitle("Produtos");
        setSize(780, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 10));

        // ── Formulário ──────────────────────────────
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        pnlForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0 — Nome e Unidade
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        pnlForm.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        txtNome = new JTextField();
        pnlForm.add(txtNome, gbc);
        gbc.gridx = 4;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        pnlForm.add(new JLabel("Unidade:"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 0.3;
        txtUnidade = new JTextField();
        pnlForm.add(txtUnidade, gbc);

        // Linha 1 — Preço e Categoria
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        pnlForm.add(new JLabel("Preço (R$):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        txtPreco = new JTextField();
        pnlForm.add(txtPreco, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;
        pnlForm.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        cmbCategoria = new JComboBox<>();
        pnlForm.add(cmbCategoria, gbc);

        // Linha 2 — Quantidades
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        pnlForm.add(new JLabel("Qtd. Estoque:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        txtQtdEstoque = new JTextField();
        pnlForm.add(txtQtdEstoque, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;
        pnlForm.add(new JLabel("Qtd. Mínima:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 0.4;
        txtQtdMinima = new JTextField();
        pnlForm.add(txtQtdMinima, gbc);
        gbc.gridx = 4;
        gbc.weightx = 0;
        pnlForm.add(new JLabel("Qtd. Máxima:"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 0.4;
        txtQtdMaxima = new JTextField();
        pnlForm.add(txtQtdMaxima, gbc);

        add(pnlForm, BorderLayout.NORTH);

        // ── Tabela ──────────────────────────────────
        modelo = new DefaultTableModel(
            new String[]{"ID", "Nome", "Preço", "Unidade", "Estoque", "Mínimo", "Máximo", "Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(24);
        tabela.getColumnModel().getColumn(0).setMaxWidth(45);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }
}
