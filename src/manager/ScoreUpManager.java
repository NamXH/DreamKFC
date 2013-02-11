package manager;

import component.ScoreUp;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;

public class ScoreUpManager {
    private Graphics2D g;
    private List<ScoreUp> scoreUpList;
    private int i;
    

    public ScoreUpManager(Graphics2D g){        
        scoreUpList=new ArrayList();
        this.g=g;
        g.drawString("", 0, 0); // Tranh bi delay init cua drawString =.='
    }
    public void add(ScoreUp scoreUp){
        scoreUpList.add(scoreUp);
    }
    public void update(){
        for (i=0;i<scoreUpList.size();i++){
            ScoreUp tem=scoreUpList.get(i);
            if (tem.getCountDown()==0){
                scoreUpList.remove(i);
                i--;
            } else {
                tem.decreaseCountDown();
                tem.flyHigher();
            }
        }
    }
    public void draw(){
       for (i=0;i<scoreUpList.size();i++){
            ScoreUp tem=scoreUpList.get(i);
            g.setColor(tem.getcolor());
            g.setFont(tem.getfont());
            g.drawString(tem.getScoreText(), tem.getX(), tem.getY());            
       }
    }
    
}
