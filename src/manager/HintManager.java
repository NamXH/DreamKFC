package manager;


import component.Target;
import component.Hint;
import component.Arrow;
import component.Conveyer;
import component.Food;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class HintManager {

    private boolean available;
    private List<int[]> queue = new ArrayList(); // chi so cuoi =0: truy xuat den parents' x, =1 truy xuat den parents' y
    private int[][][] allCell;
    private List<int[]> targetArr = new ArrayList();
    private int i, j;
    private boolean success;
    private List<int[]> hintArray = new ArrayList();
    private List<Conveyer> conveyerList = new ArrayList();
    private List<Arrow> arrowList = new ArrayList();
    private List<Target> targetList = new ArrayList();
    private List<Hint> hintList = new ArrayList();
    private int maxcountDown = 40;
    private int countDown = 0;
    private Graphics2D g;
    private BufferedImage singleHintImg, totalHintImg, blankBG, noHintImg;

    public HintManager(Graphics2D g) {
        this.g = g;

        try {
            noHintImg = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/noHint.png"));
            singleHintImg = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/hintConveyer.png"));
        } catch (IOException ex) {
            Logger.getLogger(HintManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        available = true;
    }

    public void active(int foodIndex, List<Food> foodList, List<Conveyer> conveyerList, List<Arrow> arrowList, List<Target> targetList) {
        if (foodList.size() == 0) {
            return;
        }

        this.countDown = maxcountDown;
        this.allCell = new int[21][21][4];
        this.success = false;
        //this.foodList = foodList;
        this.conveyerList = conveyerList;
        this.arrowList = arrowList;
        this.targetList = targetList;

        this.hintArray.clear();
        this.available = false;
        this.targetArr.clear();
        this.queue.clear();

        for (i = 0; i < 21; i++) {
            for (j = 0; j < 21; j++) {
                allCell[i][j][0] = i;
                allCell[i][j][1] = j;
                allCell[i][j][2] = 0;
                allCell[i][j][3] = 0;
            }
        }

        int dem = foodIndex;
        while (true) {
            // XAC DINH THUC AN TON TAI LAU NHAT TREN BAN
            if (dem>=foodList.size()) break;
            int foodXx = foodList.get(dem).getXx();
            int foodYy = foodList.get(dem).getYy();
            int foodID = foodList.get(dem).getId();

            // NEU THUC AN LAU NHAT DANG O TARGET THI CHUYEN SANG SET TIEP THANG TIEP THEO
            for (int k = 0; k < targetList.size(); k++) {
                if ((targetList.get(k).getXx() == foodXx) && (targetList.get(k).getYy() == foodYy)) {
                    dem++;
                    continue;
                }
            }

            // XAC DINH TARGET CO ID=ID CUA THUC AN TREN (cung loai thuc an)
            for (i = 0; i < targetList.size(); i++) {
                Target tem = targetList.get(i);
                if (tem.getThought().getThoughtID() == foodID) {
                    targetArr.add(new int[]{tem.getXx(), tem.getYy()});
                }
            }

            allCell[foodXx][foodYy][2] = -1;
            allCell[foodXx][foodYy][3] = -1;
            queue.add(allCell[foodXx][foodYy]);
            break;
        }

        //========================================
        //============ MAIN ALGORITHM ============

        while (!queue.isEmpty()) {
            int[] xCell = queue.get(0); // xCell la cell dang xet (lay phan tu dau tien cua queue)

            if (isValidTarget(xCell[0], xCell[1])) {
                this.success = true;

                int[] addCell = allCell[xCell[0]][xCell[1]];

                while (true) {
                    hintArray.add(new int[]{addCell[0], addCell[1]});
                    if (addCell[2] != -1) {
                        addCell = allCell[addCell[2]][addCell[3]];
                    } else {
                        break;
                    }
                }
                return;
            } else if (isArrow(xCell[0], xCell[1])) {
                Arrow xArrow = getArrowAt(xCell[0], xCell[1]);
                boolean[] lockArr = xArrow.getLockArr();

                // chu y: lockArr[0] thi ung voi direction 1
                if (lockArr[0] == false) {
                    int nextXx = xCell[0] + 1, nextYy = xCell[1];
                    if (allCell[nextXx][nextYy][2] == 0) {
                        queue.add(new int[]{nextXx, nextYy, xCell[0], xCell[1]});
                        allCell[nextXx][nextYy][2] = xCell[0];
                        allCell[nextXx][nextYy][3] = xCell[1];
                    }
                }
                if (lockArr[1] == false) {
                    int nextXx = xCell[0], nextYy = xCell[1] + 1;
                    if (allCell[nextXx][nextYy][2] == 0) {
                        queue.add(new int[]{nextXx, nextYy, xCell[0], xCell[1]});
                        allCell[nextXx][nextYy][2] = xCell[0];
                        allCell[nextXx][nextYy][3] = xCell[1];
                    }
                }
                if (lockArr[2] == false) {
                    int nextXx = xCell[0] - 1, nextYy = xCell[1];
                    if (allCell[nextXx][nextYy][2] == 0) {
                        queue.add(new int[]{nextXx, nextYy, xCell[0], xCell[1]});
                        allCell[nextXx][nextYy][2] = xCell[0];
                        allCell[nextXx][nextYy][3] = xCell[1];
                    }
                }
                if (lockArr[3] == false) {
                    int nextXx = xCell[0], nextYy = xCell[1] - 1;
                    if (allCell[nextXx][nextYy][2] == 0) {
                        queue.add(new int[]{nextXx, nextYy, xCell[0], xCell[1]});
                        allCell[nextXx][nextYy][2] = xCell[0];
                        allCell[nextXx][nextYy][3] = xCell[1];
                    }
                }

            } else if (isConveyer(xCell[0], xCell[1])) {
                Conveyer xConveyer = getConveyerAt(xCell[0], xCell[1]);
                if (xConveyer.getDirection() == 1) {
                    int nextXx = xCell[0] + 1, nextYy = xCell[1];
                    if (allCell[nextXx][nextYy][2] == 0) {
                        queue.add(new int[]{nextXx, nextYy, xCell[0], xCell[1]});
                        allCell[nextXx][nextYy][2] = xCell[0];
                        allCell[nextXx][nextYy][3] = xCell[1];
                    }
                } else if (xConveyer.getDirection() == 2) {
                    int nextXx = xCell[0], nextYy = xCell[1] + 1;
                    if (allCell[nextXx][nextYy][2] == 0) {
                        queue.add(new int[]{nextXx, nextYy, xCell[0], xCell[1]});
                        allCell[nextXx][nextYy][2] = xCell[0];
                        allCell[nextXx][nextYy][3] = xCell[1];
                    }
                } else if (xConveyer.getDirection() == 3) {
                    int nextXx = xCell[0] - 1, nextYy = xCell[1];
                    if (allCell[nextXx][nextYy][2] == 0) {
                        queue.add(new int[]{nextXx, nextYy, xCell[0], xCell[1]});
                        allCell[nextXx][nextYy][2] = xCell[0];
                        allCell[nextXx][nextYy][3] = xCell[1];
                    }
                } else if (xConveyer.getDirection() == 4) {
                    int nextXx = xCell[0], nextYy = xCell[1] - 1;
                    if (allCell[nextXx][nextYy][2] == 0) {
                        queue.add(new int[]{nextXx, nextYy, xCell[0], xCell[1]});
                        allCell[nextXx][nextYy][2] = xCell[0];
                        allCell[nextXx][nextYy][3] = xCell[1];
                    }
                }
            }

            queue.remove(xCell);
        }
        if (foodIndex < foodList.size()) {
            active(foodIndex + 1, foodList, conveyerList, arrowList, targetList);
        }

    }

    public void update() {
        if (countDown > 0) {
            countDown--;
        } else {
            available = true;
        }
    }

    public void initDraw() {
        if (success) {
            hintList.clear();
            for (int k = 0; k < hintArray.size(); k++) {
                hintList.add(new Hint(hintArray.get(k)[0], hintArray.get(k)[1], singleHintImg));
            }
        }
    }

    public void draw() {
        if (countDown > 0) {
            if (success) {
                for (int k = 0; k < hintList.size(); k++) {
                    Hint tem = hintList.get(k);
                    g.drawImage(tem.getImg(), tem.getX(), tem.getY(), null);
                }
            } else {
                g.drawImage(noHintImg, 650, 72, null);
            }
        }
    }

    private Arrow getArrowAt(int Xx, int Yy) {
        for (int k = arrowList.size()-1;k>=0; k--) {
            Arrow tem = arrowList.get(k);
            if ((tem.getXx() == Xx) && (tem.getYy() == Yy)) {
                return tem;
            }
        }
        return null;
    }

    private Conveyer getConveyerAt(int Xx, int Yy) {
        for (int k = conveyerList.size()-1;k>=0; k--) {
            Conveyer tem = conveyerList.get(k);
            if ((tem.getXx() == Xx) && (tem.getYy() == Yy)) {
                return tem;
            }
        }
        return null;
    }

    private boolean isValidTarget(int testX, int testY) {
        for (int k = 0; k < targetArr.size(); k++) {
            if ((targetArr.get(k)[0] == testX) && (targetArr.get(k)[1] == testY)) {
                return true;
            }
        }
        return false;
    }

    private boolean isArrow(int testX, int testY) {
        for (int k = 0; k < arrowList.size(); k++) {
            Arrow tem = arrowList.get(k);
            if ((tem.getXx() == testX) && (tem.getYy() == testY)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConveyer(int testX, int testY) {
        for (int k = 0; k < conveyerList.size(); k++) {
            Conveyer tem = conveyerList.get(k);
            if ((tem.getXx() == testX) && (tem.getYy() == testY)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isSuccess() {
        return success;
    }
}
