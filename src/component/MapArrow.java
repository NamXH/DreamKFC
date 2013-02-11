package component;


public class MapArrow extends PlayGroundComponent {

    public boolean[] lockArr;

    public MapArrow(int xx, int yy, int direction, boolean[] lockArr) { // lockArr la` 1 mang boolean 4 phan tu, ung voi 4 huong 1->4, true la lock
        this.xx = xx;
        this.yy = yy;
        this.direction = direction;
        int tem;

        for (tem = 1; tem <= 5; tem++) {
            this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/map/arrow/maparrow" + tem + ".png");
        }

        this.imgArr.setCurrentIndex(direction);

        Converter converter = new Converter(this.xx, this.yy);
        this.x = converter.getX() - 6;
        this.y = converter.getY() + 6;
        this.zIndex = yy - xx;

        boolean[] tem2 = new boolean[]{true, true, true, true};
        for (int k = 0; k < tem2.length; k++) {
            tem2[k] = lockArr[k];
        }

        this.lockArr = tem2;
    }

    public boolean[] getLockArr() {
        return lockArr;
    }
}
