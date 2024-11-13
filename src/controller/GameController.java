package controller;

import model.Direction;
import model.MapMatrix;
import view.game.*;

import java.io.*;

import static view.game.AISolver.aiSolve;
import static view.level.LevelFrame.getCurrentLevel;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private GamePanel view;
    private MapMatrix model;
    private LoadLevelFrame loadLevelFrame;

    public GameController(GamePanel view, MapMatrix model) throws FileNotFoundException {
        this.view = view;
        this.model = model;
        view.setController(this);
    }


    public void restartGame() {
        File file = new File("map/Level"+getCurrentLevel()+".txt");
        int[][] map = readArray(file);
        view.removeAll();
        model.setMatrix(map);
        view.initialGame();
    }

    public void restartGame(String path) {
        File file = new File(path);
        int[][] map = readArray(file);
        view.removeAll();
        model.setMatrix(map);
        view.initialGame();
    }

    public void loadGame(String path){

        try {
            File file = new File(path);
            int[][] map = readArray(file);
            model.setMatrix(map);
            loadLevelFrame = new LoadLevelFrame(600,450,model,path);
            loadLevelFrame.setVisible(true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean doMove(int row, int col, Direction direction) {
        GridComponent currentGrid = view.getGridComponent(row, col);
        //target row can column.
        int tRow = row + direction.getRow();
        int tCol = col + direction.getCol();
        int bRow = row + 2*direction.getRow();
        int bCol = col + 2*direction.getCol();
        GridComponent targetGrid = view.getGridComponent(tRow, tCol);
        GridComponent boxGrid = view.getGridComponent(bRow, bCol);
        int[][] map = model.getMatrix();

        if (map[tRow][tCol] == 0 || map[tRow][tCol] == 2) {
            //update hero in MapMatrix
            model.getMatrix()[row][col] -= 20;
            model.getMatrix()[tRow][tCol] += 20;
            //Update hero in GamePanel
            Hero h = currentGrid.removeHeroFromGrid();
            targetGrid.setHeroInGrid(h);
            //Update the row and column attribute in hero
            h.setRow(tRow);
            h.setCol(tCol);
            return true;
        }
        if ((map[tRow][tCol] == 10 || map[tRow][tCol] == 12)&&(map[bRow][bCol] == 0 || map[bRow][bCol] == 2)) {
            //update hero in MapMatrix
            model.getMatrix()[row][col] -= 20;
            model.getMatrix()[tRow][tCol] += 10;
            model.getMatrix()[bRow][bCol] += 10;
            //Update hero in GamePanel
            Hero h = currentGrid.removeHeroFromGrid();
            targetGrid.setHeroInGrid(h);
            h.setRow(tRow);
            h.setCol(tCol);

            Box box = targetGrid.removeBoxFromGrid();
            boxGrid.setBoxInGrid(box);

            //Update the row and column attribute in hero

            return true;
        }
        return false;
    }

    //todo: add other methods such as loadGame, saveGame...

    public void AISolve(){
        System.out.println("Path: "+aiSolve(model.getMatrix()).getPath());
        System.out.println("Length: "+aiSolve(model.getMatrix()).getLength());
    }


    public int countLine(File file) throws IOException {
        int lines = 0;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (br.readLine() != null) {
            lines++;
        }
        br.close();
        return lines;
    }

    public int countCol(File file) throws IOException {
        int rows = 0;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line = br.readLine();
        if (line != null) {
            return line.split(" ").length;
        } else {
            return 0;
        }
    }

    public int[][] readArray(File file){
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int row = 0;
            int numRow = countLine(file);
            int numCol = countCol(file);
            int[][] array = new int[numRow][numCol];
            while ((line = br.readLine())!=null){
                String[] value = line.split(" ");
                for(int col=0;col<value.length;col++){
                    array[row][col] = Integer.parseInt(value[col]);
                }
                row++;
            }
            br.close();
            return array;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean isVictory(){
        for(int i=0;i<model.getHeight();i++){
            for(int j=0;j<model.getWidth();j++){
                if(model.getMatrix()[i][j] == 10){
                    return false;
                }
            }
        }
        return true;
    }

}
