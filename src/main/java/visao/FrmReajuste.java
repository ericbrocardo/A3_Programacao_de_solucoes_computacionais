package visao;

import dao.ProdutoDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class FrmReajuste extends JFrame {

    private JTextField txtPercentual;
    private JLabel lblResultado;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public FrmReajuste() {
        initComponents();
    }
}
