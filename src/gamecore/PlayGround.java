package gamecore;


import manager.TargetManager;
import manager.MapManager;
import manager.FoodManager;
import manager.ConveyerManager;
import manager.ResultManager;
import manager.ScoreUpManager;
import manager.ExplodeManager;
import manager.SoundManager;
import manager.ArrowManager;
import manager.HintManager;
import component.Target;
import component.ScoreUp;
import component.Arrow;
import component.Conveyer;
import component.Food;
import component.Explode;
import gamecore.GameState;
import gamecore.User;
import gamecore.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class PlayGround extends GameState implements Runnable {

    private MouseAdapter normalMouseAdapter, winMouseAdapter, loseMouseAdapter;
    private BufferedImage bg, menuButton, menuBox;
    private Graphics2D g, gbg;
    private List<Food> foodList = new ArrayList();
    private List<Conveyer> conveyerList = new ArrayList();
    private List<Arrow> arrowList = new ArrayList();
    private List<Target> targetList = new ArrayList();
    private List<Integer> requestedFoods = new ArrayList();
    private List<ScoreUp> scoreUpList = new ArrayList();
    private List<Explode> explodeList = new ArrayList();
    private ConveyerManager conveyerManager;
    private FoodManager foodManager;
    private ArrowManager arrowManager;
    private TargetManager targetManager;
    private ScoreUpManager scoreUpManager;
    private ExplodeManager explodeManager;
    private HintManager hintManager;
    private long score = 0;
    private int require;
    private int playtime;
    private int theme;
    private JFrame frame;
    private BufferedImage offscreen = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
    private BufferedImage loadingImage;
    private Timer timer_createFoods, timer_playTime, timer_require;
    private Thread mainThread;
    private List<BufferedImage> bgList = new ArrayList();
    private int bgIndex = 1, bgCountDown = 4, bgCurrentCountDown;
    private SoundManager soundManager;
    private boolean oneSecondLeft, require0, menuOpened, stillDrawing;
    private ResultManager resultManager;
        
    private MapManager thisMapManager;
    private int thisMapID;
    private User currentUser = new User();
    
    private PlayGround thisPlayGround = this;

    public void setInfo(MapManager aMapManager, int aID, User aUser) {
        this.thisMapManager = aMapManager;
        this.thisMapID = aID;
        this.currentUser = aUser;
    }

    /////////////////////////////
    public PlayGround(Game game, int state) {
        super(game, state);
        bgCurrentCountDown = bgCountDown;
    }

    @Override
    public void start() {
        init();
    }

    @Override
    public void init() {
        timer_createFoods = new Timer();
        timer_playTime = new Timer();
        timer_require = new Timer();

        // <Nam sua> 
        int start, end;
        char c;
        String sub;
        int count;

        int mapType = thisMapManager.getMap(thisMapID).getMapType();

        File f;

        if (mapType == 1) {
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
                if ((s.charAt(0) != '#') && (s.contains("Time"))) {
                    start = 0;
                    end = 0;
                    count = 1;

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
                        switch (count) {
                            case 1:
                                playtime = Integer.parseInt(sub);
                                break;
                            default:
                                break;
                        }
                        end++;
                        start = end;
                        count++;
                    }
                }
                if ((s.charAt(0) != '#') && (s.contains("Require"))) {
                    start = 0;
                    end = 0;
                    count = 1;

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
                        switch (count) {
                            case 1:
                                require = Integer.parseInt(sub);
//                                System.out.println("thinh: "+thisMapID);
                                break;
                            default:
                                break;
                        }
                        end++;
                        start = end;
                        count++;
                    }
                }
                if ((s.charAt(0) != '#') && (s.contains("Theme"))) {
                    start = 0;
                    end = 0;
                    count = 1;

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
                        switch (count) {
                            case 1:
                                theme = Integer.parseInt(sub);
                                break;
                            default:
                                break;
                        }
                        end++;
                        start = end;
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }
        // </Nam sua>

        this.oneSecondLeft = false;

        //====== SOUND
        soundManager = new SoundManager();

        g = screen.getGraphics();

        frame = screen.getFrame();
        initFrameListener();
        int j;

        foodManager = new FoodManager(g);
        foodList = foodManager.loadDB();

        arrowManager = new ArrowManager(g, thisMapID, mapType);
        arrowList = arrowManager.loadDB();

        targetManager = new TargetManager(g, thisMapID, mapType);
        targetList = targetManager.loadDB();
        requestedFoods = targetManager.getRequestedFoods();

        scoreUpManager = new ScoreUpManager(g);

        explodeManager = new ExplodeManager(g);

        //====== HINT
        hintManager = new HintManager(g);

        // Load ngay Conveyer vao bg, coi nhu 1 image
        for (int conveyerStatus = 1; conveyerStatus <= 3; conveyerStatus++) {
            BufferedImage bg1;
            try {
                bg1 = ImageIO.read(new File(System.getProperty("user.dir") + "/src/Images/bg_playground_theme" + theme + ".jpg"));
                conveyerManager = new ConveyerManager(conveyerStatus, thisMapID, mapType);
                conveyerList = conveyerManager.loaddb();

                gbg = (Graphics2D) bg1.createGraphics();
                gbg = conveyerManager.draw(gbg);
                gbg = targetManager.drawCell(gbg); // ve layer nam sau food
                bgList.add(bg1);
                loadingImage = ImageIO.read(new File("Images/JPG/LoadingTheme/4.jpg"));
            } catch (IOException ex) {
                Logger.getLogger(CreateMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // -->

        // MENU BUTTON AND BOX
        try {
            menuButton = ImageIO.read(new File(System.getProperty("user.dir") + "/src/Images/menu.png"));
            menuBox = ImageIO.read(new File(System.getProperty("user.dir") + "/src/Images/menuBox.png"));
        } catch (IOException ex) {
            Logger.getLogger(PlayGround.class.getName()).log(Level.SEVERE, null, ex);
        }




        // ==================== <@TIMER>
        timer_require.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if ((require0) && (!menuOpened)) {
                    mainThread.stop();
                    timer_createFoods.cancel();
                    timer_playTime.cancel();
                    frame.removeMouseListener(normalMouseAdapter);
                    try {
                        resultManager = new ResultManager(playtime, score, require, screen, soundManager, thisMapManager, thisMapID, currentUser, thisPlayGround);
                        resultManagerSolve();
                    } catch (IOException ex) {
                        Logger.getLogger(PlayGround.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.cancel();
                }
            }
        }, 0, 500);


        timer_createFoods.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (!menuOpened) {
                    requestedFoods = foodManager.serve(requestedFoods, theme);
                    foodList = foodManager.getFoodList();
                }
            }
        }, 0, 3000);


        timer_playTime.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (!menuOpened) {
                    if (playtime == 15) {
                        soundManager.last10s();
                    }
                    if (playtime > 0) {
                        if (playtime == 1) {
                            oneSecondLeft = true;
                        }
                        playtime--;
                    } else {
                        if (playtime <= 0) {
                            oneSecondLeft = true;
                        }
                        if (oneSecondLeft) {
                            mainThread.stop();
                            timer_createFoods.cancel();
                            timer_require.cancel();
                            try {
                                resultManager = new ResultManager(playtime, score, require, screen, soundManager, thisMapManager, thisMapID, currentUser, thisPlayGround);
                                resultManagerSolve();
                            } catch (IOException ex) {
                                Logger.getLogger(PlayGround.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            this.cancel();

                            //oneSecondLeft = false;
                        }
                        mainThread.stop();
                    }
                }
            }
        }, 500, 1000);

        // ==================== </TIMER>




        // Call Run
        mainThread = new Thread(this);
        mainThread.start();
    }

