package view.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Box extends JComponent {
    private final int value = 10;

    public Box(int width, int height) {
        this.setSize(width, height);
        this.setLocation(5, 5);
    }

    BufferedImage image;

    {
        try {
            image = ImageIO.read(new File("img/box.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void paintComponent(Graphics g) {
        g.drawImage(image,0,0,getWidth(),getHeight(),null);
    }

    public int getValue() {
        return value;
    }
}
