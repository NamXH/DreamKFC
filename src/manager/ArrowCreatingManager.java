package manager;


import component.MapArrow;
import component.Arrow;
import component.Conveyer;
import gamecore.Screen;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ArrowCreatingManager {

    private List<MapArrow> arrowList;
    private Screen screen;
    private Graphics2D g, tmpg;

    public ArrowCreatingManager(Screen scr, Graphics2D fg) {
        arrowList = new ArrayList<MapArrow>();
        screen = scr;
        g = screen.getGraphics();
        tmpg = fg;
    }

    public List<MapArrow> getList() {
        return arrowList;
    }

    public void setList(List<MapArrow> newList) {
        arrowList = newList;
    }

    public void add(MapArrow newArrow) {
        MapArrow tmp;

        // kiem tra xem co trung arrow nao chua
        boolean overlap = false;
        for (int i = 0; i < arrowList.size(); i++) {
            tmp = arrowList.get(i);
            if ((newArrow.getXx() == tmp.getXx()) && (newArrow.getYy() == tmp.getYy())) {
                tmp.lockArr[0] = newArrow.lockArr[0] && tmp.lockArr[0];
                tmp.lockArr[1] = newArrow.lockArr[1] && tmp.lockArr[1];
                tmp.lockArr[2] = newArrow.lockArr[2] && tmp.lockArr[2];
                tmp.lockArr[3] = newArrow.lockArr[3] && tmp.lockArr[3];
                if (newArrow.getDirection() != 5) {
                    tmp.setDirection(newArrow.getDirection());
                }
                overlap = true;
                break;
            }
        }

        if (!overlap) {
            arrowList.add(newArrow);
        }
    }

    public boolean onArrow(int xx, int yy) {
        boolean result = false;
        MapArrow tmp;
        for (int i = 0; i < arrowList.size(); i++) {
            tmp = arrowList.get(i);
            if ((xx == tmp.getXx()) && (yy == tmp.getYy())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean noNullArrow() {
        boolean result = true;
        MapArrow tmp;
        for (int i = 0; i < arrowList.size(); i++) {
            tmp = arrowList.get(i);
            if (tmp.getDirection() == 5) {
                result = false;
                break;
            }
        }
        return result;
    }

    public void fillBitmap(int[][] bitmap) {
        MapArrow tmp;
        for (int i = 0; i < arrowList.size(); i++) {
            tmp = arrowList.get(i);
            bitmap[tmp.getXx()][tmp.getYy()] = 2;
        }
    }

    public void generateDb(int[][] bitmap, List<Conveyer> conveyerDb, List<Arrow> arrowDb, int mapNumber) {
        File f = new File("Data/custom/custom" + mapNumber + ".map");
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            fw = new FileWriter(f, true);
            pw = new PrintWriter(fw);
            /////////////////////////////////
            MapArrow tmp;
            for (int i = 0; i < arrowList.size(); i++) {
                tmp = arrowList.get(i);
                int count = 0;
                for (int j = 0; j <= 3; j++) {
                    if (!tmp.lockArr[j]) {
                        count++;
                    }
                }
                if (count > 1) {
                    pw.print(tmp.getXx() + " " + tmp.getYy() + " " + "A" + " ");
                    for (int j = 0; j <= 3; j++) {
                        if (!tmp.lockArr[j]) {
                            pw.print((j + 1) + " ");
                        }
                    }
                    pw.println();
                    Arrow t = new Arrow(tmp.getXx(), tmp.getYy(), tmp.getDirection(), tmp.getLockArr());
                    arrowDb.add(t);
                    pw.println(tmp.getXx() + " " + tmp.getYy() + " " + "C" + " " + tmp.getDirection());
                } else {
                    pw.println(tmp.getXx() + " " + tmp.getYy() + " " + "C" + " " + tmp.getDirection());
                    Conveyer t = new Conveyer(tmp.getXx(), tmp.getYy(), tmp.getDirection(), 2);
                    conveyerDb.add(t);
                }
            }
            /////////////////////////////////
            pw.close();
        } catch (Exception ex) {
            System.out.println("error");
        }
    }
/*
    public void drawFinish() {
        MapArrow tmp;
        List<Arrow> arrowFinishList = new ArrayList<Arrow>();
        for (int i = 0; i < arrowList.size(); i++) {
            tmp = arrowList.get(i);
            Arrow a = new Arrow(tmp.getXx(), tmp.getYy(), tmp.getDirection(), tmp.getLockArr());
            arrowFinishList.add(a);
        }
        Arrow tem;
        for (int k = -19; k <= 19; k++) {
            for (int j = 0; j < arrowFinishList.size(); j++) {
                tem = arrowFinishList.get(j);
                if (tem.getZindex() == k) {
                    tmpg.drawImage(tem.imgArr.getImg(), tem.getX(), tem.getY(), null);
                }
            }
        }
    }
 * */

    public void drawAll() {
        MapArrow tmp;
        for (int i = 0; i < arrowList.size(); i++) {
            tmp = arrowList.get(i);
//            g.drawImage(tmp.imgArr.getImg(tmp.getDirection()), tmp.getX(), tmp.getY(), null);
            tmpg.drawImage(tmp.imgArr.getImg(tmp.getDirection()), tmp.getX(), tmp.getY(), null);
        }
    }
}
