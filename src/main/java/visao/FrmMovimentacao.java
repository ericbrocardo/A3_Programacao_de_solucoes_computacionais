package visao;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import modelo.Movimentacao;
import modelo.Movimentacao.Tipo;
import modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FrmMovimentacao extends JFrame {

    private JComboBox<Produto> cmbProduto;
    private JComboBox<Tipo>    cmbTipo;
    private JTextField         txtData;
    private JTextField         txtQuantidade;
    private JTable             tabela;
    private DefaultTableModel  modelo;

    private final MovimentacaoDAO movDAO     = new MovimentacaoDAO();
    private final ProdutoDAO      produtoDAO = new ProdutoDAO();

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public FrmMovimentacao() {
        initComponents();
        carregarProdutos();
        carregarTabela();
    }
}

    private void initComponents() {
        setTitle("Movimentações de Estoque");
        setSize(720, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 10));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Registrar Movimentação"));
        pnlForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0;
        pnlForm.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 3;
        cmbProduto = new JComboBox<>();
        pnlForm.add(cmbProduto, gbc);
        gbc.gridx = 4; gbc.weightx = 0; gbc.gridwidth = 1;
        pnlForm.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.4;
        cmbTipo = new JComboBox<>(Tipo.values());
        cmbTipo.insertItemAt(null, 0);
        cmbTipo.setSelectedIndex(0);
        pnlForm.add(cmbTipo, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.weightx = 0;
        pnlForm.add(new JLabel("Data (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtData = new JTextField(LocalDate.now().format(FORMATO));
        pnlForm.add(txtData, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        pnlForm.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtQuantidade = new JTextField();
        pnlForm.add(txtQuantidade, gbc);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlBotoes.setBackground(Color.WHITE);
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnLimpar    = new JButton("Limpar");
        estilizarBotao(btnRegistrar, new Color(41, 128, 185));
        estilizarBotao(btnLimpar,    new Color(149, 165, 166));
        btnRegistrar.addActionListener(e -> registrar());
        btnLimpar.addActionListener(e -> limpar());
        pnlBotoes.add(btnRegistrar);
        pnlBotoes.add(btnLimpar);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 6; gbc.weightx = 1;
        pnlForm.add(pnlBotoes, gbc);
        add(pnlForm, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
            new String[]{"ID", "Produto", "Tipo", "Quantidade", "Data"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modelo) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                String tipo = (String) modelo.getValueAt(row, 2);
                if (!isRowSelected(row)) {
                    c.setForeground("ENTRADA".equals(tipo)
                        ? new Color(39, 174, 96) : new Color(231, 76, 60));
                } else {
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(24);
        tabela.getColumnModel().getColumn(0).setMaxWidth(45);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }