package manager;


import component.ConveyerBelt;
import component.MapArrow;
import component.HighlightedCell;
import component.MapConveyer;
import component.Conveyer;
import gamecore.Screen;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ConveyerBeltCreatingManager {

    private List<ConveyerBelt> beltList;
    private Screen screen;
    private Graphics2D g, tmpg;
    private int[] targetableBlock3x = {5, 4, 3, 2, 1, 2, 2, 2, 2, 2, 5, 4, 3, 2, 1};
    private int[] targetableBlock3y = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 19, 18, 17, 16};
    private int[] targetableBlock4x = {1, 2, 3, 4, 5, 20, 19, 18, 17, 16, 15, 14};
    private int[] targetableBlock4y = {5, 4, 3, 2, 1, 7, 6, 5, 4, 3, 2, 1};
    private int[] targetableBlock1x = {15, 16, 17, 18, 19, 20, 16, 17, 18, 19, 20};
    private int[] targetableBlock1y = {1, 2, 3, 4, 5, 6, 20, 19, 18, 17, 16};
    private int[] targetableBlock2x = {20, 19, 18, 17, 16, 15, 14, 13, 12, 1, 2, 3, 4, 5};
    private int[] targetableBlock2y = {16, 17, 18, 19, 19, 19, 19, 19, 20, 16, 17, 18, 19, 20};

    public ConveyerBeltCreatingManager(Screen scr, Graphics2D fg) {
        beltList = new ArrayList<ConveyerBelt>();
        screen = scr;
        g = screen.getGraphics();
        tmpg = fg;

    }

    public void add(ConveyerBelt newBelt, ArrowCreatingManager arrowCreator) {
        boolean legal = true;
        legal = legalCheck(newBelt);
        if (legal) {
            // thu tu nay la bat buoc
            intersectCheck(newBelt, arrowCreator);
            unifiedCheck(newBelt);
        }
    }

