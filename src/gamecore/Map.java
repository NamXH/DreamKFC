package gamecore;


import component.ScoreRecord;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Map implements Serializable {

    private int mapID;
    private int theme;
    private int mapType;
    private String mapName;
    private String[] highScores = new String[5];
    private long[] score = new long[5];
    private ImageIcon preview;

    public Map() {
    }

    public int getMapType() {
        return mapType;
    }

    public Map(int aID, int aTheme, String aName, Image aPreview, int aType) {
        this.mapID = aID;
        this.mapType = aType;
        this.theme = aTheme;
        this.mapName = aName;
        preview = new ImageIcon(aPreview);
    }

    public void setID(int passedID) {
        this.mapID = passedID;
    }

    public int getID() {
        return this.mapID;
    }

    public void setTheme(int aTheme) {
        this.theme = aTheme;
    }

    public int getTheme() {
        return this.theme;
    }

    public void setName(String aName) {
        this.mapName = aName;
    }

    public String getName() {
        return this.mapName;
    }

    public void setPreview(Image aPreview) {
        preview.setImage(aPreview);
    }

    public Image getPreview() {
        return preview.getImage();
    }

    public ArrayList<ScoreRecord> getScoreList() {
        ArrayList<ScoreRecord> mapHighScores = new ArrayList<ScoreRecord>();
        for (int i = 0; i < 5; i++) {
            if (score[i] != 0) {
                ScoreRecord aRecord = new ScoreRecord(highScores[i], score[i]);
                mapHighScores.add(aRecord);
            }
        }
        return mapHighScores;
    }

    public boolean isHighScore(ScoreRecord aRecord) {

        ArrayList<ScoreRecord> mapHighScores = new ArrayList<ScoreRecord>();
        for (int i = 0; i < 5; i++) {
            if (score[i] != 0) {
                ScoreRecord aRecord1 = new ScoreRecord(highScores[i], score[i]);
                mapHighScores.add(aRecord1);
            }
        }

        int length = mapHighScores.size();
        boolean isHigh = false;
        int i = 0;

        if (length < 5) {
            isHigh = true;
        } else {
            for (i = 0; i < length; i++) {
                if ((aRecord.getScore()) > (mapHighScores.get(i).getScore())) {
                    isHigh = true;
                    break;
                }
            }
        }
        return isHigh;
    }

    public void addHighScore(ScoreRecord aRecord) {

        ArrayList<ScoreRecord> mapHighScores = new ArrayList<ScoreRecord>();
        for (int i = 0; i < 5; i++) {
            if (score[i] != 0) {
                ScoreRecord aRecord1 = new ScoreRecord(highScores[i], score[i]);
                mapHighScores.add(aRecord1);
            }
        }

        int length = mapHighScores.size();

        int i = 0;

        if (length < 5) {
            if (length == 0) {
                mapHighScores.add(aRecord);
            } else {
                boolean alreadyAdded=false;
                for (i = 0; i < length; i++) {
                    if ((aRecord.getScore()) > (mapHighScores.get(i).getScore())) {
                        mapHighScores.add(i, aRecord);
                        alreadyAdded=true;
                        break;
                    }
                }
                if (!alreadyAdded) mapHighScores.add(i, aRecord);
            }
        } else {
            for (i = 0; i < length; i++) {
                if ((aRecord.getScore()) > (mapHighScores.get(i).getScore())) {
                    mapHighScores.add(i, aRecord);
                    if (mapHighScores.size() > 5) {
                        mapHighScores.remove(5);
                    }
                    break;
                }
            }
        }

        length = mapHighScores.size();

        for (int k = 0; k < length; k++) {
            highScores[k] = mapHighScores.get(k).getUserName();
            score[k] = mapHighScores.get(k).getScore();
        }
    }
}
