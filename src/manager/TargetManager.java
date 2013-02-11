package manager;


import component.Target;
import component.ScoreUp;
import component.Food;
import java.util.ArrayList;
import java.util.List;

//to test:
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TargetManager {

    private List<Target> targetList;
    private int i, serveOK;
    private Graphics2D g;
    private List<Integer> requestedFoods;
    private List<ScoreUp> additional_scoreUp;
    private long additional_score;
    private int thisMapID, thisMapType;

    public TargetManager(Graphics2D g, int anID, int mapType) {
        targetList = new ArrayList();
        this.g = g;
        this.requestedFoods = new ArrayList();
        thisMapID = anID;
        thisMapType = mapType;
    }

    public List<Target> loadDB() {

        int start, end;
        char c;
        String sub;
        int count;
        int x, y, direction;

        File f;

        if (thisMapType == 1) {
            f = new File("Data/campaign/campaign"+ thisMapID +".map");
        } else {
            f = new File("Data/custom/custom"+ thisMapID +".map");
        }

        FileReader fr = null;
        try {
            fr = new FileReader(f);
            BufferedReader bf = new BufferedReader(fr);
            String s = null;
            while ((s = bf.readLine()) != null) {
                if ((s.charAt(0) != '#') && (s.contains("T "))) {
                    start = 0;
                    end = 0;
                    count = 1;
                    x = 0;
                    y = 0;
                    direction = 0;

                    while (end < s.length()) {
                        c = s.charAt(end);
                        while ((c != ' ') && (end < s.length() - 1)) {
                            end++;
                            c = s.charAt(end);
                        }

                        if (start == end) {
                            sub = s.substring(start, end + 1);
                        } else {
                            sub = s.substring(start, end);
                        }                        
                        switch (count) {
                            case 1: {
                                x = Integer.parseInt(sub);
                                break;
                            }
                            case 2: {
                                y = Integer.parseInt(sub);
                                break;
                            }
                            case 3: {
                                break;
                            }
                            case 4: {
                                direction = Integer.parseInt(sub);
                                break;
                            }
                        }
                        end++;
                        start = end;
                        count++;
                    }
                    targetList.add(new Target(x, y, direction, 1));
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }


        for (int j=0;j<targetList.size();j++){
            this.requestedFoods.add(targetList.get(j).getThought().getThoughtID());            
        }

        return targetList;
    }

    public Graphics2D drawCell(Graphics2D g1) {
        for (i = 0; i < targetList.size(); i++) {
            Target tem = targetList.get(i);
            g1.drawImage(tem.imgArr.getImg(), tem.getX(), tem.getY(), null);
        }
        return g1;
    }

    public void drawGuest(int xx, int yy) {
        for (i = 0; i < targetList.size(); i++) {
            Target tem = targetList.get(i);
            if ((tem.getGuest().getXx() == xx) && (tem.getGuest().getYy() == yy)) {
                g.drawImage(tem.getGuest().getImgArr().getImg(), tem.getGuest().getX(), tem.getGuest().getY(), null);
            }
        }
    }

    public void drawThought() {
        for (i = 0; i < targetList.size(); i++) {
            Target tem = targetList.get(i);

            g.drawImage(tem.getThought().imgArr.getImg(), tem.getThought().getX(), tem.getThought().getY(), null);
        }
    }

    public void update(List<Food> foodList, List<Integer> reqFoods,SoundManager soundManager) {
        requestedFoods = reqFoods;
        additional_score = 0;
        additional_scoreUp = new ArrayList();
        serveOK = 0;

        for (i = 0; i < targetList.size(); i++) {
            Target tem = targetList.get(i);
            for (int j = 0; j < foodList.size(); j++) {
                Food tem_food = foodList.get(j);
                if ((tem_food.getXx() == tem.getXx()) && (tem_food.getYy() == tem.getYy()) && (tem_food.getDeathCountDown() == 0)) {
                    if (tem_food.getId() == tem.getThought().getThoughtID()) {
                        tem.nextGuest();
                        tem.nextThought();
                        additional_score = tem_food.getScore();
                        additional_scoreUp.add(new ScoreUp(tem.getXx(), tem.getYy(), tem_food.getScore()));
                        requestedFoods.add(tem.getThought().getThoughtID());
                        serveOK++;
                        soundManager.rightServe(); 
                    } else {
                        additional_score -= (int) ((float) tem_food.getScore() / (float) 3);
                        additional_scoreUp.add(new ScoreUp(tem.getXx(), tem.getYy(), (int) ((float) tem_food.getScore() / (float) 3 * (-1))));
                        requestedFoods.add(tem_food.getId());
                        soundManager.wrongServe();
                    }
                }
            }
        }
    }

    public List<Integer> getRequestedFoods() {
        return requestedFoods;
    }

    public int getServeOK() {
        return serveOK;
    }

    public long getAdditional_score() {
        return additional_score;
    }

    public List<ScoreUp> addAdditional_scoreUp(List<ScoreUp> baseAdditional_scoreUp) {
        for (int j = 0; j < additional_scoreUp.size(); j++) {
            baseAdditional_scoreUp.add(additional_scoreUp.get(j));
        }
        return baseAdditional_scoreUp;
    }
}
