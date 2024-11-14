package view.lose;

import controller.GameController;
import model.MapMatrix;
import view.FrameUtil;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

import static view.level.LevelFrame.getCurrentLevel;

public class LoseFrame extends JFrame {

    private JLabel loseMsg;
    private JButton okBtn;

    public LoseFrame(int width, int height, int currentLevel) throws FileNotFoundException {
        this.setTitle("You Lose in Level "+currentLevel);
        this.setLayout(null);
        this.setSize(width, height);

        //todo:调整窗口、标签的大小

        this.loseMsg = FrameUtil.createJLabel(this,new Point(width/2,height/2),500,50,"You lose! Please try again.");
        this.okBtn = FrameUtil.createButton(this,"OK",new Point(width/2+50,height/2+50),50,50);

        okBtn.addActionListener(e ->{
            this.setVisible(false);
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
