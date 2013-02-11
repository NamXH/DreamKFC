package component;

public class PlayGroundComponent extends WindowComponent{
    protected int xx,yy,zIndex,direction;
    public ImgArr imgArr=new ImgArr();

    public PlayGroundComponent(){}
    public PlayGroundComponent(int xx,int yy,int direction){
        this.xx=xx;
        this.yy=yy;
        this.zIndex=yy-xx;
        this.direction=direction;
    }

    //SETTERs
    public void setXx(int xx){
        this.xx=xx;
    }
    public void setYy(int yy){
        this.yy=yy;
    }
    public void setZIndex(int zIndex){
        this.zIndex=zIndex;
    }
    public void setDirection(int direction){
        this.direction=direction;
    }

    //GETTERs
    public int getXx(){
        return xx;
    }
    public int getYy(){
        return yy;
    }
    public int getZindex(){
        return zIndex;
    }
    public int getDirection(){
        return direction;
    }  
}

