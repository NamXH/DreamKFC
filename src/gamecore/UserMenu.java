package gamecore;


import component.JPanelExtend;
import manager.MapManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;


public class UserMenu extends GameState {

    private BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
    private ArrayList<component.MenuComponent> menuIcons = new ArrayList<component.MenuComponent>();
    private ArrayList<Map> mapList = new ArrayList<Map>();
    private JFrame frame;
    private Graphics2D g = screen.getGraphics();
    private MapManager mapManager;
    private int passedID;
    private int passedTheme = -1;
    private JLabel label;
    private ArrayList<component.MenuComponent> mapIcons = new ArrayList<component.MenuComponent>();
    private User currentUser = new User();

    public void setCurrentUser(User aUser) {
        this.currentUser = aUser;
    }

    public int getTheme() {
        return this.passedTheme;
    }

    public int getMapID() {
        return this.passedID;
    }

    public MapManager getMapManager() {
        return this.mapManager;
    }

    public UserMenu(Game game, int state) {
        super(game, state);
        this.frame = screen.getFrame();
    }

    public void runState() {
        begin();
        loadMainSelect();
    }

    //============<OLD STUFF>

    private void begin() {
    }


    private void loadMainSelect() {
        drawIMG("Images/JPG/UserSelect/pickmode.png", 0, 0);
        g.drawImage(img, 0, 0, null);
        screen.update();

        loadMenuIcon();
        createListener();

        frame.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent evt) {
                darkenPic(evt);
            }
        });
    }

    private void darkenPic(MouseEvent evt) {
        Point clickedLocation = evt.getPoint();
        int x, y, count = 0;
        boolean clickIcon = false;
        int info;

        for (component.MenuComponent eachIcon : menuIcons) {
            if (eachIcon.isClicked(clickedLocation.x, clickedLocation.y)) {
                clickIcon = true;
                break;
            }
            count++;
        }

        if (clickIcon) {
            info = menuIcons.get(count).getInfo();
            if (info == 2) {
                drawIMG("Images/JPG/UserSelect/pickmode1.png", 0, 0);
                g.drawImage(img, 0, 0, null);
                screen.update();
            }
            if (info == 3) {
                drawIMG("Images/JPG/UserSelect/pickmode2.png", 0, 0);
                g.drawImage(img, 0, 0, null);
                screen.update();
            }
        } else {
            drawIMG("Images/JPG/UserSelect/pickmode.png", 0, 0);
            g.drawImage(img, 0, 0, null);
            screen.update();
        }
    }

    private void loadMenuIcon() {
        Image icon;
        //135,162//134,341//
        icon = loadImage("Images/JPG/UserSelect/2.png");
        component.MenuComponent menuItem = new component.MenuComponent();
        menuItem.setImage(icon);
        menuItem.setX(135);
        menuItem.setY(162);
        menuItem.setInfo(2);
        menuIcons.add(menuItem);

        icon = loadImage("Images/JPG/UserSelect/3.png");
        component.MenuComponent menuItem1 = new component.MenuComponent();
        menuItem1.setImage(icon);
        menuItem1.setX(134);
        menuItem1.setY(341);
        menuItem1.setInfo(3);
        menuIcons.add(menuItem1);

        icon = loadImage("Images/JPG/UserSelect/0.png");
        component.MenuComponent menuItem3 = new component.MenuComponent();
        menuItem3.setImage(icon);
        menuItem3.setX(660);
        menuItem3.setY(520);
        menuItem3.setInfo(0);
        menuIcons.add(menuItem3);
    }

    private void loadMap(int i) {
        mapManager = new MapManager(i);
        mapList = mapManager.getMapList();
    }

    private void loadCampaignMenu() {
        loadMap(0);
        loadCampaignIcon();
        createListener();
        loadSelectLevel(0);
        createLabelListener(0);
    }

    private void removeLabelListener() {
        if (label != null) {
            int length = label.getMouseListeners().length;
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    label.removeMouseListener(label.getMouseListeners()[0]);
                }
            }
        }
    }

    private void createLabelListener(final int mapType) {
        if (label.getMouseListeners().length == 0) {
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    mapIsClicked(evt, mapType);
                }
            });
        }
    }

    private void mapIsClicked(MouseEvent evt, int mapType) {
        if (evt.getModifiers() == InputEvent.BUTTON1_MASK) {
            Point clickedLocation = evt.getPoint();
            int x, y, count = 0;
            boolean clickIcon = false;

            for (component.MenuComponent eachIcon : mapIcons) {
                if (eachIcon.isClicked(clickedLocation.x, clickedLocation.y)) {
                    clickIcon = true;
                    break;
                }
                count++;
            }

            if (clickIcon) {
                removeLabelListener();
                removeListener();
                passedID = count;
                changeMenu(mapIcons.get(count).getInfo());
            }
        }
    }

    private void loadSelectLevel(int listType) {
        Image icon;
        if (listType == 0) {
            icon = loadImage("Images/JPG/UserSelect/Campaign.jpg");
        } else {
            icon = loadImage("Images/JPG/UserSelect/Custom.jpg");
        }

        BufferedImage imgMap;
        Image imgToDraw = loadImage("Images/JPG/UserSelect/mapSelect.png");

        int mapCount = mapList.size();

        if (mapCount < 2) {
            imgMap = new BufferedImage(670, 430, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = imgMap.createGraphics();
            g2d.drawImage(imgToDraw, 0, 0, null);
            g2d.drawImage(imgToDraw, 0, 215, null);
            if (mapCount == 0) {
                if (listType == 1) {
                    imgToDraw = loadImage("Images/JPG/UserSelect/nomap.png");
                    g2d.drawImage(imgToDraw, 0, 0, null);
                } else {
                    imgToDraw = loadImage("Images/JPG/UserSelect/nocampaign.png");
                    g2d.drawImage(imgToDraw, 0, 0, null);
                }
            } else {
                for (int i = 0; i < mapCount; i++) {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 26));
                    String str = mapList.get(i).getName();
                    if ((i < currentUser.getLevel()) || (listType == 1)) {
                        g2d.drawString("Level " + (i + 1) + " - " + str, 58, (72 + 215 * i));
                    } else {
                        g2d.drawString("Level " + (i + 1) + " - Level up to unlock", 58, (72 + 215 * i));
                    }
                    if (mapList.get(i).getScoreList().size() != 0) {
                        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                        String str1 = mapList.get(i).getScoreList().get(0).getUserName();
                        long score1 = mapList.get(i).getScoreList().get(0).getScore();
                        g2d.drawString("Highest score - " + score1, 78, (112 + 215 * i));
                        g2d.drawString("By " + str1, 78, (142 + 215 * i));
                    } else {
                        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                        g2d.drawString("Unbeaten map", 78, (112 + 215 * i));
                        g2d.drawString("Be the first to overcome!!!", 78, (142 + 215 * i));
                    }

                    imgToDraw = loadImage("Images/JPG/UserSelect/mapTab.png");
                    g2d.drawImage(imgToDraw, 18, (12 + 215*i), null);
                    if ((i < currentUser.getLevel()) || (listType == 1)) {
                        component.MenuComponent menuItem = new component.MenuComponent();
                        menuItem.setImage(imgToDraw);
                        menuItem.setX(18);
                        menuItem.setY(12 + 215 * i);
                        menuItem.setInfo(5);
                        mapIcons.add(menuItem);
                        imgToDraw = mapList.get(i).getPreview();
                        g2d.drawImage(imgToDraw, 429, (48 + 215 * i), null);//411,36
                    }
                }
            }
            g2d.dispose();
        } else {
            imgMap = new BufferedImage(670, 215 * mapCount, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = imgMap.createGraphics();
            for (int i = 0; i < mapCount; i++) {
                g2d.drawImage(imgToDraw, 0, 215 * i, null);
            }
            for (int i = 0; i < mapCount; i++) {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 26));
                String str = mapList.get(i).getName();
                if ((i < currentUser.getLevel()) || (listType == 1)) {
                    g2d.drawString("Level " + (i + 1) + " - " + str, 58, (72 + 215 * i));
                } else {
                    g2d.drawString("Level " + (i + 1) + " - Level up to unlock", 58, (72 + 215 * i));
                }
                if (mapList.get(i).getScoreList().size() != 0) {
                    g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                    String str1 = mapList.get(i).getScoreList().get(0).getUserName();
                    long score1 = mapList.get(i).getScoreList().get(0).getScore();
                    g2d.drawString("Highest score - " + score1, 78, (112 + 215 * i));
                    g2d.drawString("By " + str1, 78, (142 + 215 * i));
                } else {
                    g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                    g2d.drawString("Unbeaten map", 78, (112 + 215 * i));
                    g2d.drawString("Be the first to overcome!!!", 78, (142 + 215 * i));
                }

                imgToDraw = loadImage("Images/JPG/UserSelect/mapTab.png");
                g2d.drawImage(imgToDraw, 18, (12 + 215 * i), null);
                if ((i < currentUser.getLevel()) || (listType == 1)) {
                    component.MenuComponent menuItem = new component.MenuComponent();
                    menuItem.setImage(imgToDraw);
                    menuItem.setX(18);
                    menuItem.setY(12 + 215 * i);
                    menuItem.setInfo(5);
                    mapIcons.add(menuItem);
                    imgToDraw = mapList.get(i).getPreview();
                    g2d.drawImage(imgToDraw, 429, (48 + 215 * i), null);//411,36
                }
            }
            g2d.dispose();
        }

        //18,18,624,191
        //660,215
        //215*i
        //18+215*i

        JPanelExtend mainPanel = new JPanelExtend();
        mainPanel.setLayout(null);
        mainPanel.setBackgroundImage(icon);
        frame.setContentPane(mainPanel);

        JPanel panel1 = new JPanel();
        mainPanel.add(panel1);
        panel1.setBounds(70, 70, 678, 430);
        panel1.setLayout(new BorderLayout());

        Icon image = new ImageIcon(imgMap);
        label = new JLabel(image);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(scrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().add(label);
        //scrollPane.setBorder(null);
        panel1.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

    }

    private void loadCampaignIcon() {
        Image icon;
        icon = loadImage("Images/JPG/UserSelect/0.png");
        component.MenuComponent menuItem = new component.MenuComponent();
        menuItem.setImage(icon);
        menuItem.setX(660);
        menuItem.setY(520);
        menuItem.setInfo(1);
        menuIcons.add(menuItem);

    }

    private void loadCustomMenu() {

        loadMap(1);
        loadCustomIcon();
        createListener();
        loadSelectLevel(1);
        createLabelListener(1);
        
    }

    private void loadCustomIcon() {
        Image icon;
        icon = loadImage("Images/JPG/UserSelect/0.png");
        component.MenuComponent menuItem = new component.MenuComponent();
        menuItem.setImage(icon);
        menuItem.setX(660);
        menuItem.setY(520);
        menuItem.setInfo(1);
        menuIcons.add(menuItem);

        icon = loadImage("Images/JPG/UserSelect/4.png");
        component.MenuComponent menuItem1 = new component.MenuComponent();
        menuItem1.setImage(icon);
        menuItem1.setX(398);
        menuItem1.setY(523);
        menuItem1.setInfo(4);
        menuIcons.add(menuItem1);
    }

    private void loadThemeMenu() {
        loadThemeIcon();
        drawIMG("Images/JPG/UserSelect/selectTheme.png", 0, 0);
        g.drawImage(img, 0, 0, null);
        
        boolean endMain = false;
        int x, y;
        int count = 0;

        while(!endMain){
            Graphics2D g1 = screen.getGraphics();
            drawIMG("Images/JPG/UserSelect/selectTheme.png", 0, 0);
            count = 0;

            for (component.MenuComponent eachIcon : menuIcons ) {
                if (count == 0) {
                    count++;
                } else {
                    x = eachIcon.getX();
                    y = eachIcon.getY();
                    drawIMG(eachIcon.getImage(), x, y);
                    if (x <= 0) {
                        eachIcon.update(100);
                    } else {
                        count++;
                    }
                }
            }

            if (count == menuIcons.size()) endMain = true;
            g1.drawImage(img, 0, 0, null);
            screen.update();
            g1.dispose();
        }

        createListener();
    }

    private void loadThemeIcon() {
        Image icon;

        icon = loadImage("Images/JPG/UserSelect/0.png");
        component.MenuComponent menuItem = new component.MenuComponent();
        menuItem.setImage(icon);
        menuItem.setX(134);
        menuItem.setY(511);
        menuItem.setInfo(3);
        menuIcons.add(menuItem);

        icon = loadImage("Images/JPG/UserSelect/tab1.png");
        component.MenuComponent menuItem1 = new component.MenuComponent();
        menuItem1.setImage(icon);
        menuItem1.setX(-370);
        menuItem1.setY(136);
        menuItem1.setVelocityX(0.1f);
        menuItem1.setInfo(7);
        menuIcons.add(menuItem1);

        icon = loadImage("Images/JPG/UserSelect/tab2.png");
        component.MenuComponent menuItem2 = new component.MenuComponent();
        menuItem2.setImage(icon);
        menuItem2.setX(-500);
        menuItem2.setY(312);
        menuItem2.setVelocityX(0.1f);
        menuItem2.setInfo(8);
        menuIcons.add(menuItem2);


    }

    private void changeMenu(int index) {
        clearFrame();
        removeLabelListener();
        mapIcons.clear();
        menuIcons.clear();
        switch (index) {
            case 1:
                loadMainSelect();
                break;
            case 2:
                loadCampaignMenu();
                break;
            case 3:
                loadCustomMenu();
                break;
            case 4:
                loadThemeMenu();
                break;
            case 5:
                changeState(3);
                break;
            case 7:
                this.passedTheme = 1;
                changeState(4);
                break;
            case 8:
                this.passedTheme = 2;
                changeState(4);
                break;
            case 0:
                toExit();
                break;
        }
    }

    private void changeState(int toState) {
        
        drawIMG("Images/JPG/LoadingTheme/4.jpg", 0, 0);
        g.drawImage(img, 0, 0, null);
        screen.update();

        delay(2000);

        this.finish(toState);        
    }

    private void changeFrame(MouseEvent evt) {
        if (evt.getModifiers() == InputEvent.BUTTON1_MASK) {
            Point clickedLocation = evt.getPoint();
            int x, y, count = 0;
            boolean clickIcon = false;

            for (component.MenuComponent eachIcon : menuIcons) {
                if (eachIcon.isClicked(clickedLocation.x, clickedLocation.y)) {
                    clickIcon = true;
                    break;
                }
                count++;
            }

            if (clickIcon) {
                removeListener();
                changeMenu(menuIcons.get(count).getInfo());
            }
        }
    }

    private void removeListener() {
        int length = frame.getMouseListeners().length;
        if (length > 0) {
            for (int i = 0; i < length; i++)
                frame.removeMouseListener(frame.getMouseListeners()[0]);
        }
        length = frame.getMouseMotionListeners().length;
        if (length > 0) {
            for (int i = 0; i < length; i++)
                frame.removeMouseMotionListener(frame.getMouseMotionListeners()[0]);
        }
    }

    private void createListener() {
        if (frame.getMouseListeners().length == 0) {
            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    changeFrame(evt);
                }
            });
        }
    }

    private void clearFrame() {
        removeListener();
        int n = frame.getContentPane().getComponentCount();
        for (int i = 0; i<n; i++)
            frame.remove(frame.getContentPane().getComponent(0));
        frame.remove(frame.getContentPane());
        n = menuIcons.size();
        for (int i = 0; i<n; i++)
            menuIcons.remove(0);
        menuIcons.clear();
        frame.setVisible(true);
    }

    //Graphics function

    private void drawIMG(String path, int x, int y) {
        Graphics2D g2d = img.createGraphics();
        Image imgToDraw = loadImage(path);
        g2d.drawImage(imgToDraw, x, y, null);
        g2d.dispose();
    }

    private void drawIMG(Image imgToDraw, int x, int y) {
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(imgToDraw, x, y, null);
        g2d.dispose();
    }

    private Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    //Useful function

    private void delay(long delayTime) {
        try {
            Thread.sleep(delayTime);
        }
        catch (InterruptedException ex) { }
    }

    private void toExit() {
        this.finish(1);
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cleanUp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
