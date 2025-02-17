package view.level;

import error.ErrorFrame;
import model.MapMatrix;
import view.CircularButton;
import view.FrameUtil;
import view.game.GameFrame;
import view.game.RankFrame;
import view.replay.ReplayFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static view.login.LoginFrame.isDayTheme;

public class LevelFrame extends JFrame {

    private BufferedImage levelImage;
    private static int currentLevel;

    public LevelFrame(int width, int height) {
        this.setTitle("Select Level");
        this.setLayout(null);
        this.setSize(2000, 5000);
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);
        if(isDayTheme) {
            try {
                levelImage = ImageIO.read(new File("img/level-green.jpg"));
            } catch (IOException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Failed to load day theme image");
                errorFrame.setVisible(true);
            }
        }else{
            try {
                levelImage = ImageIO.read(new File("img/level-night.jpeg"));
            } catch (IOException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Failed to load night theme image");
                errorFrame.setVisible(true);
            }
        }
        view.login.BackgroundPanel backgroundPanel = new view.login.BackgroundPanel(levelImage);
        backgroundPanel.setBounds(0, 0, 1800,900 );
        layeredPane.add(backgroundPanel, JLayeredPane.FRAME_CONTENT_LAYER);

        JButton level1Btn = FrameUtil.createButton(layeredPane, "Level1", new Point(150, height / 2 - 50), 600, 100,JLayeredPane.MODAL_LAYER);
        JButton level2Btn = FrameUtil.createButton(layeredPane, "Level2", new Point(150, height / 2 - 50+150), 600, 100,JLayeredPane.MODAL_LAYER);
        JButton level3Btn = FrameUtil.createButton(layeredPane, "Level3", new Point(150, height / 2 - 50+300), 600, 100,JLayeredPane.MODAL_LAYER);
        JButton level4Btn = FrameUtil.createButton(layeredPane, "Level4", new Point(150, height / 2 - 50+450), 600, 100,JLayeredPane.MODAL_LAYER);
        JButton level5Btn = FrameUtil.createButton(layeredPane, "Level5", new Point(150, height / 2 - 50+600), 600, 100,JLayeredPane.MODAL_LAYER);
        // 创建 CircularButton 按钮
        CircularButton RankBtn = new CircularButton("Ranking", 100); // 假设半径为 100
        CircularButton ReplayBtn = new CircularButton("Replay",100);

// 设置按钮的位置和层级
        RankBtn.setBounds(1000, 150, (int)(100 * 2), (int)(100 * 2)); // X, Y, width, height
        ReplayBtn.setBounds(1000, 550, (int)(100 * 2), (int)(100 * 2));

        JLayeredPane layeredPanecirc = new JLayeredPane();
        layeredPane.add(RankBtn, JLayeredPane.MODAL_LAYER);
        layeredPane.add(ReplayBtn, JLayeredPane.MODAL_LAYER);

        level1Btn.addActionListener(l->{
            System.out.println("Level1 button clicked");
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {1, 1, 1, 1, 1, 1},
                    {1, 20, 0, 0, 0, 1},
                    {1, 0, 0, 10, 2, 1},
                    {1, 0, 2, 10, 0, 1},
                    {1, 1, 1, 1, 1, 1},
            });
            currentLevel=1;
            GameFrame gameFrame = null;
            try {
                gameFrame = new GameFrame(600, 450, mapMatrix);
            } catch (FileNotFoundException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Game frame cannot be loaded");
                errorFrame.setVisible(true);
            }
            this.setVisible(false);
            if (gameFrame != null) {
                gameFrame.setVisible(true);
            }
        });

        level2Btn.addActionListener(l->{
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {1,1,1,1,1,1,0},
                    {1,20,0,0,0,1,1},
                    {1,0,10,10,0,0,1},
                    {1,0,1,2,0,2,1},
                    {1,0,0,0,0,0,1},
                    {1,1,1,1,1,1,1},
            });
            currentLevel = 2;
            GameFrame gameFrame = null;
            try {
                gameFrame = new GameFrame(600, 450, mapMatrix);
            } catch (FileNotFoundException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Game frame cannot be loaded");
                errorFrame.setVisible(true);
            }
            this.setVisible(false);
            if (gameFrame != null) {
                gameFrame.setVisible(true);
            }
        });

        level3Btn.addActionListener(l->{
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {0,0,1,1,1,1,0},
                    {1,1,1,0,0,1,0},
                    {1,20,0,2,10,1,1},
                    {1,0,0,0,10,0,1},
                    {1,0,1,2,0,0,1},
                    {1,0,0,0,0,0,1},
                    {1,1,1,1,1,1,1},
            });
            currentLevel =3;
            GameFrame gameFrame = null;
            try {
                gameFrame = new GameFrame(600, 450, mapMatrix);
            } catch (FileNotFoundException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Game frame cannot be loaded");
                errorFrame.setVisible(true);
            }
            this.setVisible(false);
            if (gameFrame != null) {
                gameFrame.setVisible(true);
            }
        });

        level4Btn.addActionListener(l->{
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {0,1,1,1,1,1,0},
                    {1,1,20,0,0,1,1},
                    {1,0,0,1,0,0,1},
                    {1,0,10,12,10,0,1},
                    {1,0,0,2,0,0,1},
                    {1,1,0,2,0,1,1},
                    {0,1,1,1,1,1,0},
            });
            currentLevel = 4;
            GameFrame gameFrame = null;
            try {
                gameFrame = new GameFrame(600, 450, mapMatrix);
            } catch (FileNotFoundException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Game frame cannot be loaded");
                errorFrame.setVisible(true);
            }
            this.setVisible(false);
            if (gameFrame != null) {
                gameFrame.setVisible(true);
            }
        });

        level5Btn.addActionListener(l->{
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {1,1,1,1,1,1,0,0},
                    {1,0,0,0,0,1,1,1},
                    {1,0,0,0,2,2,0,1},
                    {1,0,10,10,10,20,0,1},
                    {1,0,0,1,0,2,0,1},
                    {1,1,1,1,1,1,1,1},
            });
            currentLevel = 5;
            GameFrame gameFrame = null;
            try {
                gameFrame = new GameFrame(600, 450, mapMatrix);
            } catch (FileNotFoundException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Game frame cannot be loaded");
                errorFrame.setVisible(true);
            }
            this.setVisible(false);
            if (gameFrame != null) {
                gameFrame.setVisible(true);
            }
        });

        RankBtn.addActionListener(e -> {
            RankFrame rankFrame = new RankFrame();
            rankFrame.setVisible(true);
        });

        ReplayBtn.addActionListener(e -> {
            try {
                ReplayFrame replayFrame = new ReplayFrame();
                replayFrame.setVisible(true);
                replayFrame.update(1);
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static int getCurrentLevel() {
        return currentLevel;
    }

    public static void setCurrentLevel(int currentLevel) {
        LevelFrame.currentLevel = currentLevel;
    }
}
