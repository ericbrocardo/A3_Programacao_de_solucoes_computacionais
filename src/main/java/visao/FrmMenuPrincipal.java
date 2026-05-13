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
        
        //Painel Principal
        JPanel pnlPrincipal = new JPanel(new BorderLayout());
        pnlPrincipal.setBackground(new Color(44, 62, 80));

        //Cabeçalho
        JLabel lblTitulo = new JLabel("📦 Controle de Estoque", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
        pnlPrincipal.add(lblTitulo, BorderLayout.NORTH);
        
        add(pnlPrincipal);
    }
}
