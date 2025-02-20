package GameFrame;
import javax.swing.*;
import java.awt.Font;
import java.awt.Color;



public class GameFrame extends JFrame {
    private Player player; //objeto para armazenar imformações do jogador

    //construtor da classe gameframe
    public GameFrame() {

        setTitle("Explorador Geográfico: Missão ao Redor do Mundo");
        setSize(800, 600); //dimensão inicial
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //centraliza a janela

        //inicia o jogador com o nome padrão
        player = new Player("");

        //adiciona o painel no menu principal
        MainMenuPanel mainMenuPanel = new MainMenuPanel();
        add(mainMenuPanel);
    }


    //classe interna responsavel pelo menu principal
    private class MainMenuPanel extends JPanel {
        private JTextField nameField;//coloca o nome do jogador

        public MainMenuPanel() {
            setLayout(null);//define o layout manual para posicionar os elementos livremente

            // Configura imagem de fundo
            JLabel backgroundLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("mapa/3.png")));
            backgroundLabel.setBounds(0, 0, 800, 600);
            add(backgroundLabel);

            // Campo para inserir o nome
            JLabel nameLabel = new JLabel("Digite seu nome:");
            nameLabel.setBounds(300, 200, 200, 30);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            backgroundLabel.add(nameLabel);

            nameField = new JTextField();
            nameField.setBounds(300, 240, 200, 30);
            backgroundLabel.add(nameField);

            // Botões de avatar
            JLabel avatarLabel = new JLabel("Escolha seu avatar:");
            avatarLabel.setBounds(300, 280, 200, 30);
            avatarLabel.setForeground(Color.WHITE);
            avatarLabel.setFont(new Font("Arial", Font.BOLD, 16));
            backgroundLabel.add(avatarLabel);

            JButton maleAvatarButton = new JButton("Masculino");
            maleAvatarButton.setBounds(300, 320, 90, 30);
            backgroundLabel.add(maleAvatarButton);

            JButton femaleAvatarButton = new JButton("Feminino");
            femaleAvatarButton.setBounds(410, 320, 90, 30);
            backgroundLabel.add(femaleAvatarButton);

            //adicionando ações ao botao do avatar
            maleAvatarButton.addActionListener(e -> {
                String avatarPath = "player1/jogador1.png";
                player.setAvatarPath(avatarPath);
                JOptionPane.showMessageDialog(null, "Avatar masculino selecionado");
            });

            femaleAvatarButton.addActionListener(e -> {
                String avatarPath = "player2/jogador2.png";
                player.setAvatarPath(avatarPath);
                JOptionPane.showMessageDialog(null, "Avatar feminino selecionado");
            });

            // Botão para iniciar o jogo
            JButton startButton = new JButton("Iniciar Jogo");
            startButton.setBounds(350, 400, 100, 40);
            startButton.setBackground(Color.CYAN);
            startButton.setFont(new Font("Arial", Font.BOLD, 14));
            backgroundLabel.add(startButton);

            startButton.addActionListener(e -> startGame());
        }
    }

    //Método para iniciar o jogo e trocar de tela
    private void startGame() {
        //obtem o nome do jogador inserido no campo de texto
        String playerName = ((MainMenuPanel) getContentPane().getComponent(0)).nameField.getText();
        //verifica se o jogador colocou o nome
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, insira seu nome antes de começar o jogo.");
            return;
        }
        //verifica se escolheu o avatar
        if (player.getAvatarPath() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, escolha um avatar antes de começar o jogo.");
            return;
        }

        //atualiza o nome do jogador
        player.setName(playerName);
        JOptionPane.showMessageDialog(null, "Bem-vindo, " + playerName + "! O jogo vai começar.");

        //remove o painel do menu e adiciona o mapa
        getContentPane().removeAll();
        MapPanel mapPanel = new MapPanel(player, this);
        add(mapPanel);

        //atualiza a tela
        revalidate();
        repaint();
    }
}
