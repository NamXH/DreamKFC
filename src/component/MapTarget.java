package component;


public class MapTarget extends PlayGroundComponent {

    public MapTarget(int xx, int yy, int direction) {
        this.xx = xx;
        this.yy = yy;
        this.direction = direction;

        this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/map/target/target.png");

        Converter converter = new Converter(this.xx, this.yy);
        this.x = converter.getX() - 6;
        this.y = converter.getY() + 6;
        this.zIndex = yy - xx;
    }
}
