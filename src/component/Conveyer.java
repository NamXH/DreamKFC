package component;

public class Conveyer extends PlayGroundComponent{    
    // CONSTRUCTORs
    public Conveyer(int xx,int yy,int direction,int status){
            this.xx = xx;
            this.yy = yy;
            this.direction = direction;
                        
            this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/conveyer/"+status+"/" + direction + ".png");
            //this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/conveyer/2/" + direction + ".png");
            
            Converter converter = new Converter(this.xx, this.yy);
            this.x=converter.getX()-6;
            this.y=converter.getY()+6;
            this.zIndex=yy-xx;                    
    }    
}
