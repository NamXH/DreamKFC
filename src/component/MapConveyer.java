package component;

public class MapConveyer extends PlayGroundComponent{
    // CONSTRUCTORs
    public MapConveyer(int xx,int yy,int direction,int status){
            this.xx = xx;
            this.yy = yy;
            this.direction = direction;

            this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/map/conveyer/"+ direction + ".png");
            //this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/conveyer/2/" + direction + ".png");

            Converter converter = new Converter(this.xx, this.yy);
            this.x=converter.getX()-6;
            this.y=converter.getY()+6;
            this.zIndex=yy-xx;
    }
}
