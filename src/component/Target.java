package component;

public class Target extends PlayGroundComponent{
    
    private int totalGuestID=7; // so luong image bieu dien khach
    private String[] thought_arr = {"","hamburger","popcorn","vegetable","potato","grindedPotato"}; // ung voi so image bieu dien thought
                                                               // De trong phan tu ban dau de hamburger=1

    private Guest guest;
    private int guestID;
    private Thought thought;
    

    // CONSTRUCTORs
    public Target(int xx,int yy,int direction,int guestID){
            this.xx = xx;
            this.yy = yy;
            this.direction = direction;
            this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/target.png");
            this.guestID=guestID;

            Converter converter = new Converter(this.xx, this.yy);
            this.x=converter.getX()-6;
            this.y=converter.getY()+7;
            this.zIndex=yy-xx;

            int tem;
           
            // =============== THOUGHT
            thought=new Thought(thought_arr);
            
            

            //================ GUEST
            guest=new Guest(totalGuestID,direction);
            
            
            

            switch (this.direction){
               case 1:
                    this.guest.setX(converter.getX()-63);
                    this.guest.setY(converter.getY()-11);
                    this.guest.setXx(this.xx-1);
                    this.guest.setYy(this.yy);
                    this.thought.setX(this.x-40);
                    this.thought.setY(this.y-30);
                    break;
               case 2:
                    this.guest.setX(converter.getX()-73);
                    this.guest.setY(converter.getY()-75);
                    this.guest.setXx(this.xx);
                    this.guest.setYy(this.yy-1);
                    this.thought.setX(this.x-50);
                    this.thought.setY(this.y-70);
                    break;
               case 3:
                    this.guest.setX(converter.getX()+18);
                    this.guest.setY(converter.getY()-71);
                    this.guest.setXx(this.xx+1);
                    this.guest.setYy(this.yy);
                    this.thought.setX(this.x+40);
                    this.thought.setY(this.y-70);
                    break;
               case 4:
                    this.guest.setX(converter.getX()+17);
                    this.guest.setY(converter.getY()-9);
                    this.guest.setXx(this.xx);
                    this.guest.setYy(this.yy+1);
                    this.thought.setX(this.x+40);
                    this.thought.setY(this.y-25);
                    break;
           }
           this.guest.setZIndex(this.guest.getYy()-this.guest.getXx());

    }

    
    public Guest getGuest(){
        return guest;
    }
    public Thought getThought(){
        return thought;
    }
    public void nextGuest(){
        //guest.imgArr.next();        
        if (guestID<totalGuestID) guestID++;
            else guestID=1;
        this.guest.setGuestID(guestID);        
    }
    public void nextThought(){
        //guest.imgArr.randomNext();
        this.thought.next();
    }
}
