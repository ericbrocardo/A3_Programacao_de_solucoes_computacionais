package visao;

import javax.swing.JOptionPane;

/**
 * Classe utilitária para exibição de mensagens padronizadas ao usuário.
 * Fornece métodos estáticos para exibir diálogos de informação,
 * erro, aviso e confirmação.
 */
public class Mensagem {

    /**
     * Exibe uma mensagem de informação ao usuário.
     *
     * @param msg Mensagem a ser exibida
     */
    public static void info(String msg) {
        JOptionPane.showMessageDialog(null, msg,
                "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Exibe uma mensagem de erro ao usuário.
     *
     * @param msg Mensagem de erro a ser exibida
     */
    public static void erro(String msg) {
        JOptionPane.showMessageDialog(null, msg,
                "Erro", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Exibe uma mensagem de aviso ao usuário.
     *
     * @param msg Mensagem de aviso a ser exibida
     */
    public static void aviso(String msg) {
        JOptionPane.showMessageDialog(null, msg,
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Exibe uma caixa de diálogo de confirmação ao usuário.
     *
     * @param msg Mensagem de confirmação a ser exibida
     * @return true se o usuário confirmar, false caso contrário
     */
    public static boolean confirmar(String msg) {
        int r = JOptionPane.showConfirmDialog(null, msg,
                "Confirmação", JOptionPane.YES_NO_OPTION);
        return r == JOptionPane.YES_OPTION;
    }
}