package manager;


import component.Explode;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;

public class ExplodeManager {

    private Graphics2D g;
    private List<Explode> explodeList;
    private int i;

    public ExplodeManager(Graphics2D g) {
        explodeList = new ArrayList();
        this.g = g;
    }

    public void add(Explode explode) {
        explodeList.add(explode);
    }

    public void update() {
        for (i = 0; i < explodeList.size(); i++) {
            Explode tem = explodeList.get(i);

            if (tem.getCountDown() > 0) {
                tem.decreaseCountDown();

                if ((tem.getCountDown() >= 16) && (tem.getCountDown() < 20)) {
                    tem.imgArr.setCurrentIndex(1);
                } else if ((tem.getCountDown() >= 12) && (tem.getCountDown() < 16)) {
                    tem.imgArr.setCurrentIndex(2);
                } else if ((tem.getCountDown() >= 8) && (tem.getCountDown() < 12)) {
                    tem.imgArr.setCurrentIndex(3);
                } else if ((tem.getCountDown() >= 4) && (tem.getCountDown() < 8)) {
                    tem.imgArr.setCurrentIndex(4);                
                } else {
                    tem.imgArr.setCurrentIndex(5);
                }
            } else {
                explodeList.remove(i);
            }

        }
    }

    public void draw() {
        for (i = 0; i < explodeList.size(); i++) {
            Explode tem = explodeList.get(i);
            g.drawImage(tem.imgArr.getImg(), tem.getX(), tem.getY(), null);
        }
    }
}
