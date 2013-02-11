package manager;


import gamecore.Sound;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class SoundManager {

    private Timer timer,timer_bgSound1,timer_bgSound2;
    private Sound bgSound, last10s, result;
    private List<Sound> temSoundArr;
    private boolean muted;
    private BufferedImage onImg,offImg,currentImg;

    public SoundManager(){
        timer = new Timer();
        timer_bgSound1=new Timer();
        timer_bgSound2=new Timer();
        temSoundArr = new ArrayList();
        muted=false;
        try {
            onImg= ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/speakerOn.png"));
            offImg=ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/speakerOff.png"));
            currentImg=onImg;
        } catch (IOException ex) {
            Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        /* 0 */ temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/bg04.wav"));
        temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/bg03.wav"));
        temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/last10s.wav"));
        /* 3 */ temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/win.wav"));
        temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/lose.wav"));
        temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/bg01.wav"));
        /* 6 */ temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/bg02.wav"));
        temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/explode.wav"));
        temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/mario3.wav"));        
        /* 9 */ temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/right.wav"));
        temSoundArr.add(new Sound(System.getProperty("user.dir") + "/src/sounds/wrong.wav"));

        randomPlayMainSound();
    }

    private void randomPlayMainSound() {
        Random random = new Random();
        int randomValue = random.nextInt(3);

        System.out.println(randomValue);
        
        if (randomValue == 0) {
            timer_bgSound1.schedule(new TimerTask() {

                public void run() {
                    temSoundArr.get(0).play(1);
                }
            }, 0);
            timer_bgSound2.schedule(new TimerTask() {

                public void run() {
                    temSoundArr.get(1).play(1);                    
                }
            }, 33500);

        } else if (randomValue==1){
            timer_bgSound1.schedule(new TimerTask() {

                public void run() {
                    temSoundArr.get(5).play(1);                    
                }
            }, 0);
            timer_bgSound1.schedule(new TimerTask() {

                public void run() {
                    temSoundArr.get(6).play(1);
                }
            }, 8000);
            timer_bgSound2.schedule(new TimerTask() {

                public void run() {
                    temSoundArr.get(1).play(1);                    
                }
            }, 12500);
        } else if (randomValue==2){
            timer_bgSound1.schedule(new TimerTask() {

                public void run() {
                    temSoundArr.get(8).play(1);                    
                }
            }, 0);
        } 
    }
    public void explode(){
        if (muted) return;
        Sound explode=temSoundArr.get(7);
        explode.play(1);
    }
    public void rightServe(){
        if (muted) return;
        temSoundArr.get(9).play(1);
    }
    public void wrongServe(){
        if (muted) return;
        temSoundArr.get(10).play(1);
    }

    public void last10s() {
        if (muted) return;
        
        temSoundArr.get(2).loop();
        timer.schedule(new TimerTask() {

            public void run() {
                for (int j=0;j<temSoundArr.size();j++){
                    if (j==2) continue;
                    if (temSoundArr.get(j)!=null) temSoundArr.get(j).stop();
                }
                
            }
        }, 500);
    }
    public void result(int status) {  // 1=win; 0=lose;
        for (int j=0;j<temSoundArr.size();j++){
              if (temSoundArr.get(j)!=null) temSoundArr.get(j).stop();
        }

        timer_bgSound1.cancel();
        timer_bgSound2.cancel();

        if (muted) return;
        
        if (status == 1) {
            result = temSoundArr.get(3);
        } else {
            result = temSoundArr.get(4);
        }

        result.play(1);
    }

    public void stop(){
        currentImg=offImg;
        for (int j=0;j<temSoundArr.size();j++){
              if (temSoundArr.get(j)!=null) temSoundArr.get(j).stop();
        }
        


        muted=true;        
    }
    public void resume(int currentTime){
        currentImg=onImg;
        muted=false;
        if (currentTime>15){
            temSoundArr.get(6).play(1);
        } else {
            last10s();
        }
    }
    public Graphics2D draw(Graphics2D baseG){
        baseG.drawImage(currentImg,20,530,null);
        return baseG;
    }
    public boolean getMuted(){
        return muted;
    }
    public void stopAll(){
        if (bgSound!=null) bgSound.stop();
        if (last10s!=null) last10s.stop();
        if (result!=null) result.stop();
        if (timer!=null) timer.cancel();
        if (timer_bgSound1!=null) timer_bgSound1.cancel();
        if (timer_bgSound2!=null) timer_bgSound2.cancel();
        for (int k=0;k<temSoundArr.size();k++){
            if (temSoundArr.get(k)!=null) temSoundArr.get(k).stop();
        }
    }
}

