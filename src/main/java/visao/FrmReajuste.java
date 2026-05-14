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
    private void initComponents() {
        setTitle("Reajuste de Preços");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel pnlCentro = new JPanel(new GridBagLayout());
        pnlCentro.setBackground(Color.WHITE);
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        gbc.gridy = 0;
        JLabel lblInstrucao = new JLabel(
            "<html><center>Informe o percentual de reajuste a ser<br>" +
            "aplicado em <b>todos</b> os produtos do estoque.</center></html>",
            SwingConstants.CENTER
        );
        lblInstrucao.setForeground(new Color(100, 100, 100));
        pnlCentro.add(lblInstrucao, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.weightx = 1;
        txtPercentual = new JTextField();
        txtPercentual.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPercentual.setHorizontalAlignment(JTextField.CENTER);
        txtPercentual.setPreferredSize(new Dimension(0, 38));
        pnlCentro.add(txtPercentual, gbc);

        gbc.gridx = 1; gbc.weightx = 0;
        JLabel lblPct = new JLabel("%");
        lblPct.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlCentro.add(lblPct, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1;
        JButton btnAplicar = new JButton("Aplicar Reajuste");
        estilizarBotao(btnAplicar, new Color(230, 126, 34));
        btnAplicar.setPreferredSize(new Dimension(0, 40));
        btnAplicar.addActionListener(e -> aplicar());
        pnlCentro.add(btnAplicar, gbc);

        gbc.gridy = 3;
        lblResultado = new JLabel("", SwingConstants.CENTER);
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlCentro.add(lblResultado, gbc);

        add(pnlCentro, BorderLayout.CENTER);
    }
        private void aplicar() {
        String texto = txtPercentual.getText().trim().replace(",", ".");

        if (texto.isEmpty()) {
            lblResultado.setForeground(new Color(230, 126, 34));
            lblResultado.setText("Informe o percentual de reajuste.");
            return;
        }

        double percentual;
        try {
            percentual = Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            lblResultado.setForeground(new Color(231, 76, 60));
            lblResultado.setText("Valor inválido. Use números como 10 ou 10.5");
            return;
        }

        if (percentual == 0) {
            lblResultado.setForeground(new Color(230, 126, 34));
            lblResultado.setText("O percentual não pode ser zero.");
            return;
        }

        String direcao = percentual > 0 ? "aumento" : "redução";
        String msg = String.format(
            "Confirma o %s de %.2f%% nos preços\nde TODOS os produtos?",
            direcao, Math.abs(percentual)
        );

        if (Mensagem.confirmar(msg)) {
            try {
                produtoDAO.reajustarPrecos(percentual);
                lblResultado.setForeground(new Color(39, 174, 96));
                lblResultado.setText(String.format(
                    "✔ Reajuste de %.2f%% aplicado com sucesso!", percentual
                ));
                txtPercentual.setText("");
            } catch (SQLException e) {
                lblResultado.setForeground(new Color(231, 76, 60));
                lblResultado.setText("Erro: " + e.getMessage());
            }
        }
    }
        
    private void estilizarBotao(JButton btn, Color cor) {
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
 }