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
    }
}
