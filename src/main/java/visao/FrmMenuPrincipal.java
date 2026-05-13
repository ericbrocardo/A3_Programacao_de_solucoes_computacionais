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

            // Painel de botões
            JPanel pnlBotoes = new JPanel(new GridLayout(5, 1, 0, 10));
            pnlBotoes.setBackground(new Color(44, 62, 80));
            pnlBotoes.setBorder(BorderFactory.createEmptyBorder(0, 60, 30, 60));

            JButton btnCategorias = criarBotao("🏷   Categorias", new Color(41, 128, 185));
            JButton btnProdutos = criarBotao("📦   Produtos", new Color(39, 174, 96));
            JButton btnMovimentacoes = criarBotao("🔄   Movimentações", new Color(142, 68, 173));
            JButton btnReajuste = criarBotao("💲   Reajuste de Preços", new Color(230, 126, 34));
            JButton btnRelatorios = criarBotao("📊   Relatórios", new Color(192, 57, 43));

            pnlBotoes.add(btnCategorias);
            pnlBotoes.add(btnProdutos);
            pnlBotoes.add(btnMovimentacoes);
            pnlBotoes.add(btnReajuste);
            pnlBotoes.add(btnRelatorios);

            pnlPrincipal.add(pnlBotoes, BorderLayout.CENTER);
            add(pnlPrincipal);
        }
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 42));
        return btn;
    }
