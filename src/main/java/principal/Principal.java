package principal;

import dao.ConexaoDB;
import visao.FrmMenuPrincipal;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Principal {

    public static void main(String[] args) {
        // Testa conexão com o banco ao iniciar
        try {
            ConexaoDB.getConnection();
            System.out.println("Conexão com o banco estabelecida!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao conectar com o banco de dados:\n" + e.getMessage(),
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Inicia a interface gráfica
        SwingUtilities.invokeLater(() -> {
            FrmMenuPrincipal tela = new FrmMenuPrincipal();
            tela.setLocationRelativeTo(null);
            tela.setVisible(true);
        });
    }
}