////////////////////////////////////////////////////////////////////////
////////////////////////// MAIN THREAD: RUN ////////////////////////////
    public void run() {
        while (true) {
            if (menuOpened) {
                continue;
            }
            stillDrawing = false;
            g.clearRect(0, 0, 800, 600);
            g.drawImage(bgList.get(bgIndex), 0, 0, null);
            // Xu ly de chuyen bg (thay doi conveyer)
            if (bgCurrentCountDown > 0) {
                bgCurrentCountDown--;
            } else {
                bgCurrentCountDown = bgCountDown;
                if (bgIndex < 2) {
                    bgIndex++;
                } else {
                    bgIndex = 0;
                }
            }

            ///// DRAW ////
            // ====== DRAW SPEAKER
            g = soundManager.draw(g);

            // ====== DRAW MENU BUTTON
            g.drawImage(menuButton, 680, 540, null);

            if (!menuOpened) {
                stillDrawing = true;
                arrowManager.draw();
                stillDrawing = false;
            }

            // ===== DRAW HINT
            hintManager.update();
            if (!menuOpened) {
                stillDrawing = true;
                hintManager.draw();
                stillDrawing = false;
            }

            for (int i1 = 1; i1 <= 20; i1++) {
                for (int i2 = 20; i2 >= 1; i2--) {
                    if (!menuOpened) {
                        stillDrawing = true;
                        foodManager.draw(i2, i1);
                        stillDrawing = false;
                    }
                    if (!menuOpened) {
                        stillDrawing = true;
                        targetManager.drawGuest(i2, i1);
                        stillDrawing = false;
                    }
                }
            }



            if (!menuOpened) {
                stillDrawing = true;
                scoreUpManager.draw();
                stillDrawing = false;
            }
            if (!menuOpened) {
                stillDrawing = true;
                explodeManager.draw();
                stillDrawing = false;
            }
            if (!menuOpened) {
                stillDrawing = true;
                targetManager.drawThought(); // layer nam truoc
                stillDrawing = false;
            }

            foodList = foodManager.update(conveyerList, arrowList, targetList, soundManager);

            scoreUpList = foodManager.getScoreUpList();
            score += foodManager.getAdditionalScore();
            for (int j = 0; j < scoreUpList.size(); j++) {
                scoreUpManager.add(scoreUpList.get(j)); //scoreUpList tai playground duoc lay tu foodmanager
                // Buoc nay thuc hien viec move dan` vao cho scoreUpManager
            }

            explodeList = foodManager.getExplodeList();
            for (int j = 0; j < explodeList.size(); j++) {
                explodeManager.add(explodeList.get(j));
            }
            explodeManager.update();

            requestedFoods = foodManager.addRequestedFoods(requestedFoods);



            targetManager.update(foodList, requestedFoods,soundManager);
            requestedFoods = targetManager.getRequestedFoods();

            scoreUpList = targetManager.addAdditional_scoreUp(scoreUpList);
            for (int j = 0; j < scoreUpList.size(); j++) {
                scoreUpManager.add(scoreUpList.get(j)); //scoreUpList tai playground duoc lay tu foodmanager
                // Buoc nay thuc hien viec move dan` vao cho scoreUpManager
            }

            score += targetManager.getAdditional_score();
            require -= targetManager.getServeOK();
            if (require < 0) {
                require = 0;
            }

            // ====== <DRAW: RIBBON>
            if (!menuOpened) {
                stillDrawing = true;
                g.setColor(Color.yellow);
                g.setFont(new Font("Arial", Font.PLAIN, 26));
                g.drawString(Integer.toString(playtime), 505, 37);
                g.drawString(Long.toString(score), 625, 37);
                g.drawString(Integer.toString(require), 757, 37);
                stillDrawing = false;
            }
            // ====== </DRAW: RIBBON>

            scoreUpManager.update();

            screen.update();

            delay(30);
            if (require == 0) {
                delay(30);
                require0 = true;
            }
        }
    }

    //==========================================================================
    //==========================================================================
    //============               <FRAME LISTENER>            ===================
    //==========================================================================
    private void initFrameListener() {

        // ========== <MENU MOUSE ADAPTER>
        final MouseAdapter menuMouseAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {

                // MENU MOUSE ADAPTER ::: <CLOSE MENU>
                if (((evt.getX() > 406) && (evt.getX() < 653) && (evt.getY() > 350) && (evt.getY() < 407)) || ((evt.getX() > 680) && (evt.getX() < 771) && (evt.getY() > 540) && (evt.getY() < 578))) {
                    menuOpened = false;
                    //mainThread.resume();
                    frame.removeMouseListener(this);
                    initFrameListener();
                } else if ((evt.getX() > 408) && (evt.getX() < 560) && (evt.getY() > 132) && (evt.getY() < 176)) {
                    //RESTART
                    cleanUp();
                    g.drawImage(loadingImage, 0, 0, null);
                    screen.update();
                    finish(3);
                } else if ((evt.getX() > 409) && (evt.getX() < 612) && (evt.getY() > 202) && (evt.getY() < 247)) {
                    //MAIN MENU
                    cleanUp();
                    finish(1);
                } else if ((evt.getX() > 410) && (evt.getX() < 636) && (evt.getY() > 280) && (evt.getY() < 331)) {
                    //SELECT MAP
                    cleanUp();
                    finish(2);
                } else if ((evt.getX() > 408) && (evt.getX() < 610) && (evt.getY() > 424) && (evt.getY() < 470)) {
                    //EXIT
                    cleanUp();
                    System.exit(0);
                }

            }
        };
        // ========== </MENU MOUSE ADAPTER>


        // ========== <NORMAL MOUSE ADAPTER>
        normalMouseAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
