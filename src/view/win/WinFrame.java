package view.win;

import view.FrameUtil;
import view.game.GameFrame;
import view.level.LevelFrame;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

import static view.level.LevelFrame.getCurrentLevel;

public class WinFrame extends JFrame{

    private JLabel winMsg;

    private JButton okBtn;

    //todo:调整窗口、标签的大小
    public WinFrame(int width, int height, int currentLevel)  {
        this.setLayout(null);
        this.setSize(width, height);
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);
        if(getCurrentLevel()!=-1) {
            this.setTitle("You have passed Level " + currentLevel + "!");
            this.winMsg = FrameUtil.createJLabel(layeredPane, new Point(200, 20), 500, 50, "You win! You can enter the next level.", JLayeredPane.MODAL_LAYER);
        }else{
            this.setTitle("You have passed Load Level!");
            this.winMsg = FrameUtil.createJLabel(layeredPane, new Point(250, 20), 500, 50, "You win!", JLayeredPane.MODAL_LAYER);
        }
        this.okBtn = FrameUtil.createButton(layeredPane,"OK",new Point(250,70),100,50,JLayeredPane.MODAL_LAYER);

        okBtn.addActionListener(e ->{
            this.setVisible(false);
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
