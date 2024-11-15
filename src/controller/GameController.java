package controller;

import model.Direction;
import model.MapMatrix;
import view.game.*;
import view.lose.LoseFrame;
import view.win.WinFrame;

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
    private WinFrame winFrame;
    private LoseFrame loseFrame;

    public GameController(GamePanel view, MapMatrix model) throws FileNotFoundException {
        this.view = view;
        this.model = model;
        view.setController(this);
    }


    public void restartGame() {//重新开始（关卡模式）
        File file = new File("map/Level"+getCurrentLevel()+".txt");//从同关卡的初始文件中覆盖当前的MapMatrix
        int[][] map = readArray(file);
        view.removeAll();
        model.setMatrix(map);
        view.initialGame();
    }

    public void restartGame(String path) {//重新开始（自定义模式）
        File file = new File(path);//从path中加载关卡文件
        int[][] map = readArray(file);
        view.removeAll();
        model.setMatrix(map);
        view.initialGame();
    }

    public void loadGame(String path){//加载自定义关卡

        try {
            File file = new File(path);//载入文件
            int[][] map = readArray(file);
            model.setMatrix(map);
            loadLevelFrame = new LoadLevelFrame(600,450,model,path);//创建新loadLevel窗口
            loadLevelFrame.setVisible(true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean doMove(int row, int col, Direction direction) {//判断是否可以移动，如果可以，就移动，同时判断是否获胜或者已经失败
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

            model.getMatrix()[row][col] -= 20;
            model.getMatrix()[tRow][tCol] += 10;
            model.getMatrix()[bRow][bCol] += 10;

            Hero h = currentGrid.removeHeroFromGrid();
            targetGrid.setHeroInGrid(h);
            h.setRow(tRow);
            h.setCol(tCol);

            Box box = targetGrid.removeBoxFromGrid();
            boxGrid.setBoxInGrid(box);

            try {
                if (isWin()) {
                    winFrame = new WinFrame(600, 200, getCurrentLevel());
                    winFrame.setVisible(true);
                }
                if (isLose() && !isWin()) {
                    loseFrame = new LoseFrame(600, 200, getCurrentLevel());
                    loseFrame.setVisible(true);
                }
            }catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            return true;
        }

        return false;
    }

    //todo: add other methods such as loadGame, saveGame...

    public AIResult AISolve(){//调用aiSolve方法生成最佳路径
        return aiSolve(model.getMatrix());
    }


    public int countLine(File file) throws IOException {//辅助方法：数行数
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

    public int countCol(File file) throws IOException {//辅助方法：数列数
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

    public int[][] readArray(File file){//辅助方法：从关卡文件中读取二维数组
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

    public boolean isWin() throws FileNotFoundException {//辅助方法：判定是否胜利
        int[][] grid = model.getMatrix();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == 10){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isLose(){//辅助方法：判断是否失败
        int[][] grid = model.getMatrix();
        int sum = 0;
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 1; i < model.getHeight()-1; i++) {
            for (int j = 1; j < model.getWidth()-1; j++) {
                if(grid[i][j] == 10 || grid[i][j] == 12){//检测是否可以前往上下左右中的任意方向。
                    // 可以被推动需要同时满足两个条件：一侧有空位或者玩家或者目标点并且对侧是空位或者目标点或者玩家
                    sum1++;
                    System.out.println(i+" "+j);
                    if((grid[i-1][j] == 0 || grid[i-1][j]/10 == 2 || grid[i-1][j] == 2) && (grid[i+1][j] == 0 || grid[i+1][j]/10 == 2 || grid[i+1][j] == 2)){
                        sum++;
//                        System.out.println(true);
                    }
//                    if((grid[i+1][j] == 0 || grid[i+1][j]/10 == 2 || grid[i+1][j] == 2) && (grid[i-1][j] == 0 || grid[i-1][j]/10 == 2) || grid[i-1][j] == 2){
//                        sum++;
//                        System.out.println(true);
//                    }
                    if((grid[i][j-1] == 0 || grid[i][j-1]/10 == 2 || grid[i][j-1] == 2) && (grid[i][j+1] == 0 || grid[i][j+1]/10 == 2 || grid[i][j+1] == 2)){
                        sum++;
//                        System.out.println(true);
                    }
//                    if((grid[i][j+1] == 0 || grid[i][j+1]/10 == 2 || grid[i][j+1] == 2) && (grid[i][j-1] == 0 || grid[i][j-1]/10 == 2)){
//                        sum++;
//                        System.out.println(true);
//                    }
                    if(sum == 0){
                        sum2++;
                    }
                    sum = 0;
                }
            }
        }
        if(sum1 == sum2){
            return true;
        }
        return false;


    }

}
