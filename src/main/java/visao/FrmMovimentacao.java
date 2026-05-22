package visao;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import modelo.Movimentacao;
import modelo.Movimentacao.Tipo;
import modelo.Produto;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Tela de registro e listagem de movimentações de estoque.
 * Permite registrar entradas e saídas de produtos, validando
 * estoque disponível e atualizando automaticamente as quantidades.
 */
public class FrmMovimentacao extends JFrame {

    private JComboBox<Produto> cmbProduto;
    private JComboBox<Tipo> cmbTipo;
    private JTextField txtData;
    private JTextField txtQuantidade;
    private JTable tabela;
    private DefaultTableModel modelo;

    private final MovimentacaoDAO movDAO = new MovimentacaoDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor da tela de movimentações.
     * Inicializa os componentes, carrega os produtos e a tabela de movimentações.
     */
    public FrmMovimentacao() {
        initComponents();
        carregarProdutos();
        carregarTabela();
    }

    /**
     * Inicializa e organiza todos os componentes visuais da tela.
     */
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
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; gbc.gridx = 0;
        pnlForm.add(new JLabel("Produto:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        cmbProduto = new JComboBox<>();
        pnlForm.add(cmbProduto, gbc);

        gbc.gridx = 4; gbc.gridwidth = 1; gbc.weightx = 0;
        pnlForm.add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 5;
        cmbTipo = new JComboBox<>(Tipo.values());
        cmbTipo.insertItemAt(null, 0);
        cmbTipo.setSelectedIndex(0);
        pnlForm.add(cmbTipo, gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        pnlForm.add(new JLabel("Data (dd/MM/yyyy):"), gbc);

        gbc.gridx = 1;
        txtData = new JTextField(LocalDate.now().format(FORMATO));
        pnlForm.add(txtData, gbc);

        gbc.gridx = 2;
        pnlForm.add(new JLabel("Quantidade:"), gbc);

        gbc.gridx = 3;
        txtQuantidade = new JTextField();
        pnlForm.add(txtQuantidade, gbc);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnLimpar = new JButton("Limpar");

        btnRegistrar.addActionListener(e -> registrar());
        btnLimpar.addActionListener(e -> limpar());

        pnlBotoes.add(btnRegistrar);
        pnlBotoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 6;
        pnlForm.add(pnlBotoes, gbc);

        add(pnlForm, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Produto", "Tipo", "Quantidade", "Data"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /**
     * Registra uma nova movimentação no banco de dados.
     * Valida os campos obrigatórios, o formato da data, a quantidade
     * e verifica se há estoque suficiente em caso de saída.
     */
    private void registrar() {
        Produto produto = (Produto) cmbProduto.getSelectedItem();
        Tipo tipo = (Tipo) cmbTipo.getSelectedItem();

        if (produto == null || tipo == null) {
            Mensagem.aviso("Preencha todos os campos.");
            return;
        }

        LocalDate data;
        try {
            data = LocalDate.parse(txtData.getText(), FORMATO);
        } catch (DateTimeParseException e) {
            Mensagem.erro("Data inválida.");
            return;
        }

        double quantidade;
        try {
            quantidade = Double.parseDouble(txtQuantidade.getText());
        } catch (NumberFormatException e) {
            Mensagem.erro("Quantidade inválida.");
            return;
        }

        if (tipo == Tipo.SAIDA && produto.getQtdEstoque() < quantidade) {
            Mensagem.erro("Estoque insuficiente!");
            return;
        }

        double novoSaldo;
        if (tipo == Tipo.ENTRADA) {
            novoSaldo = produto.getQtdEstoque() + quantidade;
        } else {
            novoSaldo = produto.getQtdEstoque() - quantidade;
        }

        try {
            movDAO.inserir(new Movimentacao(0, produto, data, quantidade, tipo));

            if (tipo == Tipo.SAIDA && novoSaldo <= produto.getQtdMinima()) {
                Mensagem.aviso(
                    "ESTOQUE BAIXO!\n\n" +
                    "Produto: " + produto.getNome() + "\n" +
                    "Saldo atual: " + novoSaldo + "\n" +
                    "Quantidade mínima: " + produto.getQtdMinima()
                );
            }

            if (tipo == Tipo.ENTRADA && novoSaldo >= produto.getQtdMaxima()) {
                Mensagem.aviso(
                    "ESTOQUE ACIMA DO MÁXIMO!\n\n" +
                    "Produto: " + produto.getNome() + "\n" +
                    "Saldo atual: " + novoSaldo + "\n" +
                    "Quantidade máxima: " + produto.getQtdMaxima()
                );
            }

            Mensagem.info("Movimentação registrada!");
            limpar();
            carregarProdutos();
            carregarTabela();
        } catch (Exception e) {
            Mensagem.erro("Erro: " + e.getMessage());
        }
    }

    /**
     * Limpa os campos do formulário e redefine os valores padrão.
     */
    private void limpar() {
        cmbProduto.setSelectedIndex(-1);
        cmbTipo.setSelectedIndex(0);
        txtData.setText(LocalDate.now().format(FORMATO));
        txtQuantidade.setText("");
    }

    /**
     * Carrega todas as movimentações do banco de dados e exibe na tabela.
     */
    private void carregarTabela() {
        modelo.setRowCount(0);
        try {
            List<Movimentacao> lista = movDAO.listarTodas();
            for (Movimentacao m : lista) {
                modelo.addRow(new Object[]{
                    m.getId(),
                    m.getProduto() != null ? m.getProduto().getNome() : "",
                    m.getTipo(),
                    m.getQuantidade(),
                    m.getData().format(FORMATO)
                });
            }
        } catch (Exception e) {
            Mensagem.erro("Erro ao carregar: " + e.getMessage());
        }
    }

    /**
     * Carrega todos os produtos do banco de dados no combobox de produtos.
     */
    private void carregarProdutos() {
        cmbProduto.removeAllItems();
        try {
            List<Produto> lista = produtoDAO.listarTodos();
            for (Produto p : lista) {
                cmbProduto.addItem(p);
            }
        } catch (Exception e) {
            Mensagem.erro("Erro ao carregar produtos: " + e.getMessage());
        }
    }
}