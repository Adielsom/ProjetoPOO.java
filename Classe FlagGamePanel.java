package GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class FlagGamePanel extends JPanel {
    private Player player; //objeto que armazena as informações do jogador
    private JFrame parentFrame; //referencia ao jframe principal para a navegação
    private JLabel questionLabel; //exibe as perguntas sonbre as bandeiras
    private JLabel scoreLabel; //exibe a pontuação do jogador
    private JButton[] flagButtons; //botões da bandeira
    private String correctFlag; //caminho da bandeira
    private String correctCountry; //nome do estado correspondente da bandeira
    private int currentIndex = -1; //indice atual da bandeira correta
    private Random random = new Random(); //objeto para criar valores aleatorios
    private List<Integer> availableIndices; //lista de indices das bandeiras disponiveis
    private JPanel flagPanel; //painel onde os botões das bandeiras aparece
    private ImageIcon background; //imagem de fundo
    private Clip backgroundMusic; //musica de fundo

    //matriz contendo os caminhos das imagens das bandeiras e os nomes dos estados
    private String[][] flagData = {
            {"flags/acre.png", "Acre"},
            {"flags/alagoas.png", "Alagoas"},
            {"flags/amapa.png", "Amapá"},
            {"flags/amazonas.png", "Amazonas"},
            {"flags/bahia.png", "Bahia"},
            {"flags/ceara.png", "Ceará"},
            {"flags/distrito_federal.png", "Distrito Federal"},
            {"flags/esperito_santos.png", "Espírito Santo"},
            {"flags/goias.png", "Goiás"},
            {"flags/maranhao.png", "Maranhão"},
            {"flags/mtgrosso.png", "Mato Grosso"},
            {"flags/mtgrossosul.png", "Mato Grosso do Sul"},
            {"flags/minas.png", "Minas Gerais"},
            {"flags/para.png", "Pará"},
            {"flags/paraiba.png", "Paraíba"},
            {"flags/pernanbuco.png", "Pernambuco"},
            {"flags/piaui.png", "Piauí"},
            {"flags/riojaneiro.png", "Rio de Janeiro"},
            {"flags/riograndenorte.png", "Rio Grande do Norte"},
            {"flags/rondonia.png", "Rondônia"},
            {"flags/roraima.png", "Roraima"},
            {"flags/saopaulo.png", "São Paulo"},
            {"flags/sergipe.png", "Sergipe"},
            {"flags/tocantins.png", "Tocantins"},
            {"flags/riosul.png", "Rio Grande do Sul"},
            {"flags/santa.png", "Santa Catarina"},
            {"flags/parana.png", "Paraná"}
    };
    //construtor da classe
    public FlagGamePanel(Player player, JFrame parentFrame) {
        this.player = player;
        this.parentFrame = parentFrame;
        this.background = new ImageIcon(getClass().getClassLoader().getResource("mapa/fundo.png"));
        setLayout(new BorderLayout());

        playBackgroundMusic(); //inicia a musica

        availableIndices = new ArrayList<>();
        for (int i = 0; i < flagData.length; i++) {
            availableIndices.add(i);
        }
        Collections.shuffle(availableIndices);

        //painel superior que contém a pergunta e a pontuação
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        //exibe a pergunta
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setForeground(Color.BLACK);
        topPanel.add(questionLabel, BorderLayout.CENTER);
        //exibe a pontuação
        scoreLabel = new JLabel("Pontuação: " + player.getScore(), SwingConstants.RIGHT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.BLACK);
        topPanel.add(scoreLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        //painel onde as bandeiras são exibidas
        flagPanel = new JPanel();
        flagPanel.setOpaque(false);
        add(flagPanel, BorderLayout.CENTER);

        loadNewFlags();//carrega as bandeiras iniciais
    }

    //método para desenhar o fundo do jogo
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
    //método para tocar a musica de fundo
    private void playBackgroundMusic() {
        playSound("som/somfundo.wav", true);
    }
    //método para tocar a musica de acerto
    private void playCorrectSound() {
        playSound("som/acerto.wav", false);
    }
    //método para tocar a musica de erro
    private void playErrorSound() {
        playSound("som/somerro.wav", false);
    }
    //método  para tocar os sons
    private void playSound(String soundFile, boolean loop) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(soundFile);
            if (soundURL == null) {
                System.out.println("Erro ao carregar som: " + soundFile);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundURL.toURI()));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();
        } catch (Exception e) {
            System.out.println("Erro ao tocar som: " + soundFile + " - " + e.getMessage());
        }
    }
    //método para carregar novas bandeiras
    private void loadNewFlags() {
        flagPanel.removeAll();
        Collections.shuffle(availableIndices);
        if (availableIndices.isEmpty()) {
            for (int i = 0; i < flagData.length; i++) {
                availableIndices.add(i);
            }
            Collections.shuffle(availableIndices);
        }
        currentIndex = availableIndices.remove(0); //pega o proximo indice disponivel
        correctFlag = flagData[currentIndex][0]; //caminho da bandeira
        correctCountry = flagData[currentIndex][1]; //nome do estado correto
        questionLabel.setText("Onde está a bandeira de " + correctCountry + "?");

        List<Integer> indices = new ArrayList<>(availableIndices);
        Collections.shuffle(indices);

        int numFlags = player.getScore() >= 100 ? 6 : 3; //aumenta a dificuldade com as bandeiras depois de 100 pontos
        while (indices.size() < numFlags - 1) {
            indices.add(random.nextInt(flagData.length));
        }
        indices = indices.subList(0, numFlags - 1);
        indices.add(currentIndex);
        Collections.shuffle(indices);

        flagPanel.setLayout(new GridLayout(indices.size() > 3 ? 2 : 1, 3, 10, 10));

        flagButtons = new JButton[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            int flagIndex = indices.get(i);
            flagButtons[i] = new JButton(new ImageIcon(getClass().getClassLoader().getResource(flagData[flagIndex][0])));
            final String selectedFlag = flagData[flagIndex][0];
            flagButtons[i].addActionListener(e -> handleAnswer(selectedFlag));
            flagPanel.add(flagButtons[i]);
        }
        revalidate();
        repaint();
    }
    //método para verificar a resposta do jogador
    private void handleAnswer(String selectedFlag) {
        if (selectedFlag.equals(correctFlag)) { //verifica se a bandeira clicada é a correta
            playCorrectSound(); //toca o som de acerto
            player.addScore(10); //adiciona 10 pontos
            scoreLabel.setText("Pontuação: " + player.getScore()); //atualiza a pontuação na tela
            if (player.getScore() >= 200) {
                int option = JOptionPane.showOptionDialog(this, "Parabéns! Você completou o jogo! Deseja reiniciar ou voltar ao menu?", "Fim de Jogo",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Reiniciar", "Menu"}, "Reiniciar");
                if (option == JOptionPane.YES_OPTION) {
                    player.setScore(0);
                    loadNewFlags();
                } else {
                    parentFrame.setContentPane(new MapPanel(player, parentFrame));
                    parentFrame.revalidate();
                }
            } else {
                loadNewFlags(); //carrega nova pergunta
            }
        } else {
            playErrorSound();
            player.setScore(0); //zera a pontuação
            scoreLabel.setText("Pontuação: " + player.getScore());
            loadNewFlags(); //recarrega novas bandeiras
        }
    }
}
