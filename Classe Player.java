package GameFrame;

public class Player {
    private String nome;
    private int score;
    private String avatarPath;

    public Player(String nome) {
        this.nome = nome;
        this.score = 0;
        this.avatarPath = null;
    }

    public String getName() {
        return nome;
    }

    public void setName(String name) {
        this.nome = name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
