package principal;

import dao.ConexaoDB;
import visao.FrmMenuPrincipal;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Classe principal do sistema de controle de estoque.
 * Responsável por inicializar a aplicação, testar a conexão
 * com o banco de dados e abrir a tela do menu principal.
 */
public class Principal {

    /**
     * Método de entrada da aplicação.
     * Testa a conexão com o banco de dados ao iniciar e,
     * caso bem-sucedida, abre a tela do menu principal.
     * Em caso de falha na conexão, exibe uma mensagem de erro
     * e encerra a aplicação.
     *
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try {
            ConexaoDB.getConnection();
            System.out.println("Conexão com o banco estabelecida!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao conectar com o banco de dados:\n" + e.getMessage(),
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            FrmMenuPrincipal tela = new FrmMenuPrincipal();
            tela.setLocationRelativeTo(null);
            tela.setVisible(true);
        });
    }
}