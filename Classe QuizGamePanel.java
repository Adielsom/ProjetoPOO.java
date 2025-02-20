package GameFrame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class QuizGamePanel extends JPanel {
    private Player player;
    private JLabel questionLabel;
    private JLabel scoreLabel;
    private JButton[] answerButtons;
    private String correctAnswer;
    private int currentIndex = -1;
    private Random random = new Random();
    private List<Integer> availableIndices;
    private ImageIcon background;
    private int correctAnswers;
    private JFrame parentFrame;

    private String[][] quizData = {
            {"Qual é a capital do Brasil?", "Rio de Janeiro", "São Paulo", "Brasília", "Brasília"},
            {"Qual cidade se encontra o Cristo Redentor?", "Rio de Janeiro", "Salvador", "Recife", "Rio de Janeiro"},
            {"Qual estado tem o maior litoral?", "Bahia", "Pará", "Santa Catarina", "Bahia"},
            {"Qual rio corta a Amazônia?", "São Francisco", "Amazonas", "Paraná", "Amazonas"},
            {"Onde fica o Pantanal no mapa do Brasil?", "Pará", "Mato Grosso do Sul", "Amazonas", "Mato Grosso do Sul"},
            {"Qual estado é famoso pelo frevo?", "Pernambuco", "Ceará", "Bahia", "Pernambuco"},
            {"Onde fica o Pelourinho?", "Recife", "Salvador", "São Luís", "Salvador"},
            {"Qual é a maior cidade do Brasil?", "Brasília", "Rio de Janeiro", "São Paulo", "São Paulo"},
            {"Qual estado é da cidade de Foz do Iguaçu?", "Paraná", "Acre", "Amazonas", "Paraná"},
            {"Qual estado tem a cidade de Gramado?", "Santa Catarina", "Rio Grande do Sul", "Paraná", "Rio Grande do Sul"}
    };

    //contrutor da classe
    public QuizGamePanel(Player player, JFrame parentFrame) {
        this.player = player;
        this.parentFrame = parentFrame;
        this.background = new ImageIcon(getClass().getClassLoader().getResource("mapa/branco.png"));
        setLayout(new BorderLayout());

        //reinica a pontuação ao iniciar a fase
        player.setScore(0);
        correctAnswers = 0;

        //cria uma lista com os indices das perguntas embaralhadas
        availableIndices = new ArrayList<>();
        for (int i = 0; i < quizData.length; i++) {
            availableIndices.add(i);
        }
        Collections.shuffle(availableIndices);

        //painel superior para exibir a pontuação
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        //configura o rotulo da pergunta
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setForeground(Color.BLACK);
        topPanel.add(questionLabel, BorderLayout.CENTER);
        //configura o rotulo da pontuação
        scoreLabel = new JLabel("Pontuação: " + player.getScore(), SwingConstants.RIGHT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.BLACK);
        topPanel.add(scoreLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        //painel para os botões das respontas
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(3, 1, 10, 10));
        answerPanel.setOpaque(false);

        //cria os botões das respostas
        answerButtons = new JButton[3];
        for (int i = 0; i < 3; i++) {
            answerButtons[i] = new JButton();
            answerButtons[i].setFont(new Font("Arial", Font.BOLD, 16));
            answerButtons[i].setBackground(Color.LIGHT_GRAY);
            answerButtons[i].setForeground(Color.BLACK);
            answerButtons[i].setFocusPainted(false);
            answerButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            answerButtons[i].addActionListener(new AnswerListener());
            answerButtons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    ((JButton) e.getSource()).setBackground(Color.YELLOW);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    ((JButton) e.getSource()).setBackground(Color.LIGHT_GRAY);
                }
            });
            answerPanel.add(answerButtons[i]);
        }
        add(answerPanel, BorderLayout.CENTER);

        loadNewQuestion();//carrega a primeira pergunta
    }

    //desenha o fundo da tela
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    //carrega uma nova pergunta
    private void loadNewQuestion() {
        if (availableIndices.isEmpty()) { //se todas as perguntas forem respondidas
            showVictoryMessage();
            return;
        }

        //seleciona a proxima pergunta
        currentIndex = availableIndices.remove(0); //escolhe o proximo indice disponivel
        String[] questionData = quizData[currentIndex]; //obtém os dados das perguntas
        questionLabel.setText(questionData[0]); //define o texto de pergunta

        //cria uma lista de respostas embaralhadas
        List<String> answers = new ArrayList<>();
        for (int i = 1; i <= 3; i++) { //adiciona as opções de perguntas
            answers.add(questionData[i]);
        }
        Collections.shuffle(answers); //embaralha as respostas
        //define a resposta correta
        correctAnswer = questionData[4]; //define a resposta correta
        //define as respostas no botão
        for (int i = 0; i < 3; i++) {
            answerButtons[i].setText(answers.get(i));
        }
    }

    //calsse interna para tratar os eventos dos botões da resposta
    private class AnswerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();//obtem o botão de clique
            if (clickedButton.getText().equals(correctAnswer)) { //verifica se a resposta esta correta
                playSound("som/acerto.wav");
                player.addScore(10);
                correctAnswers++;
                scoreLabel.setText("Pontuação: " + player.getScore());
                loadNewQuestion(); //carrega a proxima pergunta
            } else {
                playSound("som/somerro.wav");
                JOptionPane.showMessageDialog(null, "Errado! Jogo reiniciado.");
                player.setScore(0);
                correctAnswers = 0;
                scoreLabel.setText("Pontuação: " + player.getScore());
                //reinicia a lista de perguntas
                availableIndices.clear();
                for (int i = 0; i < quizData.length; i++) {
                    availableIndices.add(i);
                }
                Collections.shuffle(availableIndices);
                loadNewQuestion();
            }
        }
    }

    //método que exibe a mensagem de vitória ao finalizar todas as perguntas
    private void showVictoryMessage() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel message = new JLabel("Parabéns, você ganhou!", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(message, BorderLayout.NORTH);
        //exibe o avatar
        if (player.getAvatarPath() != null) {
            ImageIcon avatarIcon = new ImageIcon(getClass().getClassLoader().getResource(player.getAvatarPath()));
            JLabel avatarLabel = new JLabel(avatarIcon);
            panel.add(avatarLabel, BorderLayout.CENTER);
        }

        int option = JOptionPane.showOptionDialog(null, panel, "Vitória!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Reiniciar", "Voltar ao Menu"}, "Reiniciar");

        if (option == 0) {
            player.setScore(0);
            correctAnswers = 0;
            scoreLabel.setText("Pontuação: " + player.getScore());

            availableIndices.clear();
            for (int i = 0; i < quizData.length; i++) {
                availableIndices.add(i);
            }
            Collections.shuffle(availableIndices);
            loadNewQuestion();
        } else {
            parentFrame.setContentPane(new MapPanel(player, parentFrame));
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }

    //metodo para tocar os sons
    private void playSound(String soundFile) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(soundFile);
            if (soundURL == null) {
                System.out.println("Erro ao carregar som: " + soundFile);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundURL.toURI()));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Erro ao tocar som: " + soundFile + " - " + e.getMessage());
        }
    }
}
