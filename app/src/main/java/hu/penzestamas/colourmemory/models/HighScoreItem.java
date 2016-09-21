package hu.penzestamas.colourmemory.models;

/**
 * POJO for a high score.
 */
public class HighScoreItem {

    private String name;

    private int score;

    public HighScoreItem() {
    }

    public HighScoreItem(String name, int score) {
        this.name = name;
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
