package component;


import gamecore.Screen;


public class WindowComponent {

    protected Screen screen;
    protected int x, y;

    public WindowComponent() {
        this.screen=null;
    }
    public WindowComponent(Screen ascreen) {
        this.screen=ascreen;
    }
    public void setScreen(Screen screen){
        this.screen=screen;
    }
    public Screen getScreen(){
        return screen;
    }

    public void setX(int val) {
        x = val;
    }

    public int getX() {
        return x;
    }

    public void setY(int val) {
        y = val;
    }

    public int getY() {
        return y;
    }    
}
