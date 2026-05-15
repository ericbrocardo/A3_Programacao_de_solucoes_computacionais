package visao;

import dao.CategoriaDAO;
import modelo.Categoria;
import modelo.Categoria.Embalagem;
import modelo.Categoria.Tamanho;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

  

    public class FrmCategoria extends JFrame {

        private JTextField txtNome;
        private JComboBox<Tamanho> cmbTamanho;
        private JComboBox<Embalagem> cmbEmbalagem;
        private JTable tabela;
        private DefaultTableModel modelo;

        private final CategoriaDAO dao = new CategoriaDAO();
        private Categoria categoriaSelecionada = null;

        public FrmCategoria() {
            initComponents();
            carregarTabela();
        }

        private void initComponents() {
            setTitle("Categorias");
            setSize(620, 480);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout(0, 10));

            JPanel pnlForm = new JPanel(new GridBagLayout());
            pnlForm.setBorder(BorderFactory.createTitledBorder("Dados da Categoria"));
            pnlForm.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 8, 6, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Nome
            gbc.gridx = 0;
            gbc.gridy = 0;
            pnlForm.add(new JLabel("Nome:"), gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            gbc.weightx = 1;
            txtNome = new JTextField();
            pnlForm.add(txtNome, gbc);

            // Tamanho
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.weightx = 0;
            pnlForm.add(new JLabel("Tamanho:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 0.5;
            cmbTamanho = new JComboBox<>(Tamanho.values());
            cmbTamanho.insertItemAt(null, 0);
            cmbTamanho.setSelectedIndex(0);
            pnlForm.add(cmbTamanho, gbc);

            // Embalagem
            gbc.gridx = 2;
            gbc.weightx = 0;
            pnlForm.add(new JLabel("Embalagem:"), gbc);
            gbc.gridx = 3;
            gbc.weightx = 0.5;
            cmbEmbalagem = new JComboBox<>(Embalagem.values());
            cmbEmbalagem.insertItemAt(null, 0);
            cmbEmbalagem.setSelectedIndex(0);
            pnlForm.add(cmbEmbalagem, gbc);

            // ── Botões ──────────────────────────────────
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
            gbc.gridy = 2;
            gbc.gridwidth = 4;
            pnlForm.add(pnlBotoes, gbc);

            add(pnlForm, BorderLayout.NORTH);

            // ── Tabela ──────────────────────────────────
            modelo = new DefaultTableModel(
                    new String[]{"ID", "Nome", "Tamanho", "Embalagem"}, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

            tabela = new JTable(modelo);
            tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tabela.setRowHeight(24);
            tabela.getColumnModel().getColumn(0).setMaxWidth(50);
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

    private void salvar() {
        String nome = txtNome.getText().trim();
        Tamanho tamanho = (Tamanho) cmbTamanho.getSelectedItem();
        Embalagem embal = (Embalagem) cmbEmbalagem.getSelectedItem();

        if (nome.isEmpty() || tamanho == null || embal == null) {
            Mensagem.aviso("Preencha todos os campos.");
            return;
        }

        try {
            if (categoriaSelecionada == null) {
                dao.inserir(new Categoria(0, nome, tamanho, embal));
                Mensagem.info("Categoria cadastrada com sucesso!");
            } else {
                categoriaSelecionada.setNome(nome);
                categoriaSelecionada.setTamanho(tamanho);
                categoriaSelecionada.setEmbalagem(embal);
                dao.atualizar(categoriaSelecionada);
                Mensagem.info("Categoria atualizada com sucesso!");
            }
            limpar();
            carregarTabela();
        } catch (SQLException e) {
            Mensagem.erro("Erro ao salvar: " + e.getMessage());
        }
    }

    private void excluir() {
        if (categoriaSelecionada == null) {
            Mensagem.aviso("Selecione uma categoria para excluir.");
            return;
        }
        if (Mensagem.confirmar("Deseja excluir a categoria \"" + categoriaSelecionada.getNome() + "\"?")) {
            try {
                dao.excluir(categoriaSelecionada.getId());
                Mensagem.info("Categoria excluída com sucesso!");
                limpar();
                carregarTabela();
            } catch (SQLException e) {
                Mensagem.erro("Erro ao excluir: " + e.getMessage());
            }
        }
    }

    private void limpar() {
        txtNome.setText("");
        cmbTamanho.setSelectedIndex(0);
        cmbEmbalagem.setSelectedIndex(0);
        tabela.clearSelection();
        categoriaSelecionada = null;
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        int id = (int) modelo.getValueAt(linha, 0);
        try {
            categoriaSelecionada = dao.buscarPorId(id);
            if (categoriaSelecionada != null) {
                txtNome.setText(categoriaSelecionada.getNome());
                cmbTamanho.setSelectedItem(categoriaSelecionada.getTamanho());
                cmbEmbalagem.setSelectedItem(categoriaSelecionada.getEmbalagem());
            }
        } catch (SQLException e) {
            Mensagem.erro("Erro ao buscar categoria: " + e.getMessage());
        }
    }
