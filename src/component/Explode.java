package component;


public class Explode extends PlayGroundComponent {

    private int countDown;
    

    public Explode(int xx, int yy) {
        this.countDown = 20;
        this.xx = xx;
        this.yy = yy;        

        //this.img = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/explode"+(int)(6-this.countdown/12)+".png"));
        int tem;
        for (tem = 1; tem <= 6; tem++) {
            this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/explode/explode" + tem + ".png");
        }

        Converter converter = new Converter(this.xx, this.yy);
        this.x = converter.getX() - 25;
        this.y = converter.getY() - 15;
        this.zIndex = yy - xx;
    }

    public int getCountDown() {
        return countDown;
    }

    public void decreaseCountDown() {
        this.countDown--;
    }
}
