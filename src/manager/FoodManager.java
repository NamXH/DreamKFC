package manager;


import component.ScoreUp;
import component.Target;
import component.Arrow;
import component.Conveyer;
import component.Food;
import component.Explode;
import component.Converter;
import java.util.ArrayList;
import java.util.List;

//to test:
import java.awt.Graphics2D;
import java.util.Random;

public class FoodManager {

    private List<Food> foodList;
    private int i;
    private Graphics2D g;
    private List<ScoreUp> scoreUpList;
    private List<Explode> explodeList;
    private long score; // addidional_score
    private List<Integer> requestedFoods_added_byExplode;

    public FoodManager(Graphics2D g) {
        foodList = new ArrayList();
        this.g=g;
    }

    public List<Food> loadDB() {
        return foodList;
    }
    public List<Food> getFoodList(){
        return foodList;
    }

    public List<Integer> serve(List<Integer> requestedFoods,int theme){
        if (requestedFoods.size()==0) return requestedFoods;
       
        Random random=new Random();
        int servedFood_index = random.nextInt( (int)(requestedFoods.size())  );                
        int servedFood_ID=requestedFoods.get(servedFood_index);
        
        if (theme==1)
            foodList.add(new Food(7, 1, 2, servedFood_ID)); // 3 chi so dau la theo vi tri, tinh chat cua Nguo^`n
        else
            foodList.add(new Food(20, 12, 3, servedFood_ID)); // 3 chi so dau la theo vi tri, tinh chat cua Nguo^`n

        requestedFoods.remove(servedFood_index);
        
        return requestedFoods;
    }

    public void draw(int xx,int yy) {
        for (i = 0; i < foodList.size(); i++) {
            Food tem = foodList.get(i);
            if (    (tem.getXx()==xx)  &&  (tem.getYy()==yy)  )
                g.drawImage(tem.imgArr.getImg(), tem.getX(), tem.getY(), null);
        }
        
    }

    

