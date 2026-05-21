package visao;

import dao.CategoriaDAO;
import dao.ProdutoDAO;
import modelo.Categoria;
import modelo.Produto;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.table.DefaultTableModel;
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

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        pnlForm.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.gridx = 0;
        pnlForm.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        txtNome = new JTextField();
        pnlForm.add(txtNome, gbc);

        gbc.gridx = 4;
        gbc.gridwidth = 1;
        pnlForm.add(new JLabel("Unidade:"), gbc);

        gbc.gridx = 5;
        txtUnidade = new JTextField();
        pnlForm.add(txtUnidade, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        pnlForm.add(new JLabel("Preço (R$):"), gbc);

        gbc.gridx = 1;
        txtPreco = new JTextField();
        pnlForm.add(txtPreco, gbc);

        gbc.gridx = 2;
        pnlForm.add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 3;
        cmbCategoria = new JComboBox<>();
        pnlForm.add(cmbCategoria, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        pnlForm.add(new JLabel("Qtd. Estoque:"), gbc);

        gbc.gridx = 1;
        txtQtdEstoque = new JTextField();
        pnlForm.add(txtQtdEstoque, gbc);

        gbc.gridx = 2;
        pnlForm.add(new JLabel("Qtd. Mínima:"), gbc);

        gbc.gridx = 3;
        txtQtdMinima = new JTextField();
        pnlForm.add(txtQtdMinima, gbc);

        gbc.gridx = 4;
        pnlForm.add(new JLabel("Qtd. Máxima:"), gbc);

        gbc.gridx = 5;
        txtQtdMaxima = new JTextField();
        pnlForm.add(txtQtdMaxima, gbc);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnExcluir = new JButton("Excluir");

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());
        btnExcluir.addActionListener(e -> excluir());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnLimpar);
        pnlBotoes.add(btnExcluir);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 6;
        pnlForm.add(pnlBotoes, gbc);

        add(pnlForm, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Nome", "Preço", "Unidade", "Estoque", "Mínimo", "Máximo", "Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarLinha();
            }
        });

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void salvar() {
        try {
            Produto p = new Produto(
                    produtoSelecionado != null ? produtoSelecionado.getId() : 0,
                    txtNome.getText(),
                    Double.parseDouble(txtPreco.getText()),
                    txtUnidade.getText(),
                    Double.parseDouble(txtQtdEstoque.getText()),
                    Double.parseDouble(txtQtdMinima.getText()),
                    Double.parseDouble(txtQtdMaxima.getText()),
                    (Categoria) cmbCategoria.getSelectedItem()
            );

            if (produtoSelecionado == null) {
                produtoDAO.inserir(p);
                Mensagem.info("Produto cadastrado!");
            } else {
                produtoDAO.atualizar(p);
                Mensagem.info("Produto atualizado!");
            }

            limpar();
            carregarTabela();

        } catch (NumberFormatException e) {
            Mensagem.erro("Preencha os campos numéricos corretamente.");
        } catch (Exception e) {
            Mensagem.erro("Erro: " + e.getMessage());
        }
    }

    private void excluir() {
        if (produtoSelecionado == null) {
            Mensagem.erro("Selecione um produto para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este produto?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            produtoDAO.excluir(produtoSelecionado.getId());

            Mensagem.info("Produto excluído!");
            limpar();
            carregarTabela();

        } catch (Exception e) {
            String erro = e.getMessage();

            if (erro != null && (
                    erro.contains("foreign key constraint fails")
                            || erro.contains("Cannot delete or update a parent row")
                            || erro.contains("movimentacao")
                            || erro.contains("produto_id")
            )) {
                Mensagem.erro("Não é possível excluir este produto, pois ele possui movimentações cadastradas.");
            } else {
                Mensagem.erro("Erro ao excluir produto: " + erro);
            }
        }
    }

    private void limpar() {
        txtNome.setText("");
        txtUnidade.setText("");
        txtPreco.setText("");
        txtQtdEstoque.setText("");
        txtQtdMinima.setText("");
        txtQtdMaxima.setText("");

        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(-1);
        }

        produtoSelecionado = null;
        tabela.clearSelection();
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();

        if (linha < 0) {
            return;
        }

        try {
            int id = (int) modelo.getValueAt(linha, 0);
            produtoSelecionado = produtoDAO.buscarPorId(id);

            txtNome.setText(produtoSelecionado.getNome());
            txtUnidade.setText(produtoSelecionado.getUnidade());
            txtPreco.setText(String.valueOf(produtoSelecionado.getPreco()));
            txtQtdEstoque.setText(String.valueOf(produtoSelecionado.getQtdEstoque()));
            txtQtdMinima.setText(String.valueOf(produtoSelecionado.getQtdMinima()));
            txtQtdMaxima.setText(String.valueOf(produtoSelecionado.getQtdMaxima()));
            cmbCategoria.setSelectedItem(produtoSelecionado.getCategoria());

        } catch (Exception e) {
            Mensagem.erro("Erro: " + e.getMessage());
        }
    }

    private void carregarTabela() {
        modelo.setRowCount(0);

        try {
            List<Produto> lista = produtoDAO.listarTodos();

            for (Produto p : lista) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getPreco(),
                        p.getUnidade(),
                        p.getQtdEstoque(),
                        p.getQtdMinima(),
                        p.getQtdMaxima(),
                        p.getCategoria() != null ? p.getCategoria().getNome() : ""
                });
            }

        } catch (Exception e) {
            Mensagem.erro("Erro: " + e.getMessage());
        }
    }

    private void carregarCategorias() {
        try {
            cmbCategoria.removeAllItems();

            List<Categoria> lista = categoriaDAO.listarTodas();

            for (Categoria c : lista) {
                cmbCategoria.addItem(c);
            }

            cmbCategoria.setSelectedIndex(-1);

        } catch (Exception e) {
            Mensagem.erro("Erro ao carregar categorias: " + e.getMessage());
        }
    }
}