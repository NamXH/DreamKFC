package gamecore;

import component.JPanelExtend;
import gamecore.GameState;
import gamecore.User;
import gamecore.Game;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainMenu extends GameState {

    private BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
    private ArrayList<component.MenuComponent> menuIcons = new ArrayList<component.MenuComponent>();
    private JFrame frame;
    private boolean userMenuSet = false;
    private boolean tutorialSet = false;
    private User currentUser;
    Graphics2D g = screen.getGraphics();

    public MainMenu(Game game, int state) {
        super(game, state);
        this.frame = screen.getFrame();
    }

    public void runState() {
        currentUser = new User();
        begin();
        loadMainSelect();
        loadRemainIcon();
        initMainSelect();
    }


    //============<OLD STUFF>


//Loading game

    private void begin() {
        Graphics2D gARGB = img.createGraphics();
        BufferedImage RGBimage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D gRGB = RGBimage.createGraphics();
        for(int i=1; i<5; i++){
            Image imgToDraw = loadImage("Images/JPG/LoadingTheme/" + i + ".jpg");
            gARGB.drawImage(imgToDraw, 0, 0, null);
            g.drawImage(img, 0, 0, null);
            screen.update();
            delay(600);
        }
        float scaleFactor = .8f;
        RescaleOp op = new RescaleOp(scaleFactor, 0, null);
        gRGB.drawImage(img, 0, 0, null);
        for(int i=1; i<18; i++){
            RGBimage = op.filter(RGBimage, null);
            g.drawImage(RGBimage, 0, 0, null);
            screen.update();
            delay(5);
        }
        gARGB.dispose();
        gRGB.dispose();
    }

    private void loadMainSelect() {
        drawIMG("Images/JPG/MainSelect/blanktheme.jpg", 0, 0);
        g.drawImage(img, 0, 0, null);
        screen.update();
        loadMenuIcon();

        boolean endMain = false;
        int x, y;
        int count = 0;

        while(!endMain){
            Graphics2D g1 = screen.getGraphics();
            drawIMG("Images/JPG/MainSelect/blanktheme.jpg", 0, 0);
            count = 0;

            for (component.MenuComponent eachIcon : menuIcons ) {
                x = eachIcon.getX();
                y = eachIcon.getY();
                drawIMG(eachIcon.getImage(), x, y);
                if (x >= 390) {
                    eachIcon.update(100);
                } else count++;
            }

            if (count == menuIcons.size()) endMain = true;
            g1.drawImage(img, 0, 0, null);
            screen.update();
            g1.dispose();
        }
    }

    //Init sprites

    private void loadMenuIcon() {
        Image icon;

        int x = 800, y = 170;
        float dx = -0.1f;

        for (int i = 3; i < 7; i++) {
            icon = loadImage("Images/JPG/MainSelect/" + i + ".png");
            component.MenuComponent menuItem = new component.MenuComponent();
            menuItem.setImage(icon);
            menuItem.setVelocityX(dx);
            menuItem.setX(x);
            menuItem.setY(y);
            menuItem.setInfo(i);
            menuIcons.add(menuItem);
            x = x + 400;
            y = y + 70 + i*6;
        }
    }

    private void loadRemainIcon() {

        Image icon;
        
        icon = loadImage("Images/JPG/MainSelect/7.png");
        component.MenuComponent menuItem = new component.MenuComponent();
        menuItem.setImage(icon);
        menuItem.setX(660);
        menuItem.setY(520);
        menuItem.setInfo(7);
        menuIcons.add(menuItem);

        icon = loadImage("Images/JPG/MainSelect/8.png");
        component.MenuComponent menuItem1 = new component.MenuComponent();
        menuItem1.setImage(icon);
        menuItem1.setX(606);
        menuItem1.setY(206);
        menuItem1.setInfo(8);
        menuIcons.add(menuItem1);

        icon = loadImage("Images/JPG/MainSelect/9.png");
        component.MenuComponent menuItem2 = new component.MenuComponent();
        menuItem2.setImage(icon);
        menuItem2.setX(606);
        menuItem2.setY(330);
        menuItem2.setInfo(9);
        menuIcons.add(menuItem2);
    }

//Init menus

    private void initMainSelect() {

        if (userMenuSet) {
            int length = frame.getContentPane().getComponentCount();
            for (int i = 0; i < length; i++) {
                frame.remove(frame.getContentPane().getComponent(0));
            }
            frame.remove(frame.getContentPane());
            frame.setVisible(true);
            userMenuSet = false;
        }

        if (tutorialSet) {
            int length = frame.getContentPane().getComponentCount();
            for (int i = 0; i < length; i++) {
                frame.remove(frame.getContentPane().getComponent(0));
            }
            frame.remove(frame.getContentPane());
            frame.setVisible(true);
            tutorialSet = false;
        }

        drawIMG("Images/JPG/MainSelect/MainSelectTheme.jpg", 0, 0);
        g.drawImage(img, 0, 0, null);
        screen.update();

        createListener(7);
    }

    private void initUserMenu(int checkStatus) {
        userMenuSet = true;

        drawIMG("Images/JPG/Account1.jpg", 0, 0);
        if (checkStatus == 2) drawIMG("Images/JPG/WrongUser.png", 85, 415);
        if (checkStatus == 3) drawIMG("Images/JPG/CreateFailed.png", 85, 415);
        if (checkStatus == 4) drawIMG("Images/JPG/NullUser.png", 85, 415);

        JPanelExtend panel = new JPanelExtend();
        panel.setBackgroundImage(img);
        panel.setLayout(null);
        frame.setContentPane(panel);

        JTextField jTextField1 = new JTextField();
        jTextField1.setColumns(20);
        jTextField1.setFont(new Font("Times New Roman", 1, 24));
        jTextField1.setBorder(BorderFactory.createEmptyBorder());
        jTextField1.setBounds(160, 227, 180, 27);
        panel.add(jTextField1);

        JTextField jTextField2 = new JTextField();
        jTextField2.setColumns(20);
        jTextField2.setFont(new Font("Times New Roman", 1, 24));
        jTextField2.setBorder(BorderFactory.createEmptyBorder());
        jTextField2.setBounds(160, 353, 180, 27);
        panel.add(jTextField2);

        JPasswordField pass1 = new JPasswordField();
        pass1.setColumns(20);
        pass1.setFont(new Font("Times New Roman", 1, 24));
        pass1.setBorder(BorderFactory.createEmptyBorder());
        pass1.setBounds(425, 227, 165, 27);
        panel.add(pass1);

        JPasswordField pass2 = new JPasswordField();
        pass2.setColumns(20);
        pass2.setFont(new Font("Times New Roman", 1, 24));
        pass2.setBorder(BorderFactory.createEmptyBorder());
        pass2.setBounds(425, 353, 165, 27);
        panel.add(pass2);

        frame.setVisible(true);

        createListener(3);
    }

    private void initTutorial() {
        
        tutorialSet = true;

        Image icon;
        icon = loadImage("Images/JPG/Tutorial.jpg");

        JPanelExtend mainPanel = new JPanelExtend();

        mainPanel.setLayout(null);
        mainPanel.setBackgroundImage(icon);
        frame.setContentPane(mainPanel);

        JPanel panel1 = new JPanel();
        mainPanel.add(panel1);
        panel1.setBounds(70, 145, 640, 360);
        panel1.setLayout(new BorderLayout());

        Icon image = new ImageIcon("Images/JPG/tut.png");
        JLabel label = new JLabel(image);

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setHorizontalScrollBarPolicy(scrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getViewport().add(label);
        //scrollPane.setBorder(null);
        panel1.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        createListener(4);
    }

    private void initAbout() {

//        ArrayList<Map> mapList = new ArrayList<Map>();
//        ObjectOutputStream os = null;
//        try {
//            os = new ObjectOutputStream(new FileOutputStream("data/custom.info"));
//            os.writeObject(mapList);
//            os.close();
//        } catch (IOException ex) {
//        }
        
        drawIMG("Images/JPG/AboutUs.jpg", 0, 0);
        g.drawImage(img, 0, 0, null);
        screen.update();

        createListener(5);
    }

    private void userMenu() {
        clearFrame();
        this.finish(2);
    }

//Event listener

    private void changeFrame(MouseEvent evt, int status) {
        if (evt.getModifiers() == InputEvent.BUTTON1_MASK) {
            Point clickedLocation = evt.getPoint();
            int x, y, count = 0;
            boolean clickIcon = false;

            for (component.MenuComponent eachIcon : menuIcons) {
                if ((isInMenu(eachIcon, status)) && (eachIcon.isClicked(clickedLocation.x, clickedLocation.y))) {
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
    }

    private void createListener(final int status) {
        if (frame.getMouseListeners().length == 0) {
            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    changeFrame(evt, status);
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

    public User getCurrentUser() {
        return this.currentUser;
    }

    private void validateUser() throws IOException {
        String filename = "user.sav";

        JTextField jTextField3 = new JTextField();
        jTextField3 = (JTextField) frame.getContentPane().getComponent(0);
        String userName = jTextField3.getText();

        JPasswordField pass3 = new JPasswordField();
        pass3 = (JPasswordField) frame.getContentPane().getComponent(2);
        String userPass = pass3.getText();

        boolean userExist = false;
        String name = "", pass = "";
        int level = 0;

        ArrayList<User> userList = new ArrayList<User>();
        FileInputStream fileIS = new FileInputStream("data/" + filename);

        if (!userName.isEmpty()) {
            ObjectInputStream ois = new ObjectInputStream(fileIS);
            try {
                userList = (ArrayList<User>) ois.readObject();
            } catch (ClassNotFoundException ex) {
            }
            ois.close();
            if (!userList.isEmpty()) {
                for (int i = 0; i < userList.size(); i++) {
                    name = userList.get(i).getUserName();
                    pass = userList.get(i).getPassword();
                    if ((userName.compareTo(name) == 0) && (userPass.compareTo(pass) == 0)) {
                        level = userList.get(i).getLevel();
                        userExist = true;
                        break;
                    }
                }
            }

            if (!userExist) {
                initUserMenu(2);
            } else {
                currentUser.initUser(name, pass, level);
                userMenu();
            }
        } else initUserMenu(2);
    }

    private void createUser() throws IOException {
        String filename = "user.sav";
        
        JTextField jTextField3 = new JTextField();
        jTextField3 = (JTextField) frame.getContentPane().getComponent(1);
        String userName = jTextField3.getText();

        JPasswordField pass3 = new JPasswordField();
        pass3 = (JPasswordField) frame.getContentPane().getComponent(3);
        String userPass = pass3.getText();

        boolean userExist = false;
        String name = "";

        ArrayList<User> userList = new ArrayList<User>();
        FileInputStream fileIS = new FileInputStream("data/" + filename);

        if (!userName.isEmpty()) {
            ObjectInputStream ois = new ObjectInputStream(fileIS);
            try {
                userList = (ArrayList<User>) ois.readObject();
            } catch (ClassNotFoundException ex) {
            }
            ois.close();
            if (!userList.isEmpty()) {
                for (int i = 0; i < userList.size(); i++) {
                    name = userList.get(i).getUserName();
                    if (userName.compareTo(name) == 0) {
                        userExist = true;
                        break;
                    }
                }
            }

            if (userExist) {
                initUserMenu(3);
            } else {
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("data/" + filename));
                currentUser.initUser(userName, userPass, 1);
                userList.add(currentUser);
                os.writeObject(userList);
                os.close();
                userMenu();
            }
        }
        else initUserMenu(4);
    }

    private void delay(long delayTime) {
        try {
            Thread.sleep(delayTime);
        }
        catch (InterruptedException ex) { }
    }

    private void changeMenu(int index) {
        switch (index) {
            case 3:
                initUserMenu(1);
                break;
            case 4:
                initTutorial();
                break;
            case 5:
                initAbout();
                break;
            case 6:
                toExit();
                break;
            case 7:
                initMainSelect();
                break;
            case 8:
                try {
                    validateUser();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case 9:
                try {
                    createUser();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }

    private void toExit() {
        finish(5);
    }

    private boolean isInMenu(component.MenuComponent menuItem, int status) {
        boolean returnValue = false;
        if ((status == 7) && (menuItem.getInfo() <= 6) && (menuItem.getInfo() >= 3) ) return true;
        if (((status == 4) || (status == 5)) && (menuItem.getInfo() == 7) ) return true;
        if ((status == 3) && ((menuItem.getInfo() == 7) || (menuItem.getInfo() == 8) || (menuItem.getInfo() == 9)) ) return true;
        return returnValue;
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
