package component;


public class ScoreRecord {
    private String userName;
    private long score;

    public ScoreRecord() {        
    }

    public long getScore() {
        return score;
    }

    public String getUserName() {
        return userName;
    }

    public ScoreRecord(String name, long passedScore) {
        this.userName = name;
        this.score = passedScore;
    }
}
