package gamecore;


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound extends Object implements LineListener {

    private File soundFile;
    private Clip clip;
    private AudioInputStream ais;
    private Line.Info linfo;
    private Line line;

    public Sound(String path) {
        soundFile = new File(path);
    }

    public void loop() {
        try {
            ais = null;
            linfo = new Line.Info(Clip.class);
            line = AudioSystem.getLine(linfo);
            clip = (Clip) line;
            ais = AudioSystem.getAudioInputStream(soundFile);
            clip.open(ais);
            
            clip.loop(clip.LOOP_CONTINUOUSLY);
            ais.close();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void play(int times) {
        try {
            ais = null;
            linfo = new Line.Info(Clip.class);
            line = AudioSystem.getLine(linfo);
            clip = (Clip) line;
            ais = AudioSystem.getAudioInputStream(soundFile);
            clip.open(ais);
            
            clip.loop(times - 1);
            ais.close();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        if (clip==null) return;
        if (clip.isRunning()) {
            try {
                clip.stop();
                ais.close();
            } catch (IOException ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void update(LineEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
