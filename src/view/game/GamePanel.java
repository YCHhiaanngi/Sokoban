package view.game;

import controller.GameController;
import model.Direction;
import model.MapMatrix;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * It is the subclass of ListenerPanel, so that it should implement those four methods: do move left, up, down ,right.
 * The class contains a grids, which is the corresponding GUI view of the matrix variable in MapMatrix.
 */
public class GamePanel extends ListenerPanel {

    private GridComponent[][] grids;
    private MapMatrix model;
    private GameController controller;
    private JLabel stepLabel;
    private int steps;
    private final int GRID_SIZE = 50;

    private Hero hero;


    public GamePanel(MapMatrix model) {
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(model.getWidth() * GRID_SIZE + 4, model.getHeight() * GRID_SIZE + 4);
        this.model = model;
        this.grids = new GridComponent[model.getHeight()][model.getWidth()];
        initialGame();

    }


    public void initialGame() {
        this.steps = 0;
        try {
            for (int i = 0; i < grids.length; i++) {
                for (int j = 0; j < grids[i].length; j++) {
                    //Units digit maps to id attribute in GridComponent. (The no change value)
                    grids[i][j] = new GridComponent(i, j, model.getId(i, j) % 10, this.GRID_SIZE);
                    grids[i][j].setLocation(j * GRID_SIZE + 2, i * GRID_SIZE + 2);
                    //Ten digit maps to Box or Hero in corresponding location in the GridComponent. (Changed value)
                    switch (model.getId(i, j) / 10) {
                        case 1:
                            Box box = new Box(GRID_SIZE - 10, GRID_SIZE - 10);
                            if(model.getId(i,j) == 10){
                                box.setOnGoal(false);
                            }
                            if(model.getId(i,j) == 12){
                                box.setOnGoal(true);
                            }
                            grids[i][j].setBoxInGrid(box);
                            break;
                        case 2:
                            this.hero = new Hero(GRID_SIZE - 16, GRID_SIZE - 16, i, j);
                            grids[i][j].setHeroInGrid(hero);
                            break;
                    }
                    this.add(grids[i][j]);
                }
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.print("数组越界");
        }
        this.repaint();
    }

    @Override
    public void doMoveRight() {
        System.out.println("Click VK_RIGHT");
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.RIGHT)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveLeft() {
        System.out.println("Click VK_LEFT");
        if(controller.doMove(hero.getRow(), hero.getCol(), Direction.LEFT)){
            this.afterMove();
        }
    }

    @Override
    public void doMoveUp() {
        System.out.println("Click VK_Up");
       if( controller.doMove(hero.getRow(), hero.getCol(), Direction.UP)){
           this.afterMove();
       }
    }

    @Override
    public void doMoveDown() {
        System.out.println("Click VK_DOWN");
        if(controller.doMove(hero.getRow(), hero.getCol(), Direction.DOWN)){
            this.afterMove();
        }
    }

    public void afterMove() {
        this.steps++;
        this.stepLabel.setText(String.format("Step: %d", this.steps));
//        for(int i=0;i<grids.length;i++){
//            for(int j=0;j<grids[0].length;j++){
//                System.out.print(model.getMatrix()[i][j]+"\t");
//            }
//            System.out.println();
//        }
    }

    public void setStepLabel(JLabel stepLabel) {
        this.stepLabel = stepLabel;
    }

    public int getCurrentStep(){
        return steps;
    }

    public void setCurrentStep(int steps){
        this.steps = steps;
//        System.out.println(this.steps);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public GridComponent getGridComponent(int row, int col) {
        return grids[row][col];
    }
}
