package view.game;

import error.ErrorFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Box extends JComponent {
    private final int value = 10;
    private boolean isOnGoal;

    public Box(int width, int height) {
        this.setSize(width, height);
        this.setLocation(5, 5);
    }

    BufferedImage image1;
    {
        try {
            image1 = ImageIO.read(new File("img/box.png"));
        } catch (IOException e) {
            ErrorFrame error = new ErrorFrame(500,200,"Image not found");
            error.setVisible(true);
        }
    }
    BufferedImage image2;
    {
        try {
            image2 = ImageIO.read(new File("img/boxongoal.png"));
        } catch (IOException e) {
            ErrorFrame error = new ErrorFrame(500,200,"Image not found");
            error.setVisible(true);
        }
    }

    public void paintComponent(Graphics g) {
        if(!isOnGoal) {
            g.drawImage(image1, 0, 0, getWidth(), getHeight(), null);
        }else{
            g.drawImage(image2,0,0,getWidth(),getHeight(),null);
        }
    }

    public int getValue() {
        return value;
    }

    public void setOnGoal(boolean onGoal) {
        isOnGoal = onGoal;
    }
}
