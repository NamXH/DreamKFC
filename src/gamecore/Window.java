package gamecore;


public class Window {

    protected Screen screen;

    public Window(Screen aScreen) {
        screen = aScreen;
    }

    public void setScreen(Screen aScreen) {
        screen = aScreen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void init() {
        screen.setScreen();
    }

    public void draw() {
        screen.update();
    }
}
