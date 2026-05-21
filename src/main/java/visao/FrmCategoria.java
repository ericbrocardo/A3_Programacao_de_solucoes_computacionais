package visao;

import dao.CategoriaDAO;
import modelo.Categoria;
import modelo.Categoria.Embalagem;
import modelo.Categoria.Tamanho;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Tela de cadastro e gerenciamento de categorias.
 * Permite inserir, atualizar, excluir e listar categorias no sistema.
 */
public class FrmCategoria extends JFrame {

    private JTextField txtNome;
    private JComboBox<Tamanho> cmbTamanho;
    private JComboBox<Embalagem> cmbEmbalagem;
    private JTable tabela;
    private DefaultTableModel modelo;

    private final CategoriaDAO dao = new CategoriaDAO();
    private Categoria categoriaSelecionada = null;

    /**
     * Construtor da tela de categorias.
     * Inicializa os componentes e carrega os dados da tabela.
     */
    public FrmCategoria() {
        initComponents();
        carregarTabela();
    }

    /**
     * Inicializa e organiza todos os componentes visuais da tela.
     */
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

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        txtNome = new JTextField();
        pnlForm.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
        pnlForm.add(new JLabel("Tamanho:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.5;
        cmbTamanho = new JComboBox<>(Tamanho.values());
        cmbTamanho.insertItemAt(null, 0);
        cmbTamanho.setSelectedIndex(0);
        pnlForm.add(cmbTamanho, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        pnlForm.add(new JLabel("Embalagem:"), gbc);

        gbc.gridx = 3; gbc.weightx = 0.5;
        cmbEmbalagem = new JComboBox<>(Embalagem.values());
        cmbEmbalagem.insertItemAt(null, 0);
        cmbEmbalagem.setSelectedIndex(0);
        pnlForm.add(cmbEmbalagem, gbc);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlBotoes.setBackground(Color.WHITE);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnExcluir = new JButton("Excluir");

        estilizarBotao(btnSalvar, new Color(39, 174, 96));
        estilizarBotao(btnLimpar, new Color(149, 165, 166));
        estilizarBotao(btnExcluir, new Color(231, 76, 60));

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());
        btnExcluir.addActionListener(e -> excluir());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnLimpar);
        pnlBotoes.add(btnExcluir);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        pnlForm.add(pnlBotoes, gbc);

        add(pnlForm, BorderLayout.NORTH);

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

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarLinha();
            }
        });

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /**
     * Salva ou atualiza uma categoria no banco de dados.
     * Se nenhuma categoria estiver selecionada, realiza uma inserção.
     * Caso contrário, atualiza a categoria selecionada.
     */
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
        } catch (Exception e) {
            Mensagem.erro("Erro ao salvar: " + e.getMessage());
        }
    }

    /**
     * Exclui a categoria selecionada na tabela após confirmação do usuário.
     * Exibe mensagem de erro caso existam produtos vinculados à categoria.
     */
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
            } catch (Exception e) {
                String erro = e.getMessage();
                if (erro != null && (
                        erro.contains("foreign key constraint fails")
                        || erro.contains("Cannot delete or update a parent row")
                        || erro.contains("produto")
                        || erro.contains("categoria_id"))) {
                    Mensagem.erro("Não é possível excluir esta categoria, pois existem produtos cadastrados nela.");
                } else {
                    Mensagem.erro("Erro ao excluir categoria: " + erro);
                }
            }
        }
    }

    /**
     * Limpa os campos do formulário e remove a seleção da tabela.
     */
    private void limpar() {
        txtNome.setText("");
        cmbTamanho.setSelectedIndex(0);
        cmbEmbalagem.setSelectedIndex(0);
        tabela.clearSelection();
        categoriaSelecionada = null;
    }

    /**
     * Carrega os dados da categoria selecionada na tabela para os campos do formulário.
     */
    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;

        int id = (int) modelo.getValueAt(linha, 0);
        try {
            categoriaSelecionada = dao.buscarPorId(id);
            if (categoriaSelecionada != null) {
                txtNome.setText(categoriaSelecionada.getNome());
                cmbTamanho.setSelectedItem(categoriaSelecionada.getTamanho());
                cmbEmbalagem.setSelectedItem(categoriaSelecionada.getEmbalagem());
            }
        } catch (Exception e) {
            Mensagem.erro("Erro ao buscar categoria: " + e.getMessage());
        }
    }

    /**
     * Carrega todas as categorias do banco de dados e exibe na tabela.
     */
    private void carregarTabela() {
        modelo.setRowCount(0);
        try {
            List<Categoria> lista = dao.listarTodas();
            for (Categoria c : lista) {
                modelo.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getTamanho(),
                    c.getEmbalagem()
                });
            }
        } catch (Exception e) {
            Mensagem.erro("Erro ao carregar categorias: " + e.getMessage());
        }
    }

    /**
     * Aplica estilo visual padronizado a um botão.
     *
     * @param btn Botão a ser estilizado
     * @param cor Cor de fundo a ser aplicada
     */
    private void estilizarBotao(JButton btn, Color cor) {
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 32));
    }
}