package gamecore;

public class Game {

    private Screen gameScreen;
    private int currentState;

    /* ==== Khai bao cac thanh phan cua currentState o day ==== */
    MainMenu state1;
    UserMenu state2;
    PlayGround state3;
    CreateMap state4;

    /* ======================================================== */
    public Game() {
        gameScreen = new Screen();
        currentState = 1;
    }

    public void setCurrentState(int val) {
        currentState = val;
    }

    public int getCurrentState() {
        return currentState;
    }

    public Screen getScreen() {
        return gameScreen;
    }

    public void init() {
        // Init everythings here
        gameScreen.setScreen();
        setCurrentState(1);
        state1 = new MainMenu(this, currentState);
        state2 = new UserMenu(this, currentState);
        state4 = new CreateMap(this, currentState);
    }

    /** 
     * Chuyen currentState
     */
    public void update() {
        switch (currentState) {
            case 1:
                state1.runState();
                break;
            case 2:
                state2.setCurrentUser(state1.getCurrentUser());
                state2.runState();
                break;
            case 3:
                state3 = new PlayGround(this, currentState);
                state3.setInfo(state2.getMapManager(), state2.getMapID(), state1.getCurrentUser());
                state3.start();
                break;
            case 4:                
                state4.setInfo(state2.getMapManager(), state2.getTheme());
                state4.start();
                break;
            case 5:
                gameScreen.getFrame().dispose();
                System.exit(0);
                break;
            default:
        }
    }

    public static void main(String args[]) {
        Game KFC = new Game();
        KFC.init();
        KFC.update();
    }
}