//                System.out.println(evt.getX() + " " + evt.getY());
                int cellX = isCellX(evt.getX(), evt.getY());
                int cellY = isCellY(evt.getX(), evt.getY());

                if ((cellX > 0) && (cellY > 0)) {
                    int j;
                    for (j = 0; j < arrowList.size(); j++) {
                        Arrow tem = arrowList.get(j);
                        if ((tem.getXx() == cellX) && (tem.getYy() == cellY)) {
                            arrowManager.next(j);
                        }
                    }
                }

                // SPEAKER:
                if ((evt.getX() > 20) && (evt.getX() < 80) && (evt.getY() > 530) && (evt.getY() < 585)) {
                    if (soundManager.getMuted()) {
                        soundManager.resume(playtime);
                    } else {
                        soundManager.stop();
                    }
                }

                // MENU BUTTON
                if ((evt.getX() > 680) && (evt.getX() < 771) && (evt.getY() > 540) && (evt.getY() < 578)) {
                    menuOpened = true;

                    //mainThread.suspend();
                    frame.removeMouseListener(this);
                    frame.addMouseListener(menuMouseAdapter);

                    while (stillDrawing) {
                    }
                    g.drawImage(menuBox, 0, 0, null);
                    g.drawImage(menuButton, 680, 540, null);


                    screen.update();
                }

                if ((evt.getX() > 570) && (evt.getX() < 657) && (evt.getY() > 541) && (evt.getY() < 578)) {
//                    System.out.println("aksjdhkjasdk");
                    if (hintManager.isAvailable()) {
                        hintManager.active(0, foodList, conveyerList, arrowList, targetList);
                        hintManager.initDraw();
                        playtime -= 10;
                    }
                }

            }
        };
        // ========== </NORMAL MOUSE ADAPTER>

        frame.addMouseListener(normalMouseAdapter);




    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////// OTHERS //////////////////////////////////
    public void cleanUp() {
        mainThread.stop();
        timer_createFoods.cancel();
        timer_playTime.cancel();
        timer_require.cancel();
        soundManager.stopAll();

        removeListener();
    }

    private void removeListener() {
        int length = frame.getMouseListeners().length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                frame.removeMouseListener(frame.getMouseListeners()[0]);
            }
        }
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
//============</FRAME LISTENER>

    private void delay(long delayTime) {
        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException ex) {
        }
    }

    public void resultManagerSolve() {

        while (!(resultManager.getResultManagerState()>=1)&&(resultManager.getResultManagerState()<=4)){}
        int tem=resultManager.getResultManagerState();
        if (tem == 1) {
            //RESTART
            cleanUp();
            g.drawImage(loadingImage, 0, 0, null);
            screen.update();
            finish(3);
        } else if (tem == 2) {
            //MAIN MENU
            cleanUp();
            finish(1);
        } else if (tem == 3) {
            //SELECT MAP
            cleanUp();
            finish(2);

        } else if (tem==4){
            //EXIT
            cleanUp();
            System.exit(0);
        }
    }
}

