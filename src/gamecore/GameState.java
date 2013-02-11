package gamecore;


public abstract class GameState extends Window {

    protected Game game;
    protected int state;

    public GameState(Game aGame, int aState) {
        super(aGame.getScreen());
        game = aGame;
        state = aState;
    }

    public void setState(int aState) {
        state = aState;
    }

    public int getState() {
        return state;
    }

    public abstract void start();
    public abstract void cleanUp();

    public void finish(int nextState) {        
        game.setCurrentState(nextState);
        game.update();
    }
}

    

