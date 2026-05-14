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

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }
}
