package view.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import controller.GameController;
import error.ErrorFrame;
import model.MapMatrix;
import view.FrameUtil;
import view.ai.AIFrame;
import view.bgm.BGMFrame;
import view.level.LevelFrame;

import static view.login.LoginFrame.isDayTheme;

public class LoadLevelFrame extends JFrame {


    private GameController controller;
    private JButton restartBtn;
    private JButton quitBtn;
    private JButton AISloveBtn;
    private JButton upBtn;
    private JButton downBtn;
    private JButton leftBtn;
    private JButton rightBtn;
    private JButton undoBtn;
    private JButton bgmBtn;

    private JLabel stepLabel;
    private GamePanel gamePanel;
    private BufferedImage loadImage;

    public LoadLevelFrame(int width, int height, MapMatrix mapMatrix, String path) throws FileNotFoundException {
        this.setTitle("Load Level "+path);
        this.setLayout(null);
        this.setSize(width, height);
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);

        if (isDayTheme) {
            try {
                loadImage = ImageIO.read(new File("img/level-green.jpg"));
            } catch (IOException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Failed to load day theme image");
                errorFrame.setVisible(true);
            }
        } else {
            try {
                loadImage = ImageIO.read(new File("img/level-night.jpeg"));
            } catch (IOException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Failed to load night theme image");
                errorFrame.setVisible(true);
            }
        }
        view.login.BackgroundPanel backgroundPanel = new view.login.BackgroundPanel(loadImage);
        backgroundPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        layeredPane.add(backgroundPanel, JLayeredPane.FRAME_CONTENT_LAYER);
        gamePanel = new GamePanel(mapMatrix);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2);
        this.add(gamePanel);
        this.controller = new GameController(gamePanel, mapMatrix);
        this.restartBtn = FrameUtil.createButton(layeredPane, "Restart", new Point(gamePanel.getWidth() + 80, 140), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.quitBtn = FrameUtil.createButton(layeredPane, "Quit", new Point(gamePanel.getWidth() + 360, 140), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.undoBtn = FrameUtil.createButton(layeredPane, "Save", new Point(gamePanel.getWidth() + 360, 245), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.upBtn = FrameUtil.createButton(layeredPane, "↑", new Point(gamePanel.getWidth() + 200, 500), 70, 70, JLayeredPane.PALETTE_LAYER);
        this.downBtn = FrameUtil.createButton(layeredPane, "↓", new Point(gamePanel.getWidth() + 200, 570), 70, 70, JLayeredPane.PALETTE_LAYER);
        this. AISloveBtn= FrameUtil.createButton(layeredPane, "AI Solver", new Point(gamePanel.getWidth() + 80, 245), 200, 80, JLayeredPane.PALETTE_LAYER);
        this.leftBtn = FrameUtil.createButton(layeredPane, "←", new Point(gamePanel.getWidth() + 130, 570), 70, 70, JLayeredPane.PALETTE_LAYER);
        this.rightBtn = FrameUtil.createButton(layeredPane, "→", new Point(gamePanel.getWidth() + 270, 570), 70, 70, JLayeredPane.PALETTE_LAYER);
        this.stepLabel = FrameUtil.createJLabel(layeredPane, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 90), 180, 50, JLayeredPane.PALETTE_LAYER);
        this.bgmBtn = FrameUtil.createButton(layeredPane, "BGM Setting", new Point(gamePanel.getWidth() + 360, 360), 200, 80, JLayeredPane.PALETTE_LAYER);
        gamePanel.setStepLabel(stepLabel);
        if(!isDayTheme) {stepLabel.setForeground(Color.white);  }

//        this.restartBtn = FrameUtil.createButton(layeredPane, "Restart", new Point(gamePanel.getWidth() + 80, 120), 80, 50,JLayeredPane.MODAL_LAYER);
//        this.quitBtn = FrameUtil.createButton(layeredPane, "Quit", new Point(gamePanel.getWidth() + 80, 210), 80, 50,JLayeredPane.MODAL_LAYER);
//        this.AISloveBtn = FrameUtil.createButton(layeredPane,"AI Solver", new Point(gamePanel.getWidth() + 80, 300), 80, 50,JLayeredPane.MODAL_LAYER);
//        this.undoBtn = FrameUtil.createButton(layeredPane,"Undo",new Point(gamePanel.getWidth() + 160 , 300), 80, 50,JLayeredPane.MODAL_LAYER);
//        this.upBtn = FrameUtil.createButton(layeredPane,"↑", new Point(gamePanel.getWidth() + 110, 360), 30, 30,JLayeredPane.MODAL_LAYER);
//        this.downBtn = FrameUtil.createButton(layeredPane,"↓", new Point(gamePanel.getWidth() + 110, 390), 30, 30,JLayeredPane.MODAL_LAYER);
//        this.leftBtn = FrameUtil.createButton(layeredPane,"←", new Point(gamePanel.getWidth() + 80, 390), 30, 30,JLayeredPane.MODAL_LAYER);
//        this.rightBtn = FrameUtil.createButton(layeredPane,"→", new Point(gamePanel.getWidth() + 140, 390), 30, 30,JLayeredPane.MODAL_LAYER);
//        this.stepLabel = FrameUtil.createJLabel(layeredPane, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50,JLayeredPane.MODAL_LAYER);
//        this.bgmBtn = FrameUtil.createButton(layeredPane, "BGM Setting", new Point(gamePanel.getWidth() + 360, 540), 200, 80, JLayeredPane.PALETTE_LAYER);
//        gamePanel.setStepLabel(stepLabel);

        this.restartBtn.addActionListener(e -> {
            controller.restartGame(path);
            this.stepLabel.setText("New Start");
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.quitBtn.addActionListener(e -> {
            this.setVisible(false);
            LevelFrame levelFrame = new LevelFrame(500,200);
            levelFrame.setVisible(true);
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.AISloveBtn.addActionListener(e -> {
            gamePanel.requestFocusInWindow();
            AIFrame aiFrame = new AIFrame(600,450, controller.AISolve());
            aiFrame.setVisible(true);
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
        this.undoBtn.addActionListener(e -> {
            controller.undo();
            stepLabel.setText(String.format("Step: %d", gamePanel.getCurrentStep()));
            gamePanel.requestFocusInWindow();
            System.out.println("undo");
        });
        this.bgmBtn.addActionListener(e -> {
            BGMFrame bgmFrame = new BGMFrame(500,200);
            bgmFrame.setVisible(true);
        });

        SwingUtilities.invokeLater(() -> {
            gamePanel.requestFocusInWindow();
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
