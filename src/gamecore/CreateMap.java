package gamecore;


import component.JPanelExtend;
import manager.MapManager;
import manager.ConveyerBeltCreatingManager;
import manager.TargetCreatingManager;
import manager.ArrowCreatingManager;
import component.ConveyerBelt;
import component.Target;
import component.MapTarget;
import component.Arrow;
import component.HighlightedCell;
import component.Conveyer;
import gamecore.GameState;
import gamecore.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class CreateMap extends GameState {

    private BufferedImage bg,  tmpScreen;   // ve moi thu vao tmpScr trc' roi moi ve ra screen
    private Graphics2D g,  tmpg;  // tmpScr graphics
    private JFrame frame;
    private int mouseX,  mouseY,  mouseXx,  mouseYy,  clickclick,  del,  selectTarget,  complete;
    private int xx1,  xx2,  yy1,  yy2,  delXx,  delYy;  // start, end cua new ConveryerBelt
    private ConveyerBeltCreatingManager beltCreator;
    private ArrowCreatingManager arrowCreator;
    private HighlightedCell highlight,  startCell,  continueCell;
    private TargetCreatingManager targetCreator;
    private int[][] bitmap = new int[21][21]; // 1 la conveyer, 10 la end cua belt ma ko the tro thanh target, 2 la arrow, 31 32 33 34 la target ko dc select (van dc add vao mapTargetList), 4 la target dc select
    private final int MAXIMUM_TARGET = 5;
    private int targetCount;
    private int mapTime,  mapRequire,  mapNumber,  mapTheme;
    private String mapName;
    private boolean warning;
    private String warningString;
    private List<Conveyer> conveyerDb;
    private List<Arrow> arrowDb;
    private List<Target> targetDb;
    private MapManager mapManager;
    private JTextField jTextField1,  jTextField2,  jTextField3;
    private Image screenShot = null;

    public CreateMap(Game game, int state) {
        super(game, state);
    }

    @Override
    public void init() {
        /* cho nay phai sua thanh get chu ko dat luon  */

        mapNumber = mapManager.getNewMapID();

        // chuan bi ve do hoa
        g = screen.getGraphics();
        if (mapTheme == 1) {
            try {
                bg = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/map/theme1.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                tmpScreen = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/map/theme1.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                bg = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/map/theme2.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                tmpScreen = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/map/theme2.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        frame = screen.getFrame();
        g.setColor(Color.BLUE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 30));

        tmpg = tmpScreen.createGraphics();
        highlight = new HighlightedCell(1, 1);
        startCell = new HighlightedCell(1, 1);
        continueCell = new HighlightedCell(1, 1);

        // init step
        clickclick = 0;
        del = 0;
        selectTarget = 0;
        complete = 0;

        warning = false;

        xx1 = xx2 = yy1 = yy2 = -1;
        beltCreator = new ConveyerBeltCreatingManager(screen, tmpg);
        arrowCreator = new ArrowCreatingManager(screen, tmpg);
        targetCreator = new TargetCreatingManager(screen, tmpg);

        conveyerDb = new ArrayList<Conveyer>();
        arrowDb = new ArrayList<Arrow>();
        targetDb = new ArrayList<Target>();

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                bitmap[i][j] = 0;
            }
        }
        targetCount = 0;
        mapTime = 0;
        mapRequire = 0;

        initFrameListener();

    }

    private void initFrameListener() {
        frame.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        frame.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
    }

    /*Duy*/
    public void setMapManager(MapManager aMapManager) {
        mapManager = aMapManager;
    }

    public void setInfo(MapManager aMapManager, int aTheme) {
        mapManager = aMapManager;
        mapTheme = aTheme;
    }

    @Override
    public void start() {
        init();
        g.drawImage(tmpScreen, 0, 0, null);
        screen.update();
    }

    @Override
    public void cleanUp() {
        int length;
        length = frame.getMouseListeners().length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                frame.removeMouseListener(frame.getMouseListeners()[0]);
            }
        }
        length = frame.getMouseMotionListeners().length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                frame.removeMouseMotionListener(frame.getMouseMotionListeners()[0]);
            }
        }
        int n = frame.getContentPane().getComponentCount();
        for (int i = 0; i < n; i++) {
            frame.remove(frame.getContentPane().getComponent(0));
        }
        frame.remove(frame.getContentPane());
        frame.setVisible(true);
    }

    public int getTheme() {
        return mapTheme;
    }

    public void setTheme(int val) {
        mapTheme = val;
    }

    // RETURN XX (arg: x,y)
    private int isCellX(int x, int y) {
        int result = -1;
        int j = 1;
        int lineX = (int) (0.72 * x + 291.8 - 29 * (j - 1));

        while ((j <= 21) && (lineX > y)) {
            j++;
            lineX = (int) (0.72 * x + 291.8 - 29 * (j - 1));
        }
        if ((j <= 21) && (j > 1)) {
            result = j - 1;
        }
        return result;
    }

    // RETURN YY (arg: x,y)
    private int isCellY(int x, int y) {
        int result = -1;
        int j = 1;
        int lineY = (int) (-0.72 * x + 295.3 + 29 * (j - 1));

        while ((j <= 21) && (lineY < y)) {
            j++;
            lineY = (int) (-0.72 * x + 295.3 + 29 * (j - 1));
        }
        if ((j <= 21) && (j > 1)) {
            result = j - 1;
        }
        return result;
    }

    private int charToInt(char c) {
        int result = -1;
        switch (c) {
            case '0':
                result = 0;
                break;
            case '1':
                result = 1;
                break;
            case '2':
                result = 2;
                break;
            case '3':
                result = 3;
                break;
            case '4':
                result = 4;
                break;
            case '5':
                result = 5;
                break;
            case '6':
                result = 6;
                break;
            case '7':
                result = 7;
                break;
            case '8':
                result = 8;
                break;
            case '9':
                result = 9;
                break;
        }
        return result;
    }

    private int stringToInt(String s) {
        int result = 0;
        if (s.length() == 0) {
            result = -1;
            return result;
        }
        int tmp;
        for (int i = s.length() - 1; i >= 0; i--) {
            tmp = charToInt(s.charAt(i));
            if (tmp == -1) {
                result = -1;
                break;
            } else {
                for (int j = 1; j <= s.length() - 1 - i; j++) {
                    tmp = tmp * 10;
                }
                result += tmp;
            }
        }
        return result;
    }

    private void cleanBitmap() {
        for (int i = 1; i <= 20; i++) {
            for (int j = 1; j <= 20; j++) {
                bitmap[i][j] = 0;
            }
        }
    }

    private void printBitmap() {
        for (int i = 1; i <= 20; i++) {
            for (int j = 1; j <= 20; j++) {
                System.out.print(bitmap[j][i] + " ");
            }
            System.out.println("");
        }
    }

    private void delOption() {
        //lua chon del        
        if ((del == 0) && (clickclick == 0)) {
            del = 1;
        }
    }

    private void selectTargetOption() {
        // lua chon select target
        targetCount = 0;
        if (clickclick == 0) {
            if (del == 1) {
                del = 0;
            }
            if (!arrowCreator.noNullArrow()) {
//                System.out.println("Null Arrows still appear!");
                warningString = "Null Arrows still appear!";
                g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                screen.update();
                warning = true;
            } else {
                if (((mapTheme == 1) && (!beltCreator.sourceConnected(7, 1, 2))) || ((mapTheme == 2) && (!beltCreator.sourceConnected(20, 12, 3)))) {
//                    System.out.println("The Chef has't been connected!");
                    warningString = "The Chef has't been connected!";
                    g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                    screen.update();
                    warning = true;
                } else {
                    cleanBitmap();
                    beltCreator.fillBitmap(bitmap);
                    arrowCreator.fillBitmap(bitmap);
                    if (((mapTheme == 1) && ((bitmap[6][1] != 0) || (bitmap[8][1] != 0))) || ((mapTheme == 2) && ((bitmap[20][11] != 0) || (bitmap[20][13] != 0)))) {
//                        System.out.println("The Chef needs more room");
                        warningString = "The Chef needs more room!";
                        g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                        screen.update();
                        warning = true;
                    } else {
                        // chon target
                        if (beltCreator.selectTarget(bitmap, targetCreator, mapTheme) > 0) {
//                            System.out.println("Select the places for your guest (less than " + (MAXIMUM_TARGET + 1) + ")");
                            g.drawImage(tmpScreen, 0, 0, null);
                            warningString = "Select the places for your guests (less than " + (MAXIMUM_TARGET + 1) + ")";
                            g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                            screen.update();
                            warning = true;
                            selectTarget = 1;
                        } else {
//                            System.out.println("There isn't any place for the guest");
                            warningString = "There isn't any place for the guests!";
                            g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                            screen.update();
                            warning = true;
                        }
                    }
                }
            }
        }
    }

    private void drawMap() {
        // draw finish
        BufferedImage img = null;
        if (mapTheme == 1) {
            try {
                img = ImageIO.read(new File(System.getProperty("user.dir") + "/src/Images/map/blankFloorLeft.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("error");
            }
        } else {
            try {
                img = ImageIO.read(new File(System.getProperty("user.dir") + "/src/Images/map/blankFloorRight.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("error");
            }
        }
        tmpg.drawImage(img, 0, 0, null);
        ////////////////////////////////////////////////////
        Conveyer ctem;
        Arrow atem;
        Target ttem;
        for (int k = -19; k <= 19; k++) {
            for (int j = 0; j < conveyerDb.size(); j++) {
                ctem = conveyerDb.get(j);
                if (ctem.getZindex() == k) {
                    tmpg.drawImage(ctem.imgArr.getImg(), ctem.getX(), ctem.getY(), null);
                }
            }
            for (int j = 0; j < arrowDb.size(); j++) {
                atem = arrowDb.get(j);
                if (atem.getZindex() == k) {
                    Conveyer t = new Conveyer(atem.getXx(), atem.getYy(), 1, 2);
                    tmpg.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                    tmpg.drawImage(atem.imgArr.getImg(), atem.getX(), atem.getY(), null);
                }
            }
            for (int j = 0; j < targetDb.size(); j++) {
                ttem = targetDb.get(j);
                if (ttem.getZindex() == k) {
                    Conveyer t = new Conveyer(ttem.getXx(), ttem.getYy(), 1, 2);
                    tmpg.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                    tmpg.drawImage(ttem.imgArr.getImg(), ttem.getX(), ttem.getY(), null);
                    tmpg.drawImage(ttem.getGuest().imgArr.getImg(), ttem.getGuest().getX(), ttem.getGuest().getY(), null);
                }
            }
        }
        /////////////////////////////////////////////////


        /* Ve cuoi cung va take screenshot*/
        g.drawImage(tmpScreen, 0, 0, null);
        screen.update();

        screenShot = tmpScreen.getScaledInstance(160, 120, Image.SCALE_SMOOTH);

        try {
            Thread.sleep(3000);
        /* Ve cuoi cung va take screenshot*/
        } catch (InterruptedException ex) {
            Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    /* Ve cuoi cung va take screenshot*/


    }
///////////////////////////// Duy them

    private void drawInfo() {
        BufferedImage infoScreen = null;
        try {
            infoScreen = ImageIO.read(new File(System.getProperty("user.dir") + "/src/Images/map/mapinfo.png"));
        } catch (IOException ex) {
            Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
        }

        tmpg.drawImage(infoScreen, 0, 0, null);

        JPanelExtend panel = new JPanelExtend();
        panel.setBackgroundImage(tmpScreen);
        panel.setLayout(null);
        frame.setContentPane(panel);

        jTextField1 = new JTextField();
        jTextField1.setColumns(20);
        jTextField1.setBackground(new Color(85, 2, 4));
        jTextField1.setForeground(new Color(255, 255, 255));
        jTextField1.setFont(new Font("Times New Roman", 1, 24));
        jTextField1.setBorder(BorderFactory.createEmptyBorder());
        jTextField1.setBounds(267, 218, 124, 24);
        panel.add(jTextField1);

        jTextField2 = new JTextField();
        jTextField2.setColumns(20);
        jTextField2.setBackground(new Color(85, 2, 4));
        jTextField2.setForeground(new Color(255, 255, 255));
        jTextField2.setFont(new Font("Times New Roman", 1, 24));
        jTextField2.setBorder(BorderFactory.createEmptyBorder());
        jTextField2.setBounds(267, 284, 124, 24);
        panel.add(jTextField2);

        jTextField3 = new JTextField();
        jTextField3.setColumns(20);
        jTextField3.setBackground(new Color(85, 2, 4));
        jTextField3.setForeground(new Color(255, 255, 255));
        jTextField3.setFont(new Font("Times New Roman", 1, 24));
        jTextField3.setBorder(BorderFactory.createEmptyBorder());
        jTextField3.setBounds(267, 350, 124, 24);
        panel.add(jTextField3);

        frame.setVisible(true);
        g.drawImage(tmpScreen, 0, 0, null);
        screen.update();


    //267,218//124,24
    //267,284//124,24
    //267,350//124,24
    // Note: To get the text:
//        JTextField jTextField = new JTextField();
//        jTextField = (JTextField) frame.getContentPane().getComponent(0);
//        String mapName = jTextField.getText();
    }

//////////////////////////////////////////////////////////// Duy
    private void completeOption() {
        complete = 1;
        drawInfo();
    }

    private void finishOption() {
        mapName = jTextField1.getText();
        mapTime = stringToInt(jTextField2.getText());
        mapRequire = stringToInt(jTextField3.getText());

        if ((mapName.length() > 0) && (mapTime > 0) && (mapTime <= 200) && (mapRequire > 0) && (mapRequire <= 10)) {

            /* =========== sinh text file ============*/
            File f = new File("Data/custom/custom" + mapNumber + ".map");
            FileWriter fw = null;
            PrintWriter pw = null;
            try {
                fw = new FileWriter(f, true);
                pw = new PrintWriter(fw);
                /////////////////////////////////
                pw.println("### Dong thu 1 gom 1 so int la Time");
                pw.println("### Dong thu 2 gom 1 so int la Require");
                pw.println("### Dong thu 3 gom 1 so int la Theme");
                pw.println("### Cac dong tiep theo la cac o cua map voi y nghia nhu vi du sau");
                pw.println("### 1 1 C 3 - o 1 1 la Conveyer co Direction la 3");
                pw.println("### 2 2 A 1 2 3 - o 2 2 la Arrow co cac chieu 1 2 3 duoc mo");
                pw.println("### 3 3 T 2 - o 3 3 la Target co Direction la 2");

                pw.println(mapTime + " Time");
                pw.println(mapRequire + " Require");
                if (mapTheme == 1) {
                    pw.println(1 + " Theme");
                } else {
                    pw.println(2 + " Theme");
                }
                /////////////////////////////////
                pw.close();
            } catch (Exception ex) {
                System.out.println("error");
            }
            /* =========== sinh text file ============*/
            beltCreator.generateDb(bitmap, conveyerDb, mapNumber);
            arrowCreator.generateDb(bitmap, conveyerDb, arrowDb, mapNumber);
            targetCreator.generateDb(bitmap, conveyerDb, targetDb, mapNumber);


            cleanUp();
            drawMap();
            Map newMap = new Map(mapNumber, mapTheme, mapName, screenShot, 2);
            mapManager.addNewMap(newMap);
//            printBitmap();
            finish(2);
        }
    }

    private void resetOption() {

        // chuan bi ve do hoa
        g = screen.getGraphics();
        if (mapTheme == 1) {
            try {
                bg = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/map/theme1.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                tmpScreen = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/map/theme1.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                bg = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/map/theme2.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                tmpScreen = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/map/theme2.png"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        frame = screen.getFrame();
        g.setColor(Color.BLUE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 30));

        tmpg = tmpScreen.createGraphics();
        highlight = new HighlightedCell(1, 1);
        startCell = new HighlightedCell(1, 1);
        continueCell = new HighlightedCell(1, 1);

        // init step
        clickclick = 0;
        del = 0;
        selectTarget = 0;
        complete = 0;
        warning = false;

        xx1 = xx2 = yy1 = yy2 = -1;
        beltCreator = new ConveyerBeltCreatingManager(screen, tmpg);
        arrowCreator = new ArrowCreatingManager(screen, tmpg);
        targetCreator = new TargetCreatingManager(screen, tmpg);

        conveyerDb = new ArrayList<Conveyer>();
        arrowDb = new ArrayList<Arrow>();
        targetDb = new ArrayList<Target>();

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                bitmap[i][j] = 0;
            }
        }
        targetCount = 0;
        mapTime = 0;
        mapRequire = 0;
        g.drawImage(tmpScreen, 0, 0, null);
        screen.update();
    }

    private void cancelOption() {
        cleanUp();
        finish(2);
    }

    private void formMousePressed(MouseEvent evt) {
        if (warning) {
            g.drawImage(tmpScreen, 0, 0, null);
            screen.update();
            warning = false;
        }
        boolean notOption = true;
        mouseX = evt.getX();
        mouseY = evt.getY();        
        if (complete == 0) {
            if ((mouseX >= 151) && (mouseX <= 272) && (mouseY >= 552) && (mouseY <= 587) && (selectTarget == 0)) {
                notOption = false;
                delOption();
            }
            if ((mouseX >= 151) && (mouseX <= 272) && (mouseY >= 552) && (mouseY <= 587) && (selectTarget == 1)) {
//            System.out.println("You can't delete at this step");
                warningString = "You can't delete at this step!";
                g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                screen.update();
                warning = true;
            }
            if ((mouseX >= 18) && (mouseX <= 139) && (mouseY >= 552) && (mouseY <= 587)) {
                notOption = false;
                selectTargetOption();
            }
            if (((mouseX >= 486) && (mouseX <= 575) && (mouseY >= 546) && (mouseY <= 587))) {
                notOption = false;
                resetOption();
            }
            if (((mouseX >= 590) && (mouseX <= 679) && (mouseY >= 546) && (mouseY <= 587)) && (selectTarget == 1) && (targetCount > 0)) {
                notOption = false;
                completeOption();
            }
            if (((mouseX >= 590) && (mouseX <= 679) && (mouseY >= 546) && (mouseY <= 587)) && ((selectTarget == 0) || (targetCount <= 0))) {
//            System.out.println("You haven't chosen any places for the guest");
                warningString = "You haven't chosen any places for the guests!";
                g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                screen.update();
                warning = true;
            }
            if (((mouseX >= 695) && (mouseX <= 785) && (mouseY >= 546) && (mouseY <= 587))) {
                notOption = false;
                cancelOption();
            }
        }
        if (((mouseX >= 212) && (mouseX <= 350) && (mouseY >= 411) && (mouseY <= 452)) && (complete == 1)) {
            notOption = false;
            finishOption();
        }
        if ((notOption) && (complete == 0)) {
            if (selectTarget == 0) {
                if (del == 0) {
                    if (clickclick == 0) {
                        xx1 = isCellX(mouseX, mouseY);
                        yy1 = isCellY(mouseX, mouseY);
                        if ((xx1 > 0) && (yy1 > 0)) {
                            clickclick = 1;
                            startCell.setXx(xx1);
                            startCell.setYy(yy1);
                            tmpg.drawImage(startCell.imgArr.getImg(), startCell.getX(), startCell.getY(), null);

                        } else {
                            clickclick = 0;
                        }

                    } else {
                        clickclick = 0;
                        xx2 = isCellX(mouseX, mouseY);
                        yy2 = isCellY(mouseX, mouseY);

                        if (((xx2 > 0) && (yy2 > 0)) && (((xx1 == xx2) && (yy1 != yy2)) || ((yy1 == yy2) && (xx1 != xx2)))) {
                            ConveyerBelt newBelt = new ConveyerBelt(xx1, yy1, xx2, yy2);
                            tmpg.drawImage(bg, 0, 0, null);
                            beltCreator.add(newBelt, arrowCreator);
                            beltCreator.drawAll();
                            arrowCreator.drawAll();
                            g.drawImage(tmpScreen, 0, 0, null);
                            screen.update();
                        } else {
                            // ko can cap nhat belt chi can xoa highlight o start = cach cap nhat lai tmpScreen
//                    g.drawImage(bg, 0, 0, null);
                            tmpg.drawImage(bg, 0, 0, null);
                            beltCreator.drawAll();
                            arrowCreator.drawAll();
                            g.drawImage(tmpScreen, 0, 0, null);
                            screen.update();
                        }
                    }
                } else { // truong hop del
                    del = 0;
                    delXx = isCellX(mouseX, mouseY);
                    delYy = isCellY(mouseX, mouseY);
                    // neu ko click vao arrow thi moi xet
                    if (!arrowCreator.onArrow(delXx, delYy)) {
                        tmpg.clearRect(0, 0, 800, 600);
                        tmpg.drawImage(bg, 0, 0, null);
                        beltCreator.del(delXx, delYy, arrowCreator);
                        beltCreator.drawAll();
                        arrowCreator.drawAll();
                        g.drawImage(tmpScreen, 0, 0, null);
                        screen.update();
                    }
                }
            } else { //truong hop select target
                mouseXx = isCellX(mouseX, mouseY);
                mouseYy = isCellY(mouseX, mouseY);
                if ((targetCount < MAXIMUM_TARGET) && (mouseXx > 0) && (mouseYy > 0) && (bitmap[mouseXx][mouseYy] > 30)) {
//                    targetCreator.add(mouseXx, mouseYy, bitmap[mouseXx][mouseYy] % 10);
                    MapTarget t = new MapTarget(mouseXx, mouseYy, bitmap[mouseXx][mouseYy] % 10);
                    tmpg.drawImage(t.imgArr.getImg(), t.getX(), t.getY(), null);
                    targetCount++;
                    bitmap[mouseXx][mouseYy] = 4;
                    g.drawImage(tmpScreen, 0, 0, null);
                    screen.update();
                }
            }
        }
    }

    private void formMouseMoved(MouseEvent evt) {
        if (selectTarget == 0) {
            mouseX = evt.getX();
            mouseY = evt.getY();
            mouseXx = isCellX(mouseX, mouseY);
            mouseYy = isCellY(mouseX, mouseY);
            if (clickclick == 0) {
                if ((mouseXx > 0) && (mouseYy > 0)) {
                    highlight.setXx(mouseXx);
                    highlight.setYy(mouseYy);

                    g.drawImage(tmpScreen, 0, 0, null);
                    g.drawImage(highlight.imgArr.getImg(), highlight.getX(), highlight.getY(), null);
                    if (warning) {
                        g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                    }
                    screen.update();
                } else {
                    g.drawImage(tmpScreen, 0, 0, null);
                    if (warning) {
                        g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                    }
                    screen.update();
                }
            }
            if (clickclick == 1) {
                if ((mouseXx == xx1) && (mouseYy > yy1) && (mouseXx > 0) && (mouseYy > 0)) {
                    continueCell.setXx(xx1);
                    g.drawImage(tmpScreen, 0, 0, null);
                    for (int i = yy1 + 1; i <= mouseYy; i++) {
                        continueCell.setYy(i);
                        g.drawImage(continueCell.imgArr.getImg(), continueCell.getX(), continueCell.getY(), null);
                    }
                    if (warning) {
                        g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                    }
                    screen.update();
                }
                if ((mouseXx == xx1) && (mouseYy < yy1) && (mouseXx > 0) && (mouseYy > 0)) {
                    continueCell.setXx(xx1);
                    g.drawImage(tmpScreen, 0, 0, null);
                    for (int i = mouseYy; i <= yy1 - 1; i++) {
                        continueCell.setYy(i);
                        g.drawImage(continueCell.imgArr.getImg(), continueCell.getX(), continueCell.getY(), null);
                    }
                    if (warning) {
                        g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                    }
                    screen.update();
                }
                if ((mouseYy == yy1) && (mouseXx > xx1) && (mouseXx > 0) && (mouseYy > 0)) {
                    continueCell.setYy(yy1);
                    g.drawImage(tmpScreen, 0, 0, null);
                    for (int i = mouseXx; i >= xx1 + 1; i--) {
                        continueCell.setXx(i);
                        g.drawImage(continueCell.imgArr.getImg(), continueCell.getX(), continueCell.getY(), null);
                    }
                    if (warning) {
                        g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                    }
                    screen.update();
                }
                if ((mouseYy == yy1) && (mouseXx < xx1) && (mouseXx > 0) && (mouseYy > 0)) {
                    g.drawImage(tmpScreen, 0, 0, null);
                    continueCell.setYy(yy1);
                    for (int i = xx1 - 1; i >= mouseXx; i--) {
                        continueCell.setXx(i);
                        g.drawImage(continueCell.imgArr.getImg(), continueCell.getX(), continueCell.getY(), null);
                    }
                    if (warning) {
                        g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                    }
                    screen.update();
                }
                if ((mouseXx != xx1) && (mouseYy != yy1) && (mouseXx > 0) && (mouseYy > 0)) {
                    highlight.setXx(mouseXx);
                    highlight.setYy(mouseYy);
                    g.drawImage(tmpScreen, 0, 0, null);
                    g.drawImage(highlight.imgArr.getImg(), highlight.getX(), highlight.getY(), null);
                    if (warning) {
                        g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                    }
                    screen.update();
                }
                if ((mouseXx < 0) || (mouseYy < 0) || ((mouseXx == xx1) && (mouseYy == yy1))) {
                    g.drawImage(tmpScreen, 0, 0, null);
                    if (warning) {
                        g.drawString(warningString, 400 - (warningString.length() / 2) * 12, 250);
                    }
                    screen.update();
                }
            }
        }
    }
}
