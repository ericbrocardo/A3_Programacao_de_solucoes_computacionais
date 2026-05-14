package visao;

import javax.swing.JOptionPane;

public class Mensagem {

    public static void info(String msg) {
        JOptionPane.showMessageDialog(null, msg,
                "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void erro(String msg) {
        JOptionPane.showMessageDialog(null, msg,
                "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void aviso(String msg) {
        JOptionPane.showMessageDialog(null, msg,
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public static boolean confirmar(String msg) {
        int r = JOptionPane.showConfirmDialog(null, msg,
                "Confirmação", JOptionPane.YES_NO_OPTION);
        return r == JOptionPane.YES_OPTION;
    }
}
