package view.game;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

import controller.GameController;
import model.MapMatrix;
import view.FrameUtil;
import view.ai.AIFrame;
import view.level.LevelFrame;

import static view.level.LevelFrame.getCurrentLevel;

public class LoadLevelFrame extends JFrame {


    private GameController controller;
    private JButton restartBtn;
    private JButton backBtn;
    private JButton AISloveBtn;

    private JLabel stepLabel;
    private GamePanel gamePanel;

    public LoadLevelFrame(int width, int height, MapMatrix mapMatrix, String path) throws FileNotFoundException {
        this.setTitle("Load Level "+path);
        this.setLayout(null);
        this.setSize(width, height);
        gamePanel = new GamePanel(mapMatrix);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2);
        this.add(gamePanel);
        this.controller = new GameController(gamePanel, mapMatrix);

        this.restartBtn = FrameUtil.createButton(this, "Restart", new Point(gamePanel.getWidth() + 80, 120), 80, 50);
        this.backBtn = FrameUtil.createButton(this, "Go Back", new Point(gamePanel.getWidth() + 80, 210), 80, 50);
        this.AISloveBtn = FrameUtil.createButton(this,"AI Solver", new Point(gamePanel.getWidth() + 80, 300), 80, 50);
        this.stepLabel = FrameUtil.createJLabel(this, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50);
        gamePanel.setStepLabel(stepLabel);

        this.restartBtn.addActionListener(e -> {
            controller.restartGame(path);
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.backBtn.addActionListener(e -> {
            this.setVisible(false);
            LevelFrame levelFrame = new LevelFrame(500,200);
            levelFrame.setVisible(true);
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.AISloveBtn.addActionListener(e -> {
            gamePanel.requestFocusInWindow();
            try {
                AIFrame aiFrame = new AIFrame(600,450, controller.AISolve());
                aiFrame.setVisible(true);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
