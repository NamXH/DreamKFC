package manager;


import component.Target;
import component.MapTarget;
import component.Conveyer;
import gamecore.Screen;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TargetCreatingManager {

    private List<MapTarget> targetList;
    private Screen screen;
    private Graphics2D g, tmpg;

    public TargetCreatingManager(Screen scr, Graphics2D fg) {
        targetList = new ArrayList<MapTarget>();
        screen = scr;
        g = screen.getGraphics();
        tmpg = fg;
    }

    public List<MapTarget> getList() {
        return targetList;
    }

    public void add(int xx, int yy, int direction) {
        MapTarget newTarget = new MapTarget(xx, yy, direction);
        targetList.add(newTarget);
//        tmpg.drawImage(newTarget.imgArr.getImg(), newTarget.getX(), newTarget.getY(), null);
    }

    public void generateDb(int[][] bitmap, List<Conveyer> conveyerDb, List<Target> targetDb, int mapNumber) {
        File f = new File("Data/custom/custom" + mapNumber + ".map");
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            fw = new FileWriter(f, true);
            pw = new PrintWriter(fw);
            /////////////////////////////////
            MapTarget tmp;
            for (int i = 0; i < targetList.size(); i++) {
                tmp = targetList.get(i);
                if (bitmap[tmp.getXx()][tmp.getYy()] == 4) {
                    pw.println(tmp.getXx() + " " + tmp.getYy() + " " + "T" + " " + tmp.getDirection());
                    Target t = new Target(tmp.getXx(), tmp.getYy(), tmp.getDirection(), 1);
                    targetDb.add(t);
                    pw.println(tmp.getXx() + " " + tmp.getYy() + " " + "C" + " " + tmp.getDirection());
                } else {
                    switch (tmp.getDirection()) {
                        case 1: {
                            pw.println(tmp.getXx() + " " + tmp.getYy() + " " + "C" + " " + 1);
                            Conveyer t = new Conveyer(tmp.getXx(), tmp.getYy(), 1, 2);
                            conveyerDb.add(t);
                            break;
                        }
                        case 2: {
                            pw.println(tmp.getXx() + " " + tmp.getYy() + " " + "C" + " " + 2);
                            Conveyer t = new Conveyer(tmp.getXx(), tmp.getYy(), 2, 2);
                            conveyerDb.add(t);                            
                            break;
                        }
                        case 3: {
                            pw.println(tmp.getXx() + " " + tmp.getYy() + " " + "C" + " " + 3);
                            Conveyer t = new Conveyer(tmp.getXx(), tmp.getYy(), 3, 2);
                            conveyerDb.add(t);
                            break;
                        }
                        case 4: {
                            pw.println(tmp.getXx() + " " + tmp.getYy() + " " + "C" + " " + 4);
                            Conveyer t = new Conveyer(tmp.getXx(), tmp.getYy(), 4, 2);
                            conveyerDb.add(t);
                            break;
                        }
                    }
                }
            }
            /////////////////////////////////
            pw.close();
        } catch (Exception ex) {
            System.out.println("error");
        }
    }
}
