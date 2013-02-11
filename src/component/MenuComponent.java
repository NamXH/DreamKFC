package component;


import java.awt.Image;

public class MenuComponent extends WindowComponent {
    protected int info;
    private float dx;
    private float dy;
    protected Image img;
    
    public MenuComponent() {
        this.info = -1;
        this.x = 0;
        this.y = 0;
        
    }

    public void setInfo(int i) {
        this.info = i;
    }

    public int getInfo() {
        return this.info;
    }

    public void setImage(Image anImage) {
        this.img = anImage;
    }

    public Image getImage() {
        return img;
    }

    public void setVelocityX(float dx) {
        this.dx = dx;
    }

    public void setVelocityY(float dy) {
        this.dy = dy;
    }

    public void update(long elapsedTime) {
        this.x = this.x + (int) (this.dx * elapsedTime);
        this.y = this.y + (int) (this.dy * elapsedTime);
    }

    public int getWidth() {
        return img.getWidth(null);
    }

    public int getHeight() {
        return img.getHeight(null);
    }

    public boolean isClicked (int x1, int y1){
        if ((x1 >= x) && (x1 <= (x + this.getWidth()))
                && (y1 >= y) && (y1 <= (y + this.getHeight()))) {
            return true;
        }
        else return false;
    }
}
