package component;


public class Converter {

    private int magicx, magicy;

    public Converter(int magicx, int magicy) {
        this.magicx = magicx;
        this.magicy = magicy;
    }

    public int getX() {
        if ((this.magicx > 20) && (this.magicy > 20)) {
            return this.magicx;
        } else {
            int basex = 4;
            int j, temx = basex;
            for (j = 1; j < magicx; j++) {
                temx += 20;
            }
            for (j = 1; j < magicy; j++) {
                temx += 20;
            }
            return (int) temx;
        }
    }

    public int getY() {
        if ((this.magicx > 20) && (this.magicy > 20)) {
            return this.magicy;
        } else {
            int basex = 4;
            int j, temx = basex;
            for (j = 1; j < magicx; j++) {
                temx += 20;
            }
            for (j = 1; j < magicy; j++) {
                temx += 20;
            }
            return ((int) (0.72 * temx + 290.5 - 28.8 * (magicx - 1) - 20));

        }
    }

    public void setMagicX(int val) {
        magicx = val;
    }

    public void setMagicY(int val) {
        magicy = val;
    }

}