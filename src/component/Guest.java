package component;

public class Guest extends PlayGroundComponent{
    public Guest(int totalGuestID,int direction){
      
        for (int tem=1;tem<=totalGuestID;tem++){
                this.imgArr.addImg(System.getProperty("user.dir") + "/src/images/guest/guest"+tem+"dir"+direction+".png");
        }
    }
    public ImgArr getImgArr(){
        return this.imgArr;
    }
    public void setGuestID(int guestID){
        this.imgArr.setCurrentIndex(guestID);
    }
}
