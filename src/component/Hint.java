package component;


import java.awt.image.BufferedImage;

public class Hint extends PlayGroundComponent{
    public Hint(int xx,int yy,BufferedImage img){
          this.imgArr.addImgLoaded(img);
          this.xx=xx;
          this.yy=yy;
          Converter converter=new Converter(xx,yy);
          this.x=converter.getX()-6;
          this.y=converter.getY()+5;
    }
    public BufferedImage getImg(){
        return this.imgArr.getImg();
    }

}
