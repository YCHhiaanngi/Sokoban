package view.game;

import error.ErrorFrame;
import model.Direction;
import view.login.LoginFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Hero extends JComponent {
    private int row;
    private int col;
    public static Direction heroDirection;

    private final int value = 20;
    private boolean isWin;
    private boolean isLose;

    public Hero(int width, int height, int row, int col) {
        this.row = row;
        this.col = col;
        this.setSize(width, height);
        this.setLocation(8, 8);
        this.setIsWin(false);
        this.setIsLose(false);
    }

    BufferedImage[] image = new BufferedImage[6];
    {if(LoginFrame.isDayTheme){
        try {
            image[0] = ImageIO.read(new File("img/man-back.png"));
            image[1] = ImageIO.read(new File("img/man-front.png"));
            image[2] = ImageIO.read(new File("img/man-left.png"));
            image[3] = ImageIO.read(new File("img/man-right.png"));
            image[4] = ImageIO.read(new File("img/man-win.png"));
            image[5] = ImageIO.read(new File("img/man-lose.png"));
        } catch (IOException e) {
            ErrorFrame errorFrame = new ErrorFrame(500, 200, "The man's image cannot be loaded");
            errorFrame.setVisible(true);
        }
    }
        else {
        try {
            image[0] = ImageIO.read(new File("img/hero-back.png"));
            image[1] = ImageIO.read(new File("img/hero-front.png"));
            image[2] = ImageIO.read(new File("img/hero-left.png"));
            image[3] = ImageIO.read(new File("img/hero-right.png"));
            image[4] = ImageIO.read(new File("img/hero-win.png"));
            image[5] = ImageIO.read(new File("img/hero-lose.png"));
        } catch (IOException e) {
            ErrorFrame errorFrame = new ErrorFrame(500, 200, "The man's image cannot be loaded");
            errorFrame.setVisible(true);
        }
    }
    }

    public void paintComponent(Graphics g) {
        if(!isWin && !isLose) {
            switch (heroDirection) {
                case Direction.UP:
                    g.drawImage(image[0], 0, 0, getWidth(), getHeight(), null);
                    break;
                case Direction.DOWN:
                    g.drawImage(image[1], 0, 0, getWidth(), getHeight(), null);
                    break;
                case Direction.LEFT:
                    g.drawImage(image[2], 0, 0, getWidth(), getHeight(), null);
                    break;
                case Direction.RIGHT:
                    g.drawImage(image[3], 0, 0, getWidth(), getHeight(), null);
                    break;
                case null:
                    g.drawImage(image[1], 0, 0, getWidth(), getHeight(), null);
                    break;
            }
        }
        if(isWin){
            g.drawImage(image[4], 0, 0, getWidth(), getHeight(), null);
        }
        if(isLose){
            g.drawImage(image[5], 0, 0, getWidth(), getHeight(), null);
        }
    }

    public int getValue() {
        return value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setIsWin(boolean isWin){
        this.isWin = isWin;
    }
    public void setIsLose(boolean isLose){
        this.isLose = isLose;
    }
}
