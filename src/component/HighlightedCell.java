package component;


public class HighlightedCell extends PlayGroundComponent {
    // CONSTRUCTORs

    private Converter converter;

    public HighlightedCell(int xx, int yy) {
        this.xx = xx;
        this.yy = yy;

        this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/map/highlight.png");

        converter = new Converter(this.xx, this.yy);
        this.x = converter.getX() - 6;
        this.y = converter.getY() + 6;
        this.zIndex = yy - xx;
    }

    @Override
    public void setXx(int xx) {
        this.xx = xx;
        converter.setMagicX(xx);
        this.x = converter.getX() - 6;
        this.y = converter.getY() + 6;
    }

    @Override
    public void setYy(int yy) {
        this.yy = yy;
        converter.setMagicY(yy);
        this.x = converter.getX() - 6;
        this.y = converter.getY() + 6;
    }
}
