package view.level;

import error.ErrorFrame;
import model.MapMatrix;
import view.FrameUtil;
import view.game.GameFrame;

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
        this.setSize(width, height);
    //todo:定义一个boolean，if（green）则level-green，else level-night
//        if(isDayTheme) {
//            try {
//                levelImage = ImageIO.read(new File("img/level-green.jpg"));
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to load  level image", e);
//            }
//        }else{
//            try {
//                levelImage = ImageIO.read(new File("img/level-night.jpg"));
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to load  level image", e);
//            }
//        }
        view.login.BackgroundPanel backgroundPanel = new view.login.BackgroundPanel(levelImage);
        this.setContentPane(backgroundPanel);
        this.setLayout(null);

        JButton level1Btn = FrameUtil.createButton(this, "Level1", new Point(30, height / 2 - 50), 60, 60);
        JButton level2Btn = FrameUtil.createButton(this, "Level2", new Point(120, height / 2 - 50), 60, 60);
        JButton level3Btn = FrameUtil.createButton(this, "Level3", new Point(210, height / 2 - 50), 60, 60);
        JButton level4Btn = FrameUtil.createButton(this, "Level4", new Point(300, height / 2 - 50), 60, 60);
        JButton level5Btn = FrameUtil.createButton(this, "Level5", new Point(390, height / 2 - 50), 60, 60);
        level1Btn.addActionListener(l->{
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
