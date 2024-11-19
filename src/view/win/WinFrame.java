package view.win;

import view.FrameUtil;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

import static view.level.LevelFrame.getCurrentLevel;

public class WinFrame extends JFrame{

    private JLabel winMsg;

    private JButton okBtn;

    //todo:调整窗口、标签的大小
    public WinFrame(int width, int height, int currentLevel)  {
        this.setTitle("You have passed Level " + currentLevel + "!");
        this.setLayout(null);
        this.setSize(width, height);

        this.winMsg = FrameUtil.createJLabel(this,new Point(200,20),500,50,"You win! You can enter the next level.");
        this.okBtn = FrameUtil.createButton(this,"OK",new Point(250,70),100,50);

        okBtn.addActionListener(e ->{
            this.setVisible(false);
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