//   System.out.println(tmp.getXx1() + " " + tmp.getYy1() + " " + tmp.getXx2() + " " + tmp.getYy2());
    public void del(int delXx, int delYy, ArrowCreatingManager arrowCreator) {
        ConveyerBelt tmp;

        for (int i = 0; i < beltList.size(); i++) {
            tmp = beltList.get(i);

            // @ dieu kien kiem tra 1 cell co thuoc belt nao do ko
            if (((delXx == tmp.getXx1()) && (delXx == tmp.getXx2()) && ((delYy - tmp.getYy1()) * (delYy - tmp.getYy2()) <= 0)) || ((delYy == tmp.getYy1()) && (delYy == tmp.getYy2()) && ((delXx - tmp.getXx1()) * (delXx - tmp.getXx2()) <= 0))) {

                // kiem tra de xoa cac arrow ko can thiet
                List<MapArrow> arrowList = arrowCreator.getList();
                MapArrow a;
                int count = 0; // bien dem so chieu ko bi khoa
                for (int j = 0; j < arrowList.size(); j++) {
                    a = arrowList.get(j);
                    // neu arrow thuoc belt
                    if (((a.getXx() == tmp.getXx1()) && (a.getXx() == tmp.getXx2()) && ((a.getYy() - tmp.getYy1()) * (a.getYy() - tmp.getYy2()) <= 0)) || ((a.getYy() == tmp.getYy1()) && (a.getYy() == tmp.getYy2()) && ((a.getXx() - tmp.getXx1()) * (a.getXx() - tmp.getXx2()) <= 0))) {

                        count = 0;
                        for (int k = 0; k < a.lockArr.length; k++) {
                            if (!a.lockArr[k]) {
                                count++;
                            }
                        }

                        // neu arrow o end
                        if ((a.getXx() == tmp.getXx2()) && (a.getYy() == tmp.getYy2())) {
                            switch (count) {
                                case 0: {
                                    // dem so belt co end nam o arrow nay
                                    int converge = 0;
                                    ConveyerBelt tmp2;
                                    for (int k = 0; k < beltList.size(); k++) {
                                        tmp2 = beltList.get(k);
                                        if ((tmp2.getXx2() == a.getXx()) && (tmp2.getYy2() == a.getYy())) {
                                            converge++;
                                        }
                                    }
                                    if (converge < 3) {
                                        arrowList.remove(j);
                                        j--;
                                    }
                                    break;
                                }
                                case 1: {
                                    // dem so belt co end nam o arrow nay
                                    int converge = 0;
                                    ConveyerBelt tmp2;
                                    for (int k = 0; k < beltList.size(); k++) {
                                        tmp2 = beltList.get(k);
                                        if ((tmp2.getXx2() == a.getXx()) && (tmp2.getYy2() == a.getYy())) {
                                            converge++;
                                        }
                                    }
                                    if (converge < 2) {
                                        arrowList.remove(j);
                                        j--;
                                    }
                                    break;
                                }
                                default:
                                    break;

                            }
                        } else {
                            a.lockArr[tmp.getDirection() - 1] = true;
                            a.setDirection(5);
                            for (int k = 0; k <= 3; k++) {
                                if (!a.lockArr[k]) {
                                    a.setDirection(k + 1);
                                    break;
                                }
                            }

                            switch (count) {
                                case 1: {
                                    // dem so belt co end nam o arrow nay
                                    int converge = 0;
                                    ConveyerBelt tmp2;
                                    for (int k = 0; k < beltList.size(); k++) {
                                        tmp2 = beltList.get(k);
                                        if ((tmp2.getXx2() == a.getXx()) && (tmp2.getYy2() == a.getYy())) {
                                            converge++;
                                        }
                                    }
                                    if (converge < 2) {
                                        arrowList.remove(j);
                                        j--;
                                    }
                                    break;
                                }
                                case 2: {
                                    // dem so belt co end nam o arrow nay
                                    int converge = 0;
                                    ConveyerBelt tmp2;
                                    for (int k = 0; k < beltList.size(); k++) {
                                        tmp2 = beltList.get(k);
                                        if ((tmp2.getXx2() == a.getXx()) && (tmp2.getYy2() == a.getYy())) {
                                            converge++;
                                        }
                                    }
                                    if (converge < 1) {
                                        arrowList.remove(j);
                                        j--;
                                    }
                                    break;
                                }
                                default:
                                    break;

                            }
                        }

                    } // end of dk xet arrow co thuoc belt
                } // end of vong for xet arrow
                beltList.remove(i);
                break;
            } // end of dk if delxx, delyy thuoc belt
        } // end of vong for xet belt
    }

    // check xem co legal ko
    private boolean legalCheck(ConveyerBelt newBelt) {
        ConveyerBelt tmp;
        boolean result = true;
        for (int i = 0; i < beltList.size(); i++) {
            tmp = beltList.get(i);
            if (newBelt.overlapOppositeDirection(tmp) == 3) {
                result = false;
                break;
            }
        }
        return result;
    }

    // check cac truong hop giao nhau de add arrow neu co
    private void intersectCheck(ConveyerBelt newBelt, ArrowCreatingManager arrowCreator) {
        ConveyerBelt tmp;
        for (int i = 0; i < beltList.size(); i++) {
            tmp = beltList.get(i);
            if (newBelt.cross(tmp)) {
                int crossXx, crossYy, newDirec;
                crossXx = newBelt.crossXx(tmp);
                crossYy = newBelt.crossYy(tmp);
                newDirec = newBelt.getDirection();

                boolean[] lockArr = {true, true, true, true};
                lockArr[tmp.getDirection() - 1] = false;
                lockArr[newBelt.getDirection() - 1] = false;

                // belt nao cross o dau thi khoa lai
                if ((crossXx == newBelt.getXx2()) && (crossYy == newBelt.getYy2())) {
                    lockArr[newBelt.getDirection() - 1] = true;
                    newDirec = tmp.getDirection();
                }
                if ((crossXx == tmp.getXx2()) && (crossYy == tmp.getYy2())) {
                    lockArr[tmp.getDirection() - 1] = true;
                    if (lockArr[newBelt.getDirection() - 1]) {
                        newDirec = 5;
                    }
                }
                MapArrow a = new MapArrow(crossXx, crossYy, newDirec, lockArr);
                arrowCreator.add(a);

            } else {
                if (newBelt.overlapOppositeDirection(tmp) == 1) {
                    /* @ : them arrow vao
                    newBelt.overlapOppositeDirectionStartXx(tmp);
                    newBelt.overlapOppositeDirectionStartYy(tmp); */

                    boolean[] lockArr = {true, true, true, true};
                    lockArr[tmp.getDirection() - 1] = false;
                    lockArr[newBelt.getDirection() - 1] = false;
                    MapArrow a = new MapArrow(newBelt.overlapOppositeDirectionStartXx(tmp), newBelt.overlapOppositeDirectionStartYy(tmp), newBelt.getDirection(), lockArr);
                    arrowCreator.add(a);

                } else {
                    if (newBelt.overlapOppositeDirection(tmp) == 2) {
                        /* @ : them arrow vao 
                        newBelt.overlapOppositeDirectionEndXx(tmp);
                        newBelt.overlapOppositeDirectionEndYy(tmp); */

                        boolean[] lockArr = {true, true, true, true};
                        MapArrow a = new MapArrow(newBelt.overlapOppositeDirectionEndXx(tmp), newBelt.overlapOppositeDirectionEndYy(tmp), 5, lockArr);
                        arrowCreator.add(a);

                    }
                }
            }
        }
    }

    // check cac truong hop can phai hop nhat
    private void unifiedCheck(ConveyerBelt newBelt) {
        ConveyerBelt tmp, unifiedBelt;
        int i = 0;
        unifiedBelt = newBelt;
        for (i = 0; i < beltList.size(); i++) {
            tmp = beltList.get(i);
            if (unifiedBelt.overlapSameDirection(tmp)) {
                /* xoa belt cu, gan' unifiedBelt moi*/
                int xx1, yy1, xx2, yy2;
                xx1 = unifiedBelt.overlapSameDirectionXx1(tmp);
                yy1 = unifiedBelt.overlapSameDirectionYy1(tmp);
                xx2 = unifiedBelt.overlapSameDirectionXx2(tmp);
                yy2 = unifiedBelt.overlapSameDirectionYy2(tmp);
                ConveyerBelt a = new ConveyerBelt(xx1, yy1, xx2, yy2);

                unifiedBelt = a;
                beltList.remove(i);
            }
        }
        /* , them belt moi tai
        newBelt.overlapSameDirectionXx1(tmp);
        newBelt.overlapSameDirectionYy1(tmp);
        newBelt.overlapSameDirectionXx2(tmp);
        newBelt.overlapSameDirectionYy2(tmp); */
        beltList.add(unifiedBelt);
    }

    public boolean sourceConnected(int sourceXx, int sourceYy, int sourceDirection) {
        boolean result = false;
        ConveyerBelt tmp;
        for (int i = 0; i < beltList.size(); i++) {
            tmp = beltList.get(i);
            if ((tmp.getXx1() == sourceXx) && (tmp.getYy1() == sourceYy) && (tmp.getDirection() == sourceDirection)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void fillBitmap(int[][] bitmap) {
        ConveyerBelt tmp;
        for (int i = 0; i < beltList.size(); i++) {
            tmp = beltList.get(i);
            switch (tmp.getDirection()) {
                case 1:
                    for (int j = tmp.getXx2(); j >= tmp.getXx1(); j--) {
                        bitmap[j][tmp.getYy1()] = 1;
                    }
                    break;
                case 2:
                    for (int j = tmp.getYy1(); j <= tmp.getYy2(); j++) {
                        bitmap[tmp.getXx1()][j] = 1;
                    }
                    break;
                case 3:
                    for (int j = tmp.getXx1(); j >= tmp.getXx2(); j--) {
                        bitmap[j][tmp.getYy1()] = 1;
                    }
                    break;
                case 4:
                    for (int j = tmp.getYy2(); j <= tmp.getYy1(); j++) {
                        bitmap[tmp.getXx1()][j] = 1;
                    }
                    break;
            }

        }
    }

    private boolean isTargetable(int xx, int yy, int direction, int[][] bitmap, int theme) {
        boolean result = true;
        switch (direction) {
            case 1: {
                for (int i = xx; i <= xx + 5; i++) {
                    for (int j = yy - 2; j <= yy + 2; j++) {
                        if (((i != xx) || (j != yy)) && (i > 0) && (i < 21) && (j > 0) && (j < 21) && (bitmap[i][j] != 0)) {
                            result = false;
                            break;
                        }
                        if ((theme == 1) && (((i == 6) && (j == 1)) || ((i == 8) && (j == 1)))) {
                            result = false;
                            break;
                        }
                        if ((theme == 2) && (((i == 20) && (j == 11)) || ((i == 20) && (j == 13)))) {
                            result = false;
                            break;
                        }
                    }
                    if (!result) {
                        break;
                    }
                }
                for (int k = 0; k < targetableBlock1x.length; k++) {
                    if ((xx > targetableBlock1x[k]) && (yy == targetableBlock1y[k])) {
                        result = false;
                        break;
                    }
                }
                break;
            }
            case 2: {
                for (int j = yy; j <= yy + 5; j++) {
                    for (int i = xx - 2; i <= xx + 2; i++) {
                        if (((i != xx) || (j != yy)) && (i > 0) && (i < 21) && (j > 0) && (j < 21) && (bitmap[i][j] != 0)) {
                            result = false;
                            break;
                        }
                        if ((theme == 1) && (((i == 6) && (j == 1)) || ((i == 8) && (j == 1)))) {
                            result = false;
                            break;
                        }
                        if ((theme == 2) && (((i == 20) && (j == 11)) || ((i == 20) && (j == 13)))) {
                            result = false;
                            break;
                        }
                    }
                    if (!result) {
                        break;
                    }
                }
                for (int k = 0; k < targetableBlock2x.length; k++) {
                    if ((xx == targetableBlock2x[k]) && (yy > targetableBlock2y[k])) {
                        result = false;
                        break;
                    }
                }
                break;
            }
            case 3: {
                for (int i = xx; i >= xx - 5; i--) {
                    for (int j = yy - 2; j <= yy + 2; j++) {
                        if (((i != xx) || (j != yy)) && (i > 0) && (i < 21) && (j > 0) && (j < 21) && (bitmap[i][j] != 0)) {
                            result = false;
                            break;
                        }
                        if ((theme == 1) && (((i == 6) && (j == 1)) || ((i == 8) && (j == 1)))) {
                            result = false;
                            break;
                        }
                        if ((theme == 2) && (((i == 20) && (j == 11)) || ((i == 20) && (j == 13)))) {
                            result = false;
                            break;
                        }
                    }
                    if (!result) {
                        break;
                    }
                }
                for (int k = 0; k < targetableBlock3x.length; k++) {
                    if ((xx < targetableBlock3x[k]) && (yy == targetableBlock3y[k])) {
                        result = false;
                        break;
                    }
                }
                break;
            }
            case 4: {
                for (int j = yy; j >= yy - 5; j--) {
                    for (int i = xx - 2; i <= xx + 2; i++) {
                        if (((i != xx) || (j != yy)) && (i > 0) && (i < 21) && (j > 0) && (j < 21) && (bitmap[i][j] != 0)) {
                            result = false;
                            break;
                        }
                        if ((theme == 1) && (((i == 6) && (j == 1)) || ((i == 8) && (j == 1)))) {
                            result = false;
                            break;
                        }
                        if ((theme == 2) && (((i == 20) && (j == 11)) || ((i == 20) && (j == 13)))) {
                            result = false;
                            break;
                        }
                    }
                    if (!result) {
                        break;
                    }
                }
                for (int k = 0; k < targetableBlock4x.length; k++) {
                    if ((xx == targetableBlock4x[k]) && (yy < targetableBlock4y[k])) {
                        result = false;
                        break;
                    }
                }
                break;
            }
        }
        return result;
    }

    public int selectTarget(int[][] bitmap, TargetCreatingManager targetCreator, int theme) {
        int count = 0;
        ConveyerBelt tmp;
        HighlightedCell hl = new HighlightedCell(1, 1);
        for (int i = 0; i < beltList.size(); i++) {
            tmp = beltList.get(i);
            if (isTargetable(tmp.getXx2(), tmp.getYy2(), tmp.getDirection(), bitmap, theme)) {
                hl.setXx(tmp.getXx2());
                hl.setYy(tmp.getYy2());
                tmpg.drawImage(hl.imgArr.getImg(), hl.getX(), hl.getY(), null);
                count++;
                switch (tmp.getDirection()) {
                    case 1:
                        bitmap[tmp.getXx2()][tmp.getYy2()] = 33;
                        targetCreator.add(tmp.getXx2(), tmp.getYy2(), 3);
                        break;
                    case 2:
                        bitmap[tmp.getXx2()][tmp.getYy2()] = 34;
                        targetCreator.add(tmp.getXx2(), tmp.getYy2(), 4);
                        break;
                    case 3:
                        bitmap[tmp.getXx2()][tmp.getYy2()] = 31;
                        targetCreator.add(tmp.getXx2(), tmp.getYy2(), 1);
                        break;
                    case 4:
                        bitmap[tmp.getXx2()][tmp.getYy2()] = 32;
                        targetCreator.add(tmp.getXx2(), tmp.getYy2(), 2);
                        break;
                }
            } else {
                if (bitmap[tmp.getXx2()][tmp.getYy2()] == 1) {
                    bitmap[tmp.getXx2()][tmp.getYy2()] = 10;
                }
            }

        }
        return count;
    }

    public void generateDb(int[][] bitmap, List<Conveyer> conveyerDb, int mapNumber) {
        File f = new File("Data/custom/custom" + mapNumber + ".map");
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            fw = new FileWriter(f, true);
            pw = new PrintWriter(fw);
            /////////////////////////////
            ConveyerBelt tmp;
            for (int i = 0; i < beltList.size(); i++) {
                tmp = beltList.get(i);
                switch (tmp.getDirection()) {
                    case 1:
                        for (int j = tmp.getXx2(); j >= tmp.getXx1(); j--) {
                            if (bitmap[j][tmp.getYy1()] == 1) {
                                pw.println(j + " " + tmp.getYy1() + " " + "C" + " " + tmp.getDirection());
                                Conveyer t = new Conveyer(j, tmp.getYy1(), tmp.getDirection(), 2);
                                conveyerDb.add(t);
                            }
                            if (bitmap[j][tmp.getYy1()] == 10) {
                                pw.println(j + " " + tmp.getYy1() + " " + "C" + " " + 3);
                                Conveyer t = new Conveyer(j, tmp.getYy1(), 3, 2);
                                conveyerDb.add(t);
                            }
                        }
                        break;
                    case 2:
                        for (int j = tmp.getYy1(); j <= tmp.getYy2(); j++) {
                            if (bitmap[tmp.getXx1()][j] == 1) {
                                pw.println(tmp.getXx1() + " " + j + " " + "C" + " " + tmp.getDirection());
                                Conveyer t = new Conveyer(tmp.getXx1(), j, tmp.getDirection(), 2);
                                conveyerDb.add(t);
                            }
                            if (bitmap[tmp.getXx1()][j] == 10) {
                                pw.println(tmp.getXx1() + " " + j + " " + "C" + " " + 4);
                                Conveyer t = new Conveyer(tmp.getXx1(), j, 4, 2);
                                conveyerDb.add(t);
                            }
                        }
                        break;
                    case 3:
                        for (int j = tmp.getXx1(); j >= tmp.getXx2(); j--) {
                            if (bitmap[j][tmp.getYy1()] == 1) {
                                pw.println(j + " " + tmp.getYy1() + " " + "C" + " " + tmp.getDirection());
                                Conveyer t = new Conveyer(j, tmp.getYy1(), tmp.getDirection(), 2);
                                conveyerDb.add(t);
                            }
                            if (bitmap[j][tmp.getYy1()] == 10) {
                                pw.println(j + " " + tmp.getYy1() + " " + "C" + " " + 1);
                                Conveyer t = new Conveyer(j, tmp.getYy1(), 1, 2);
                                conveyerDb.add(t);
                            }
                        }
                        break;
                    case 4:
                        for (int j = tmp.getYy2(); j <= tmp.getYy1(); j++) {
                            if (bitmap[tmp.getXx1()][j] == 1) {
                                pw.println(tmp.getXx1() + " " + j + " " + "C" + " " + tmp.getDirection());
                                Conveyer t = new Conveyer(tmp.getXx1(), j, tmp.getDirection(), 2);
                                conveyerDb.add(t);
                            }
                            if (bitmap[tmp.getXx1()][j] == 10) {
                                pw.println(tmp.getXx1() + " " + j + " " + "C" + " " + 2);
                                Conveyer t = new Conveyer(tmp.getXx1(), j, 2, 2);
                                conveyerDb.add(t);
                            }
                        }
                        break;
                }
            }
            /////////////////////////
            pw.close();
        } catch (Exception ex) {
            System.out.println("error");
        }
    }
    /*
    public void drawFinish() {
    List<Conveyer> conveyerList = new ArrayList<Conveyer>();
    ConveyerBelt tmp;
    for (int i = 0; i < beltList.size(); i++) {
    tmp = beltList.get(i);
    switch (tmp.getDirection()) {
    case 1:
    for (int j = tmp.getXx2(); j >= tmp.getXx1(); j--) {
    Conveyer t = new Conveyer(j, tmp.getYy1(), 1, 2);
    conveyerList.add(t);
    }
    break;
    case 2:
    for (int j = tmp.getYy1(); j <= tmp.getYy2(); j++) {
    Conveyer t = new Conveyer(tmp.getXx1(), j, 2, 2);
    conveyerList.add(t);
    }
    break;
    case 3:
    for (int j = tmp.getXx1(); j >= tmp.getXx2(); j--) {
    Conveyer t = new Conveyer(j, tmp.getYy1(), 3, 2);
    conveyerList.add(t);
    }
    break;
    case 4:
    for (int j = tmp.getYy2(); j <= tmp.getYy1(); j++) {
    Conveyer t = new Conveyer(tmp.getXx1(), j, 4, 2);
    conveyerList.add(t);
    }
    break;
    }
    }
    Conveyer tem;
    for (int k = -19; k <= 19; k++) {
    for (int j = 0; j < conveyerList.size(); j++) {
    tem = conveyerList.get(j);
    if (tem.getZindex() == k) {
    tmpg.drawImage(tem.imgArr.getImg(), tem.getX(), tem.getY(), null);
    }
    }
    }
    }
     * */

    public void drawAll() {
        ConveyerBelt tmp;
        for (int i = 0; i < beltList.size(); i++) {
            tmp = beltList.get(i);
            switch (tmp.getDirection()) {
                case 1:
                    for (int j = tmp.getXx2(); j >= tmp.getXx1(); j--) {
                        MapConveyer t = new MapConveyer(j, tmp.getYy1(), 1, 2);
//                        g.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                        tmpg.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                    }
                    break;
                case 2:
                    for (int j = tmp.getYy1(); j <= tmp.getYy2(); j++) {
                        MapConveyer t = new MapConveyer(tmp.getXx1(), j, 2, 2);
//                        g.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                        tmpg.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                    }
                    break;
                case 3:
                    for (int j = tmp.getXx1(); j >= tmp.getXx2(); j--) {
                        MapConveyer t = new MapConveyer(j, tmp.getYy1(), 3, 2);
//                        g.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                        tmpg.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                    }
                    break;
                case 4:
                    for (int j = tmp.getYy2(); j <= tmp.getYy1(); j++) {
                        MapConveyer t = new MapConveyer(tmp.getXx1(), j, 4, 2);
//                        g.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                        tmpg.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                    }
                    break;
            }
        }
//        screen.update();
    }
}
