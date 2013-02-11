package manager;


import component.Arrow;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ArrowManager {

    private List<Arrow> arrowList;
    private int i;
    private Graphics2D g;
    private int thisMapID, thisMapType;

    public ArrowManager(Graphics2D g, int anID, int mapType) {
        arrowList = new ArrayList();
        this.g = g;
        thisMapID = anID;
        thisMapType = mapType;
    }

    public List<Arrow> loadDB() {      
        int start, end;
        char c;
        String sub;
        int count;
        int x, y, direction;
        boolean[] lock = {true, true, true, true};

        File f;

        if (thisMapType == 1) {
            f = new File("Data/campaign/campaign" + thisMapID + ".map");
        } else {
            f = new File("Data/custom/custom" + thisMapID + ".map");            
        }

        FileReader fr = null;
        try {
            fr = new FileReader(f);
            BufferedReader bf = new BufferedReader(fr);
            String s = null;
            while ((s = bf.readLine()) != null) {
                if ((s.charAt(0) != '#') && (s.contains("A"))) {
                    start = 0;
                    end = 0;
                    count = 1;
                    x = 0;
                    y = 0;
                    direction = 0;
                    for (int k = 0; k <= 3; k++) {
                        lock[k] = true;
                    }
                    while (end < s.length()) {
                        c = s.charAt(end);
                        while ((c != ' ') && (end < s.length() - 1)) {
                            end++;
                            c = s.charAt(end);
                        }

                        if (start == end) {
                            sub = s.substring(start, end + 1);
                        } else {
                            sub = s.substring(start, end);
                        }

//                        System.out.println(sub);
                        switch (count) {
                            case 1: {
                                x = Integer.parseInt(sub);
                                break;
                            }
                            case 2: {
                                y = Integer.parseInt(sub);
                                break;
                            }
                            case 3: {
                                break;
                            }
                            default: {
                                if (direction == 0) {
                                    direction = Integer.parseInt(sub);
                                }
//                                System.out.println("chieu: " + Integer.parseInt(sub));
                                lock[Integer.parseInt(sub) - 1] = false;
                            }
                        }
                        end++;
                        start = end;
                        count++;
                    }
//                    System.out.println("lockArr: " +lock[0] + " " + lock[1] + " " + lock[2] + " " + lock[3]);
                    arrowList.add(new Arrow(x, y, direction, lock));
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }

        return arrowList;
    }

    public void draw() {
        for (i = 0; i < arrowList.size(); i++) {
            Arrow tem = arrowList.get(i);
            g.drawImage(tem.imgArr.getImg(), tem.getX(), tem.getY(), null);
        }
    }

    public void next(int arrowId) {
        int currentDir = this.arrowList.get(arrowId).getDirection();
        boolean[] boolean_arr;
        boolean ok;

        do {
            ok = true;
            if (currentDir < 4) {
                currentDir++;
            } else {
                currentDir = 1;
            }

            this.arrowList.get(arrowId).imgArr.setCurrentIndex(currentDir);
            //currentDir=this.arrowList.get(arrowId).imgArr.getCurrentIndex();
            boolean_arr = this.arrowList.get(arrowId).getLockArr();
            if (boolean_arr[currentDir - 1]) {
                ok = false;
            }

        } while (!ok);

        this.arrowList.get(arrowId).setDirection(currentDir);
    }
}
