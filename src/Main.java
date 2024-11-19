import error.ErrorFrame;
import view.level.LevelFrame;
import view.login.LoginFrame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            playMusic();
            LoginFrame loginFrame = new LoginFrame(1900,600);
            loginFrame.setVisible(true);
//            LevelFrame levelFrame = new LevelFrame(500,200);
//            levelFrame.setVisible(false);
//            loginFrame.setLevelFrame(levelFrame);
        });
    }

    public static void playMusic(){
        try{
            File audioFile = new File("sound/bgm.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            audioStream.close();
        } catch (Exception e) {
            ErrorFrame errorFrame = new ErrorFrame(500,200,"Music file not found");
            errorFrame.setVisible(true);
        }
    }
}
