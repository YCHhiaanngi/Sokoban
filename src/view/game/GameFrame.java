package view.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import controller.GameController;
import error.ErrorFrame;
import model.MapMatrix;
import view.FrameUtil;
import view.ai.AIFrame;
import view.level.LevelFrame;
import view.lose.LoseFrame;
import view.win.WinFrame;

import static view.level.LevelFrame.getCurrentLevel;
import static view.login.LoginFrame.getUserName;

public class GameFrame extends JFrame {


    private GameController controller;
    private JButton restartBtn;
    private JButton loadBtn;
    private JButton AISolveBtn;
    private JButton saveBtn;
    private JButton upBtn;
    private JButton downBtn;
    private JButton leftBtn;
    private JButton rightBtn;
    private JButton progressBtn;
    private JButton quitBtn;
    private JButton undoBtn;

    private JLabel stepLabel;
    private GamePanel gamePanel;
    private LoadLevelFrame loadLevelFrame;


    public GameFrame(int width, int height, MapMatrix mapMatrix) throws FileNotFoundException {
        this.setTitle("Sokoban   Level:"+getCurrentLevel());
        this.setLayout(null);
        this.setSize(width, height);
        gamePanel = new GamePanel(mapMatrix);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2);
        this.add(gamePanel);
        this.controller = new GameController(gamePanel, mapMatrix);

        this.restartBtn = FrameUtil.createButton(this, "Restart", new Point(gamePanel.getWidth() + 80, 120), 80, 50);
        this.quitBtn = FrameUtil.createButton(this, "Quit", new Point(gamePanel.getWidth() + 160, 120), 80, 50);
        this.loadBtn = FrameUtil.createButton(this, "Load", new Point(gamePanel.getWidth() + 80, 210), 80, 50);
        this.progressBtn = FrameUtil.createButton(this, "Get Previous Progress", new Point(gamePanel.getWidth() + 160, 210), 80, 50);
        this.AISolveBtn = FrameUtil.createButton(this,"AI Solver", new Point(gamePanel.getWidth() + 80, 300), 80, 50);
        this.saveBtn = FrameUtil.createButton(this,"Save", new Point(gamePanel.getWidth() + 160, 300), 80, 50);
        this.undoBtn = FrameUtil.createButton(this,"Undo",new Point(gamePanel.getWidth() + 240 , 300), 80, 50);
        this.upBtn = FrameUtil.createButton(this,"↑", new Point(gamePanel.getWidth() + 110, 360), 30, 30);
        this.downBtn = FrameUtil.createButton(this,"↓", new Point(gamePanel.getWidth() + 110, 390), 30, 30);
        this.leftBtn = FrameUtil.createButton(this,"←", new Point(gamePanel.getWidth() + 80, 390), 30, 30);
        this.rightBtn = FrameUtil.createButton(this,"→", new Point(gamePanel.getWidth() + 140, 390), 30, 30);
        this.stepLabel = FrameUtil.createJLabel(this, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50);
        gamePanel.setStepLabel(stepLabel);

        if(getUserName() == null){
            saveBtn.setEnabled(false);
            progressBtn.setEnabled(false);
        }else{
            controller.autoSave(60);//每隔60秒自动保存
        }

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
        this.AISolveBtn.addActionListener(e -> {
            gamePanel.requestFocusInWindow();
            try {
                AIFrame aiFrame = new AIFrame(600,450, controller.AISolve());
                aiFrame.setVisible(true);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.upBtn.addActionListener(e -> {
            gamePanel.doMoveUp();
            gamePanel.requestFocusInWindow();
        });
        this.downBtn.addActionListener(e -> {
            gamePanel.doMoveDown();
            gamePanel.requestFocusInWindow();
        });
        this.leftBtn.addActionListener(e -> {
            gamePanel.doMoveLeft();
            gamePanel.requestFocusInWindow();
        });
        this.rightBtn.addActionListener(e -> {
            gamePanel.doMoveRight();
            gamePanel.requestFocusInWindow();
        });
        this.saveBtn.addActionListener(e ->{
            controller.saveGame();
            gamePanel.requestFocusInWindow();
        });
        this.progressBtn.addActionListener(e ->{
            controller.loadProgress();
            stepLabel.setText(String.format("Step: %d", gamePanel.getCurrentStep()));
            gamePanel.requestFocusInWindow();
        });
        this.quitBtn.addActionListener(e ->{
            this.setVisible(false);
            GameController.stopAutoSave();
            LevelFrame levelFrame = new LevelFrame(500,200);
            levelFrame.setVisible(true);
        });
        this.undoBtn.addActionListener(e -> {
            controller.undo();
            stepLabel.setText(String.format("Step: %d", gamePanel.getCurrentStep()));
            gamePanel.requestFocusInWindow();
            System.out.println("undo");
        });

        //todo: add other button here

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


}
