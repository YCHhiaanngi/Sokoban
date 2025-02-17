package view.game;

import error.ErrorFrame;
import view.login.LoginFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class GridComponent extends JComponent {
    private int row;
    private int col;
    private final int id; // represents the units digit value. It cannot be changed during one game.

    private Hero hero;
    private Box box;
    static Color color = new Color(0,0,0);

    public GridComponent(int row, int col, int id, int gridSize) {
        this.setSize(gridSize, gridSize);
        this.row = row;
        this.col = col;
        this.id = id;
    }

    BufferedImage[] image = new BufferedImage[3];

    {
        if(LoginFrame.isDayTheme) {
            try {
                image[0] = ImageIO.read(new File("img/empty.png"));
                image[1] = ImageIO.read(new File("img/barrier.png"));
                image[2] = ImageIO.read(new File("img/goal.png"));

            } catch (IOException e) {
                ErrorFrame error = new ErrorFrame(500,200,"Image not found");
                error.setVisible(true);
            }
        }else{
            try {
                image[0] = ImageIO.read(new File("img/empty-night.png"));
                image[1] = ImageIO.read(new File("img/barrier-night.png"));
                image[2] = ImageIO.read(new File("img/goal-night.png"));

            } catch (IOException e) {
                ErrorFrame error = new ErrorFrame(500,200,"Image not found");
                error.setVisible(true);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.printComponents(g);
        Color borderColor = color;
        switch (id % 10) {
            case 1:
                g.drawImage(image[1],0,0,getWidth(),getHeight(),null);
                break;
            case 0:
                g.drawImage(image[0],0,0,getWidth(),getHeight(),null);
                break;
            case 2:
                g.drawImage(image[2],0,0,getWidth(),getHeight(),null);
                break;
        }
        Border border = BorderFactory.createLineBorder(borderColor, 1);
        this.setBorder(border);
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

    public int getId() {
        return id;
    }

    //When adding a hero in this grid, invoking this method.
    public void setHeroInGrid(Hero hero) {
        this.hero = hero;
        this.add(hero);
    }

    //When adding a box in this grid, invoking this method.
    public void setBoxInGrid(Box box) {
        this.box = box;
        this.add(box);
    }
    //When removing hero from this grid, invoking this method
    public Hero removeHeroFromGrid() {
        this.remove(this.hero);//remove hero component from grid component
        Hero h = this.hero;
        this.hero = null;//set the hero attribute to null
        this.revalidate();//Update component painting in real time
        this.repaint();
        return h;
    }
    //When removing box from this grid, invoking this method
    public Box removeBoxFromGrid() {
        this.remove(this.box);//remove box component from grid component
        Box b = this.box;
//        this.box = null;//set the hero attribute to null
        this.revalidate();//Update component painting in real time
        this.repaint();
        return b;
    }
}
