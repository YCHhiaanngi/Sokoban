package view.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

import controller.GameController;
import model.MapMatrix;
import view.FrameUtil;
import view.lose.LoseFrame;
import view.win.WinFrame;

import static view.level.LevelFrame.getCurrentLevel;

public class GameFrame extends JFrame {


    private GameController controller;
    private JButton restartBtn;
    private JButton loadBtn;
    private JButton AISloveBtn;

    private JLabel stepLabel;
    private GamePanel gamePanel;
    private LoadLevelFrame loadLevelFrame;
    private LoseFrame loseFrame;
    private WinFrame winFrame;


    public GameFrame(int width, int height, MapMatrix mapMatrix) throws FileNotFoundException {
        this.setTitle("Sokoban   Level:"+getCurrentLevel());
        this.setLayout(null);
        this.setSize(width, height);
        gamePanel = new GamePanel(mapMatrix);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2);
        this.add(gamePanel);
        this.controller = new GameController(gamePanel, mapMatrix);

        this.restartBtn = FrameUtil.createButton(this, "Restart", new Point(gamePanel.getWidth() + 80, 120), 80, 50);
        this.loadBtn = FrameUtil.createButton(this, "Load", new Point(gamePanel.getWidth() + 80, 210), 80, 50);
        this.AISloveBtn = FrameUtil.createButton(this,"AI Solver", new Point(gamePanel.getWidth() + 80, 300), 80, 50);
        this.stepLabel = FrameUtil.createJLabel(this, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50);
        gamePanel.setStepLabel(stepLabel);

        this.restartBtn.addActionListener(e -> {
            controller.restartGame();
            this.stepLabel.setText("New Start");
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.loadBtn.addActionListener(e -> {
            String string = JOptionPane.showInputDialog(this, "Input path:");
            System.out.println(string);
            controller.loadGame(string);
            this.setVisible(false);
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.AISloveBtn.addActionListener(e -> {
            controller.AISolve();
            gamePanel.requestFocusInWindow();
            //todo:写一个单独的窗体输出结果
        });
        //todo: add other button here

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


}
