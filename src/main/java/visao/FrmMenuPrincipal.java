package visao;

import javax.swing.*;
import java.awt.*;

public class FrmMenuPrincipal extends JFrame {

    public FrmMenuPrincipal() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Controle de Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 400);
        setResizable(false);
    }
}
