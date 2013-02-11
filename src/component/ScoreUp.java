package component;

import java.awt.Color;
import java.awt.Font;

public class ScoreUp extends PlayGroundComponent{
    private long score;
    private String scoreText;
    private Color color;
    private Font font;
    private int fontSize;
    private int countDown;

    public ScoreUp(int xx,int yy,long score){
         this.score=score;
            if (this.score>=0) {
                this.color=Color.GREEN;
                this.scoreText="+"+score;
            } else {
                this.color=Color.RED;
                this.scoreText=""+score;
            }


            this.fontSize=20;
            this.font=new Font("Arial", Font.PLAIN, this.fontSize);


            this.countDown=40;
            this.xx = xx;
            this.yy = yy;

            Converter converter = new Converter(this.xx, this.yy);
            this.x=converter.getX()-15;
            this.y=converter.getY()-15;
            this.zIndex=yy-xx;
    }

    // GETTERs
    public Color getcolor(){
        return color;
    }
    public Font getfont(){
        return font;
    }
    public String getScoreText(){
        return scoreText;
    }
    public int getCountDown(){
        return countDown;
    }


    // SETTERs
    public void decreaseCountDown(){
        this.countDown--;
    }
    public void flyHigher(){
        //this.setX(this.getX());
        this.setY(this.getY()-1);
    }

}
