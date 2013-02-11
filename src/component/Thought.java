package component;


import java.awt.image.BufferedImage;
import java.util.Random;

public class Thought extends PlayGroundComponent{
    private int thoughtID;
    private String[] thought_arr;

    public Thought(String[] thought_arr){
        this.thought_arr=thought_arr;
        
        for (int tem=1;tem<this.thought_arr.length;tem++){
                this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/thought/"+thought_arr[tem]+".png");
        }

        next();
    }
    public void next(){
        Random random=new Random();
        int randomThought=random.nextInt(thought_arr.length-1)+1;
        thoughtID=randomThought;
        this.imgArr.setCurrentIndex(thoughtID);
    }
    
    public int getThoughtID(){
        return thoughtID;
    }

    public ImgArr getImgArr(){
        return this.imgArr;
    }
    public BufferedImage getImage(){
        return this.imgArr.getImg();
    }
}
