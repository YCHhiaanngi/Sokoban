package view.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
import static view.login.LoginFrame.isDayTheme;

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
    private BufferedImage gameImage;

//todo:调整页面宽度
    public GameFrame(int width, int height, MapMatrix mapMatrix) throws FileNotFoundException {
        this.setTitle("Sokoban   Level:"+getCurrentLevel());
        this.setLayout(null);
        this.setSize(2000, 900);
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);
        if (isDayTheme) {
            try {
                gameImage = ImageIO.read(new File("img/level-green.jpg"));
            } catch (IOException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Failed to load day theme image");
                errorFrame.setVisible(true);
            }
        } else {
            try {
                gameImage = ImageIO.read(new File("img/level-night.jpg"));
            } catch (IOException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Failed to load night theme image");
                errorFrame.setVisible(true);
            }
        }

        // 创建背景面板
        view.login.BackgroundPanel backgroundPanel = new view.login.BackgroundPanel(gameImage);
        backgroundPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        layeredPane.add(backgroundPanel, JLayeredPane.FRAME_CONTENT_LAYER);

        // 创建游戏面板
        gamePanel = new GamePanel(mapMatrix);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2 + 185);
        layeredPane.add(gamePanel, JLayeredPane.MODAL_LAYER);

        // 初始化控制器
        this.controller = new GameController(gamePanel, mapMatrix);

        // 创建按钮
        this.restartBtn = FrameUtil.createButton(layeredPane, "Restart", new Point(gamePanel.getWidth() + 80, 120), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.quitBtn = FrameUtil.createButton(layeredPane, "Quit", new Point(gamePanel.getWidth() + 360, 120), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.loadBtn = FrameUtil.createButton(layeredPane, "Load", new Point(gamePanel.getWidth() + 80, 225), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.progressBtn = FrameUtil.createButton(layeredPane, "Get Previous Progress", new Point(gamePanel.getWidth() + 80, 330), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.AISolveBtn = FrameUtil.createButton(layeredPane, "AI Solver", new Point(gamePanel.getWidth() + 360, 330), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.saveBtn = FrameUtil.createButton(layeredPane, "Save", new Point(gamePanel.getWidth() + 360, 225), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.undoBtn = FrameUtil.createButton(layeredPane, "Undo", new Point(gamePanel.getWidth() + 360, 440), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.upBtn = FrameUtil.createButton(layeredPane, "↑", new Point(gamePanel.getWidth() + 120, 450), 70, 70, JLayeredPane.PALETTE_LAYER);
        this.downBtn = FrameUtil.createButton(layeredPane, "↓", new Point(gamePanel.getWidth() + 120, 520), 70, 70, JLayeredPane.PALETTE_LAYER);
        this.leftBtn = FrameUtil.createButton(layeredPane, "←", new Point(gamePanel.getWidth() + 50, 520), 70, 70, JLayeredPane.PALETTE_LAYER);
        this.rightBtn = FrameUtil.createButton(layeredPane, "→", new Point(gamePanel.getWidth() + 190, 520), 70, 70, JLayeredPane.PALETTE_LAYER);
        this.stepLabel = FrameUtil.createJLabel(layeredPane, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50, JLayeredPane.PALETTE_LAYER);
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
            try {
                controller.loadGame(string);
                this.setVisible(false);
                gamePanel.requestFocusInWindow();//enable key listener
            }catch(Exception ex){
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Map file not found");
                errorFrame.setVisible(true);
            }
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
