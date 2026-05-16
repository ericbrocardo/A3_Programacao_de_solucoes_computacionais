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

            // Linha 3 — Botões
            JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            pnlBotoes.setBackground(Color.WHITE);

            JButton btnSalvar = new JButton("Salvar");
            JButton btnLimpar = new JButton("Limpar");
            JButton btnExcluir = new JButton("Excluir");

            estilizarBotao(btnSalvar, new Color(39, 174, 96));
            estilizarBotao(btnLimpar, new Color(149, 165, 166));
            estilizarBotao(btnExcluir, new Color(231, 76, 60));

            btnSalvar.addActionListener(e -> salvar());
            btnExcluir.addActionListener(e -> excluir());
            btnLimpar.addActionListener(e -> limpar());

            pnlBotoes.add(btnSalvar);
            pnlBotoes.add(btnLimpar);
            pnlBotoes.add(btnExcluir);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 6;
            gbc.weightx = 1;
            pnlForm.add(pnlBotoes, gbc);

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
            tabela.getSelectionModel().addListSelectionListener(e -> selecionarLinha());

            add(new JScrollPane(tabela), BorderLayout.CENTER);
        }
    }

    private void estilizarBotao(JButton btn, Color cor) {
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 32));
    }

    private void carregarCategorias() {
        try {
            List<Categoria> lista = categoriaDAO.listarTodas();
            for (Categoria c : lista) {
                cmbCategoria.addItem(c);
            }
            cmbCategoria.setSelectedIndex(-1);
        } catch (SQLException e) {
            Mensagem.erro("Erro ao carregar categorias: " + e.getMessage());
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
                    String.format("R$ %.2f", p.getPreco()),
                    p.getUnidade(),
                    p.getQtdEstoque(),
                    p.getQtdMinima(),
                    p.getQtdMaxima(),
                    p.getCategoria() != null ? p.getCategoria().getNome() : ""
                });
            }
        } catch (SQLException e) {
            Mensagem.erro("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void limpar() {
        txtNome.setText("");
        txtUnidade.setText("");
        txtPreco.setText("");
        txtQtdEstoque.setText("");
        txtQtdMinima.setText("");
        txtQtdMaxima.setText("");
        cmbCategoria.setSelectedIndex(-1);
        tabela.clearSelection();
        produtoSelecionado = null;
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String unidade = txtUnidade.getText().trim();
        Categoria cat = (Categoria) cmbCategoria.getSelectedItem();

        if (nome.isEmpty() || unidade.isEmpty() || cat == null
                || txtPreco.getText().trim().isEmpty()
                || txtQtdEstoque.getText().trim().isEmpty()
                || txtQtdMinima.getText().trim().isEmpty()
                || txtQtdMaxima.getText().trim().isEmpty()) {
            Mensagem.aviso("Preencha todos os campos.");
            return;
        }

        double preco, estoque, minima, maxima;
        try {
            preco = Double.parseDouble(txtPreco.getText().trim().replace(",", "."));
            estoque = Double.parseDouble(txtQtdEstoque.getText().trim().replace(",", "."));
            minima = Double.parseDouble(txtQtdMinima.getText().trim().replace(",", "."));
            maxima = Double.parseDouble(txtQtdMaxima.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            Mensagem.erro("Valores numéricos inválidos. Use ponto como separador decimal.");
            return;
        }

        if (minima >= maxima) {
            Mensagem.aviso("A quantidade mínima deve ser menor que a máxima.");
            return;
        }

        try {
            Produto p = new Produto(
                    produtoSelecionado != null ? produtoSelecionado.getId() : 0,
                    nome, preco, unidade, estoque, minima, maxima, cat
            );
            if (produtoSelecionado == null) {
                produtoDAO.inserir(p);
                Mensagem.info("Produto cadastrado com sucesso!");
            } else {
                produtoDAO.atualizar(p);
                Mensagem.info("Produto atualizado com sucesso!");
            }
            limpar();
            carregarTabela();
        } catch (SQLException e) {
            Mensagem.erro("Erro ao salvar: " + e.getMessage());
        }
    }

    private void excluir() {
        if (produtoSelecionado == null) {
            Mensagem.aviso("Selecione um produto para excluir.");
            return;
        }
        if (Mensagem.confirmar("Deseja excluir o produto \"" + produtoSelecionado.getNome() + "\"?")) {
            try {
                produtoDAO.excluir(produtoSelecionado.getId());
                Mensagem.info("Produto excluído com sucesso!");
                limpar();
                carregarTabela();
            } catch (SQLException e) {
                Mensagem.erro("Erro ao excluir: " + e.getMessage());
            }
        }
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        int id = (int) modelo.getValueAt(linha, 0);
        try {
            produtoSelecionado = produtoDAO.buscarPorId(id);
            if (produtoSelecionado != null) {
                txtNome.setText(produtoSelecionado.getNome());
                txtUnidade.setText(produtoSelecionado.getUnidade());
                txtPreco.setText(String.valueOf(produtoSelecionado.getPreco()));
                txtQtdEstoque.setText(String.valueOf(produtoSelecionado.getQtdEstoque()));
                txtQtdMinima.setText(String.valueOf(produtoSelecionado.getQtdMinima()));
                txtQtdMaxima.setText(String.valueOf(produtoSelecionado.getQtdMaxima()));
                cmbCategoria.setSelectedItem(produtoSelecionado.getCategoria());
            }
        } catch (SQLException e) {
            Mensagem.erro("Erro ao buscar produto: " + e.getMessage());
        }
    }
