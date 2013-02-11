package component;

public class Food extends PlayGroundComponent {
    final private String[] food_arr = {"","hamburger", "popcorn","vegetable","potato","grindedPotato"}; // De trong phan tu ban dau de hamburger=1
    final private int[] countdown_arr = {0,14, 12,9,10,10}; //toc do cua food
    final private int[] score_arr = {0,5000, 4000,2000,2500,2500};

    private int id;
    private int[] currentCell = new int[2];
    private int[] nextCell = new int[2];
    private long score;
    private int currentCountDown,countDown, deathCountDown;
    private boolean stay;

    public Food(int xx, int yy, int direction, int id){
        this.xx=xx;
        this.yy=yy;
        this.direction=direction;
        this.id=id;
        Converter converter=new Converter(xx,yy);
        this.x=converter.getX();
        this.y=converter.getY();
        this.zIndex=yy;

        
                
        this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/food/"+food_arr[id]+".png");        
        


        this.currentCell[0] = this.xx;
        this.currentCell[1] = this.yy;

        if (direction == 1) {
            if (xx < 20) {
                this.nextCell[0] = xx + 1;
            }
            this.nextCell[1] = yy;
        } else if (direction == 2) {
            this.nextCell[0] = xx;
            if (yy < 20) {                
                this.nextCell[1] = yy + 1;                
            }
        } else if (direction == 3) {
            if (xx > 1) {
                this.nextCell[0] = xx - 1;
            }
            this.nextCell[1] = yy;
        } else if (direction == 4) {
            this.nextCell[0] = xx;
            if (yy > 1) {
                this.nextCell[1] = yy - 1;
            }
        }

        this.stay=false;
        
        this.countDown = countdown_arr[this.id];
        this.currentCountDown = countdown_arr[this.id];
        
        this.deathCountDown = 15;
        this.score = score_arr[this.id];
    }

    //GETTERs
    public int getId(){
        return id;
    }
    public int[] getCurrentCell(){
        return currentCell;
    }
    public int[] getNextCell(){
        return nextCell;
    }
    public long getScore(){
        return score;
    }
    public int getCountDown(){
        return countDown;
    }
    public int getCurrentCountDown(){
        return currentCountDown;
    }
    public int getDeathCountDown(){
        return deathCountDown;
    }
    public boolean getStay(){
        return stay;
    }

    //SETTERs
    public void setCurrentCell(int[] currentCell){
        this.currentCell=currentCell;
    }
    public void setNextCell(int[] nextCell){
        this.nextCell=nextCell;
    }
    public void setCountDown(int countDown){
        this.countDown=countDown;
    }
    public void setCurrentCountDown(int currentCountDown){
        this.currentCountDown=currentCountDown;
    }
    public void setDeathCountDown(int deathCountDown){
        this.deathCountDown=deathCountDown;
    }
    public void setStay(boolean stay){
        this.stay=stay;
    }


}
