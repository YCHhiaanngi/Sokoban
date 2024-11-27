import error.ErrorFrame;
import view.level.LevelFrame;
import view.login.LoginFrame;
import javax.swing.*;

import static view.bgm.BGMFrame.playMusic;

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
}
