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

    public LoseFrame(int width, int height, int currentLevel)  {
        this.setTitle("You Lose in Level "+currentLevel);
        this.setLayout(null);
        this.setSize(width, height);
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);

        //todo:调整颜色

        this.loseMsg = FrameUtil.createJLabel(layeredPane,new Point(200,20),500,50,"You lose! Please try again.",JLayeredPane.MODAL_LAYER);
        this.okBtn = FrameUtil.createButton(layeredPane,"OK",new Point(250,70),100,50,JLayeredPane.MODAL_LAYER);

        okBtn.addActionListener(e ->{
            this.setVisible(false);
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
