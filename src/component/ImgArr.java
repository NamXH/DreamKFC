package component;


import gamecore.CreateMap;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImgArr {
    private int currentIndex; 
    private List<String> imgPathArr;
    private List<BufferedImage> imgArr;
    private float opacity;

    public ImgArr(){
        this.currentIndex=1;
        this.imgPathArr=new ArrayList();
            this.imgPathArr.add("");

        this.imgArr=new ArrayList();
            this.imgArr.add(null);

        this.opacity=(float)1.0;
    }

    //index
    public void setCurrentIndex(int currentIndex){
        this.currentIndex=currentIndex;
    }
    public int getCurrentIndex(){
        return currentIndex;
    }
      
    //opacity
    public void setOpacity(float opacity){
        this.opacity=(float)opacity;
    }
    public float getOpacity(){
        return opacity;
    }
    public void decreaseOpacity(float value){
        this.opacity-=value;
    }

    // ImgPathArr
    public void addImg(String imgpath){
        this.imgPathArr.add(imgpath);
        
        BufferedImage tem;
        try {
            tem = ImageIO.read(new File(imgpath));
            this.imgArr.add(tem);            
        } catch (IOException ex) {
            Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void addImgLoaded(BufferedImage loadedImg){
        this.imgArr.add(loadedImg);
    }
    /*
    public void clearImgArr(){
        this.imgPathArr=new ArrayList();
    }
     */

    ////////////////////////////////////////////////////////////////////////////////
    /////////////////// !IMPORTANT /////////////////////////////////////////////////
    public String getImgPath(){
        return this.imgPathArr.get(currentIndex);
    }
    public String getImgPath(int index){
        return this.imgPathArr.get(index);
    }
    public BufferedImage getImg(){
        return changeOpacity(this.imgArr.get(currentIndex),opacity);
    }

    public BufferedImage getImg(int index){
        BufferedImage tem;
        try {
            tem = ImageIO.read(new File(this.imgPathArr.get(index)));
            return changeOpacity(tem,opacity);
        } catch (IOException ex) {
            Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    

    public void next(){
        if (this.imgArr.size()<3) return;
        if (this.currentIndex<this.imgPathArr.size()-1) this.currentIndex++;
        else this.currentIndex=1;
    }
   

    ///// MAKE TRANSPARENT
    private BufferedImage changeOpacity(BufferedImage src, float alpha) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        int rule = AlphaComposite.SRC_OVER;
        AlphaComposite ac = AlphaComposite.getInstance(rule, alpha);
        g2.setComposite(ac);
        g2.drawImage(src, null, 0, 0);
        g2.dispose();
        return dest;
    }
}

