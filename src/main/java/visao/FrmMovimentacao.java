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