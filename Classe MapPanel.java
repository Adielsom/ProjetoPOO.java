package GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapPanel extends JPanel {
    private Player player; //objeto que armazena informações do jogador
    private JFrame parentFrame; //referencia ao jframe principal
    private JLabel fase1Label; //label que representa a area clicavel da fase1
    private JLabel fase2Label;

    public MapPanel(Player player, JFrame parentFrame) {
        this.player = player; //inicia o jogador
        this.parentFrame = parentFrame; //inicia o frame principal
        setLayout(null); //layout manual para posicionar os elementos livremente

        // Carregar o fundo do mapa
        JLabel backgroundLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("mapa/map1.png")));
        backgroundLabel.setBounds(0, 0, 800, 600);
        add(backgroundLabel);

        // Exibir avatar escolhido
        if (player.getAvatarPath() != null) {
            ImageIcon avatarIcon = new ImageIcon(getClass().getClassLoader().getResource(player.getAvatarPath()));
            JLabel avatarLabel = new JLabel(avatarIcon);
            avatarLabel.setBounds(10, 450, 100, 150); // Posição do avatar
            backgroundLabel.add(avatarLabel);
        }

        // Mensagem ao lado do avatar
        JLabel messageLabel = new JLabel("Explorador Geográfico! Escolha uma fase.");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBounds(250, 30, 400, 30);
        backgroundLabel.add(messageLabel);

        // Criar botões clicáveis para as fases
        fase1Label = new JLabel("FASE 1", SwingConstants.CENTER);
        fase1Label.setFont(new Font("Arial", Font.BOLD, 20));
        fase1Label.setOpaque(true);
        fase1Label.setBackground(Color.BLUE);
        fase1Label.setForeground(Color.WHITE);
        fase1Label.setBounds(300, 200, 200, 50);
        fase1Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        backgroundLabel.add(fase1Label);

        fase2Label = new JLabel("FASE 2", SwingConstants.CENTER);
        fase2Label.setFont(new Font("Arial", Font.BOLD, 20));
        fase2Label.setOpaque(true);
        fase2Label.setBackground(Color.RED);
        fase2Label.setForeground(Color.WHITE);
        fase2Label.setBounds(300, 300, 200, 50);
        fase2Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        backgroundLabel.add(fase2Label);

        // Detectar clique na Fase 1
        fase1Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openFlagGame();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                fase1Label.setBackground(Color.CYAN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fase1Label.setBackground(Color.BLUE);
            }
        });

        // Detectar clique na Fase 2
        fase2Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openQuizGame();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                fase2Label.setBackground(Color.PINK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fase2Label.setBackground(Color.RED);
            }
        });
    }

    // Método para iniciar a Fase 1 (Jogo de Bandeiras)
    public void openFlagGame() {
        parentFrame.setContentPane(new FlagGamePanel(player, parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    // Método para iniciar a Fase 2 (Jogo de Perguntas e Respostas)
    public void openQuizGame() {
        parentFrame.setContentPane(new QuizGamePanel(player, parentFrame)); // Agora passando os dois parâmetros
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
