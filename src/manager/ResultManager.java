package manager;


import component.ScoreRecord;
import gamecore.PlayGround;
import gamecore.Sound;
import gamecore.User;
import gamecore.Screen;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ResultManager {

    private Graphics2D g;
    private int timeToScore = 250;
    private int i;
    private Sound highScoreSound;
    private boolean win;
    private JFrame frame;
    private BufferedImage highScoreTable, menuBox;
    private Screen screen;
    private boolean highScoreOpened = false, menuBoxOpened = false;
    private MapManager thisMapManager = new MapManager(0);
    private int thisMapID;
    private User currentUser = new User();
    private long userScore;
    private int resultManagerState;
    
    private PlayGround thePlayGround;

    public ResultManager(int playtime, long score, int require, Screen aScreen, SoundManager soundManager, MapManager aMapManager, int aID, User aUser, PlayGround aPlayGround) throws IOException {
        this.thisMapManager = aMapManager;
        this.thisMapID = aID;
        this.currentUser = aUser;
        userScore = score;
        this.screen = aScreen;
        frame = screen.getFrame();
        highScoreSound = new Sound(System.getProperty("user.dir") + "/src/sounds/highScore.wav");
        
        thePlayGround = aPlayGround;

        BufferedImage resultBox;
        if ((require == 0) && (score > 0)) {
            win = true;
            soundManager.result(1);
            resultBox = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/resultBoxWin.png"));
        } else {
            win = false;
            soundManager.result(0);
            resultBox = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/resultBoxLose.png"));
        }

        BufferedImage clock = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/clock.png"));
        BufferedImage highScore = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/highScore.png"));
        BufferedImage ok = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/ok.png"));
        BufferedImage viewHighScore = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/viewHighScore.png"));
        menuBox = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/menuBox2.png"));
        highScoreTable = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/highScoreTable.png"));

        float f = (float) 0.01;
        for (i = 1; i <= 30; i++) {
            g = screen.getGraphics();
            f += 0.005;
            BufferedImage tem = changeOpacity(resultBox, (float) f);
            g.drawImage(tem, 0, 0, null);
            screen.update();
        }

        g.setFont(new Font("Arial", Font.PLAIN, 26));

        if (win) {

            if (((thisMapID + 1) == currentUser.getLevel()) && (thisMapManager.getMap(thisMapID).getMapType() == 1)) {
                currentUser.setLevel(currentUser.getLevel()+1);
                ArrayList<User> userList = new ArrayList<User>();
                FileInputStream fileIS = new FileInputStream("data/user.sav");
                ObjectInputStream ois = new ObjectInputStream(fileIS);
                String name = "";
                try {
                    userList = (ArrayList<User>) ois.readObject();
                } catch (ClassNotFoundException ex) {
                }
                ois.close();
                for (int i = 0; i < userList.size(); i++) {
                    name = userList.get(i).getUserName();
                    if (currentUser.getUserName().compareTo(name) == 0) {
                        userList.set(i, currentUser);
                        break;
                    }
                }
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("data/user.sav"));
                os.writeObject(userList);
                os.close();
            }

            delay(1000);
            g.setColor(Color.black);
            g.drawString("Score:", 180, 280);
            g.setColor(Color.green);
            g.drawString("" + score, 260, 281);
            screen.update();

            delay(1500);
            g.setColor(Color.black);
            g.drawString("+", 160, 328);
            g.drawImage(clock, 180, 305, null);
            g.setColor(Color.green);
            if (playtime > 99) {
                g.drawString("" + playtime, 210, 328);
            } else if (playtime > 9) {
                g.drawString("" + playtime, 220, 328);
            } else {
                g.drawString("" + playtime, 227, 328);
            }
            screen.update();

            delay(1000);
            g.setColor(Color.black);
            g.drawString("x", 260, 328);
            g.setColor(Color.green);
            g.drawString("" + timeToScore, 285, 328);
            screen.update();

            delay(1000);
            g.setColor(Color.BLACK);
            g.drawLine(180, 350, 327, 350);
            g.drawLine(180, 351, 327, 351);
            g.drawLine(180, 352, 327, 352);
            screen.update();

            delay(1000);
            g.setColor(Color.black);
            g.drawString("Total:", 180, 395); // 5 cho so
            g.setColor(Color.green);
            userScore=(long) (score + timeToScore * playtime);
            g.drawString("" + userScore, 255, 395); // 5 cho so
            screen.update();

            // SET IF HIGH SCORE

            ScoreRecord aRecord = new ScoreRecord(currentUser.getUserName(), userScore);
            if (thisMapManager.getMap(thisMapID).isHighScore(aRecord)) {
//                System.out.println(aRecord.getUserName());
                thisMapManager.getMap(thisMapID).addHighScore(aRecord);
                thisMapManager.ChangeMap(thisMapID, thisMapManager.getMap(thisMapID));
                delay(1000);
                g.drawImage(changeOpacity(highScore, (float) 0.9), 322, 305, null);
                screen.update();
                highScoreSound.play(1);
            }

            delay(1000);
            g.drawImage(ok, 165, 450, null);
            g.drawImage(viewHighScore, 235, 452, null);
            screen.update();
        }

        if (win) {
            loadMouseAdapter("win");
        } else {
            loadMouseAdapter("lose");
        }
    }

    ///// MAKE TRANSPARENT
    private BufferedImage changeOpacity(BufferedImage src, float alpha) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        int rule = AlphaComposite.SRC_OVER;
        AlphaComposite ac = AlphaComposite.getInstance(rule, alpha);
        g2.setComposite(ac);
        g2.drawImage(src, null, 0, 0);
        g2.dispose();
        return dest;
    }

    //DELAY
    private void delay(long delayTime) {
        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException ex) {
        }
    }

    //Load MouseAdapter
    private void loadMouseAdapter(String winOrLose) {

        if (winOrLose.equals("win")) {
            // ========== <WIN MOUSE ADAPTER>
            MouseAdapter winMouseAdapter = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent evt) {
//                    System.out.println(evt.getX()+" ... "+evt.getY());
                    if ((!menuBoxOpened) && (evt.getX() > 164) && (evt.getX() < 234) && (evt.getY() > 447) && (evt.getY() < 478)) {
                        // ok button of win
                        if (!menuBoxOpened) {
                            menuBoxOpened = true;
                            g.drawImage(menuBox, 0, 0, null);
                            screen.update();
                        }
                    } else if ((!menuBoxOpened) && (evt.getX() > 234) && (evt.getX() < 382) && (evt.getY() > 447) && (evt.getY() < 478)) {
                        // view highscore of win
                        if ((!highScoreOpened) && (!menuBoxOpened)) {
                            highScoreOpened = true;
                            g.drawImage(highScoreTable, 0, 0, null);
                            g.setColor(Color.YELLOW);
                            g.setFont(new Font("Arial", Font.PLAIN, 22));
                            g.drawString("MAP: "+thisMapManager.getMap(thisMapID).getName(), 180, 240);
                            g.setColor(Color.WHITE);
                            ArrayList<ScoreRecord> mapHighScores = new ArrayList<ScoreRecord>();
                            mapHighScores = thisMapManager.getMap(thisMapID).getScoreList();

                            g.setFont(new Font("Arial", Font.PLAIN, 18));
                            g.drawString("Score", 180, 270);
                            g.drawString("User", 290, 270);
                            g.setFont(new Font("Arial", Font.PLAIN, 16));

                            int maxDisplayRecord=5,displayRecord;
                            if (mapHighScores.size()>=maxDisplayRecord)
                                 displayRecord=maxDisplayRecord;
                            else
                                 displayRecord=mapHighScores.size();
                            
                            for (int m=0; m<displayRecord; m++ ) {
                                g.drawString(""+mapHighScores.get(m).getScore(), 180, 300+30*m);
                                g.drawString(mapHighScores.get(m).getUserName(), 290, 300+30*m);
//                                System.out.println("highscore");
                            }
                            screen.update();
                        }
                    } 
                    if (menuBoxOpened) {

                        //System.out.println(evt.getX()+" ... "+evt.getY());
                        if ((evt.getX() > 406) && (evt.getX() < 561) && (evt.getY() > 168) && (evt.getY() < 210)) {
                            // RESTART
                            resultManagerState=1;
                            thePlayGround.resultManagerSolve();
                            
                        } else if ((evt.getX() > 411) && (evt.getX() < 611) && (evt.getY() > 242) && (evt.getY() < 279)) {
                            // MAIN MENU
                            resultManagerState=2;
                            thePlayGround.resultManagerSolve();
                        } else if ((evt.getX() > 411) && (evt.getX() < 632) && (evt.getY() > 316) && (evt.getY() < 361)) {
                            // SELECT MAP
                            resultManagerState=3;
                            thePlayGround.resultManagerSolve();
                        } else if ((evt.getX() > 410) && (evt.getX() < 602) && (evt.getY() > 388) && (evt.getY() < 432)) {
                            // EXIT GAME
                            resultManagerState=4;
                            thePlayGround.resultManagerSolve();
//                            System.exit(0);
                        }
                    
                    }

                }
            };
            // ========== </WIN MOUSE ADAPTER>
            frame.addMouseListener(winMouseAdapter);
        } else {
            // ========== <LOSE MOUSE ADAPTER>
            MouseAdapter loseMouseAdapter = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent evt) {
//                  System.out.println(evt.getX() + " ... " + evt.getY());
                    
                    if ((evt.getX() > 499) && (evt.getX() < 586) && (evt.getY() > 348) && (evt.getY() < 393)) {
                        // ok button of win
                        if (!menuBoxOpened) {
                            menuBoxOpened = true;
                            g.drawImage(menuBox, 0, 0, null);
                            screen.update();
                        }
                    }
                     if (menuBoxOpened) {
                        //System.out.println(evt.getX()+" ... "+evt.getY());
                        if ((evt.getX() > 406) && (evt.getX() < 561) && (evt.getY() > 168) && (evt.getY() < 210)) {
                            // RESTART
                            resultManagerState=1;
                            thePlayGround.resultManagerSolve();
                        } else if ((evt.getX() > 411) && (evt.getX() < 611) && (evt.getY() > 242) && (evt.getY() < 279)) {
                            // MAIN MENU
                            resultManagerState=2;
                            thePlayGround.resultManagerSolve();
                        } else if ((evt.getX() > 411) && (evt.getX() < 632) && (evt.getY() > 316) && (evt.getY() < 361)) {
                            // SELECT MAP
                            resultManagerState=3;
                            thePlayGround.resultManagerSolve();
                        } else if ((evt.getX() > 410) && (evt.getX() < 602) && (evt.getY() > 388) && (evt.getY() < 432)) {
                            // EXIT GAME
                            resultManagerState=4;  
                            thePlayGround.resultManagerSolve();
                        }
                    }
                }
            };
            // ========== </LOSE MOUSE ADAPTER>
            frame.addMouseListener(loseMouseAdapter);
        }

    }

    public int getResultManagerState(){
        return resultManagerState;
    }
}


