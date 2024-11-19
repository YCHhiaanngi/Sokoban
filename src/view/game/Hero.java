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

    public Hero(int width, int height, int row, int col) {
        this.row = row;
        this.col = col;
        this.setSize(width, height);
        this.setLocation(8, 8);
    }

    BufferedImage[] image = new BufferedImage[4];
//todo:把图片再画的合适一些，现在有点违和，尤其是hero-right（night的右边）
    {if(LoginFrame.isDayTheme){
        try {
            image[0] = ImageIO.read(new File("img/man-back.jpg"));
            image[1] = ImageIO.read(new File("img/man-front.jpg"));
            image[2] = ImageIO.read(new File("img/man-left.jpg"));
            image[3] = ImageIO.read(new File("img/man-right.jpg"));
        } catch (IOException e) {
            ErrorFrame errorFrame = new ErrorFrame(500, 200, "The man's image cannot be loaded");
            errorFrame.setVisible(true);
        }
    }
        else {
        try {
            image[0] = ImageIO.read(new File("img/hero-front.jpg"));
            image[1] = ImageIO.read(new File("img/hero-back.jpg"));
            image[2] = ImageIO.read(new File("img/hero-left.png"));
            image[3] = ImageIO.read(new File("img/hero-right.jpg"));
        } catch (IOException e) {
            ErrorFrame errorFrame = new ErrorFrame(500, 200, "The man's image cannot be loaded");
            errorFrame.setVisible(true);
        }
    }
    }

    public void paintComponent(Graphics g) {
        switch(heroDirection) {
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
                g.drawImage(image[0], 0, 0, getWidth(), getHeight(), null);
                break;
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
}