    public List<Food> update(List<Conveyer> conveyerList, List<Arrow> arrowList, List<Target> targetList,SoundManager soundManager) {
        this.scoreUpList=new ArrayList();
        this.explodeList=new ArrayList();
        this.requestedFoods_added_byExplode=new ArrayList();
        this.score=0;

        //====== <INIT: EXPLODECHECK ARRAY> ========//
            int[][] explodecheck = new int[22][22];
            for (int j = 0; j <= 20; j++) {
                for (int k = 0; k <= 20; k++) {
                    explodecheck[j][k] = 0;
                }
            }
        //====== </INIT: EXPLODECHECK ARRAY> ========//


        //============= MAIN UPDATE (set x,y,remove from list....)
        for (i = 0; i < foodList.size(); i++) {
            Food tem = foodList.get(i);

            int[] nextCell = tem.getNextCell();

            Converter converter = new Converter(nextCell[0], nextCell[1]);
            int nextCellX = converter.getX();
            int nextCellY = converter.getY();

            int[] currentCell = tem.getCurrentCell();
            Converter converter1 = new Converter(currentCell[0], currentCell[1]);
            int currentCellX = converter1.getX();

            int currentCellY = converter1.getY();


            if (!tem.getStay()) {
                tem.setX(currentCellX + (int) ((float) ((float) ((float) (nextCellX - currentCellX) / (float) tem.getCountDown()) * (tem.getCountDown() - tem.getCurrentCountDown() + 1 ))));
                tem.setY(currentCellY + (int) ((float) ((float) ((float) (nextCellY - currentCellY) / (float) tem.getCountDown()) * (tem.getCountDown() - tem.getCurrentCountDown() + 1 ))));                
            }

            if (tem.getCurrentCountDown() > 0) {
                tem.setCurrentCountDown(tem.getCurrentCountDown() - 1);
            }



            if (tem.getCurrentCountDown() == 0) {
                tem.setXx(nextCell[0]);
                tem.setYy(nextCell[1]);

                int j;
                for (j = 0; j < targetList.size(); j++) {
                    Target temTarget = targetList.get(j);
                    if ((temTarget.getXx() == tem.getXx()) && (temTarget.getYy() == tem.getYy())) {
                        if (tem.getDeathCountDown() == 0) {
                            foodList.remove(i);
                            i--;
                        } else {
                            tem.setDeathCountDown(tem.getDeathCountDown() - 1);
                            tem.imgArr.decreaseOpacity((float)(tem.imgArr.getOpacity()*0.25));
                            tem.setStay(true);
                        }
                        break;
                    }
                }



                if (!tem.getStay()) {
                    tem.setCurrentCountDown(tem.getCountDown());

                    currentCell[0] = tem.getXx();
                    currentCell[1] = tem.getYy();
                    tem.setCurrentCell(currentCell);

                    tem.setX(nextCellX);
                    tem.setY(nextCellY);

                    for (j = 0; j < conveyerList.size(); j++) {
                        Conveyer temConveyer = conveyerList.get(j);
                        if ((temConveyer.getXx() == tem.getXx()) && (temConveyer.getYy() == tem.getYy())) {
                            tem.setDirection(temConveyer.getDirection());
                        }
                    }
                    for (j = 0; j < arrowList.size(); j++) {
                        Arrow temArrow = arrowList.get(j);
                        if ((temArrow.getXx() == tem.getXx()) && (temArrow.getYy() == tem.getYy())) {
                            tem.setDirection(temArrow.getDirection());
                        }
                    }

                    switch (tem.getDirection()) {
                        case 1:
                            if (nextCell[0] < 20) {
                                nextCell[0]++;
                            }
                            break;
                        case 2:
                            if (nextCell[1] < 20) {
                                nextCell[1]++;
                            }
                            break;
                        case 3:
                            if (nextCell[0] > 1) {
                                nextCell[0]--;
                            }
                            break;
                        case 4:
                            if (nextCell[1] > 1) {
                                nextCell[1]--;
                            }
                            break;
                    }
                    tem.setNextCell(nextCell);
                }
            } // end of if tem.getCurrentCountDown==0
            explodecheck[tem.getXx()][tem.getYy()]++;
        }




       //====== <CHECK AND SOLVE: EXPLODE> ========//
            int j,k;
            for (j = 1; j <= 20; j++) {
                for (k = 1; k <= 20; k++) {
                    if (explodecheck[j][k] > 1) {
                        int l;
                        long scoreuptem=0;
                        for (l = 0; l < foodList.size(); l++) {
                            Food tem = foodList.get(l);
                            if ((tem.getXx() == j) && (tem.getYy() == k)) {
                                int scoretem=(int) ((float) 1 / (float) 3 * tem.getScore());
                                scoreuptem+=scoretem;
                                score -= scoretem; 
                                foodList.remove(l);
                                requestedFoods_added_byExplode.add(tem.getId());
                                l--;
                            }
                        }
                        scoreUpList.add(new ScoreUp(j,k,-scoreuptem));
                        explodeList.add(new Explode(j, k));
                        soundManager.explode();
                    }
                }
            }


            for (j=0;j<foodList.size();j++){
               boolean collision=false;
               long scoreuptem=0;
               Food tem1=foodList.get(j);
               for (k=j+1;k<foodList.size();k++){
                   Food tem2=foodList.get(k);

                    if ( (Math.abs(tem1.getX()-tem2.getX())<13) &&
                        (Math.abs(tem1.getY()-tem2.getY())<13) &&
                        (
                            (
                                (tem1.getYy()==tem2.getYy() && (tem1.getDirection()+tem2.getDirection()==4) ||
                                (tem1.getXx()==tem2.getXx() && (tem1.getDirection()+tem2.getDirection()==6)
                            )
                            ||
                            (Math.abs(tem1.getDirection()-tem2.getDirection())==1)
                            ||
                            (Math.abs(tem1.getDirection()-tem2.getDirection())==3)
                        )
                   ) ) ) {

                       int scoretem=(int) ((float) 1 / (float) 3 * tem2.getScore());
                       scoreuptem+=scoretem;
                       score -= scoretem;
                       foodList.remove(k);
                       requestedFoods_added_byExplode.add(tem2.getId());
                       k--;
                       collision=true;
                   }
               }
               if (collision==true){
                    foodList.remove(j);j--;
                    requestedFoods_added_byExplode.add(tem1.getId());
                    scoreuptem+=(int) ((float) 1 / (float) 3 * tem1.getScore());
                    scoreUpList.add(new ScoreUp(tem1.getXx(),tem1.getYy(),-scoreuptem));
                    score-=scoreuptem;
                    explodeList.add(new Explode(tem1.getXx(),tem1.getYy()));
                    soundManager.explode();
               }
            }

            //====== </CHECK AND SOLVE: EXPLODE> ========//



        return foodList;
    }

    public List<Integer> addRequestedFoods(List<Integer> baseRequestedFoods){
        for (int j=0;j<requestedFoods_added_byExplode.size();j++){
            baseRequestedFoods.add(requestedFoods_added_byExplode.get(j));
        }
        return baseRequestedFoods;
    }
    public List<ScoreUp> getScoreUpList(){
        return scoreUpList;
    }
    public List<Explode> getExplodeList(){
        return explodeList;
    }
    public long getAdditionalScore(){
        return score;
    }


}
