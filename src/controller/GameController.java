package controller;

import model.Direction;
import model.MapMatrix;
import view.game.*;
import view.lose.LoseFrame;
import view.win.WinFrame;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static view.game.AISolver.aiSolve;
import static view.level.LevelFrame.getCurrentLevel;
import static view.level.LevelFrame.setCurrentLevel;
import static view.login.LoginFrame.getUserName;
import static view.login.LoginFrame.getPassword;

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
        int[][] map = null;
        try {
            map = readArray(file,0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view.removeAll();
        model.setMatrix(map);
        view.initialGame();
    }

    public void restartGame(String path) {//重新开始（自定义模式）
        File file = new File(path);//从path中加载关卡文件
        int[][] map = null;
        try {
            map = readArray(file,0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view.removeAll();
        model.setMatrix(map);
        view.initialGame();
    }

    public void loadGame(String path){//加载自定义关卡

        try {
            File file = new File(path);//载入文件
            int[][] map = readArray(file,0);
            model.setMatrix(map);
            loadLevelFrame = new LoadLevelFrame(600,450,model,path);//创建新loadLevel窗口
            loadLevelFrame.setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void saveGame(){
        String userName = getUserName();
        String path = "userfile/" + userName + ".txt";
        File file = new File(path);
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            StringBuilder sb = new StringBuilder();
            sb.append(getPassword()).append("\n");
            System.out.println(getUserName());
            sb.append(getCurrentLevel()).append("\n");
            for (int i = 0; i < model.getHeight(); i++) {
                for (int j = 0; j < model.getWidth()-1; j++) {
                    sb.append(model.getMatrix()[i][j]).append(" ");
                }
                sb.append(model.getMatrix()[i][model.getWidth() - 1]).append("\n");
            }
            bw.write("");
            bw.write(sb.toString());
            System.out.println(sb);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadProgress(){
        String userName = getUserName();
        String path = "userfile/" + userName + ".txt";
        File file = new File(path);
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            int[][] map = null;
            br.readLine();//第一行是密码，不要读
            int previousLevel = Integer.parseInt(br.readLine());//第二行是上次保存时的关卡
            if(getCurrentLevel() == previousLevel){
                map = readArray(file,3);//第3行开始读取关卡数据
                System.out.println("Previous progress loaded.");
            }else {
                System.out.println("You don't have any progress in this level.");
            }
            if(map!=null) {
                view.removeAll();
                model.setMatrix(map);
                view.initialGame();
            }
            br.close();
        } catch (IOException e) {
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

    public static int[][] readArray(File file, int from) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<int[]> matrixList = new ArrayList<>();
        int currentLine = 0;

        // 跳过前from行
        while (currentLine < from - 1 && (line = reader.readLine()) != null) {
            currentLine++;
        }

        // 开始读取
        while ((line = reader.readLine()) != null) {
            currentLine++;
            // 分割并转化为int
            String[] values = line.trim().split("\\s+");
            int[] row = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                row[i] = Integer.parseInt(values[i]);
            }
            matrixList.add(row);
        }

        // 将List转化为二维数组
        int[][] matrix = new int[matrixList.size()][];
        for (int i = 0; i < matrixList.size(); i++) {
            matrix[i] = matrixList.get(i);
        }

        // Close the BufferedReader
        reader.close();

        return matrix;
    }

//    public int countLine(File file) throws IOException {//辅助方法：数行数
//        int lines = 0;
//        BufferedReader br;
//        try {
//            br = new BufferedReader(new FileReader(file));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        while (br.readLine() != null) {
//            lines++;
//        }
//        br.close();
//        return lines;
//    }
//
//    public int countCol(File file) throws IOException {//辅助方法：数列数
//        int rows = 0;
//        BufferedReader br;
//        try {
//            br = new BufferedReader(new FileReader(file));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        String line = br.readLine();
//        if (line != null) {
//            return line.split(" ").length;
//        } else {
//            return 0;
//        }
//    }
//
//    public int[][] readArray(File file, int from){//辅助方法：从文件中读取二维数组
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String line;
//            int row = 0;
//            int numRow = countLine(file);
//            int numCol = countCol(file);
//            int[][] array = new int[numRow][numCol];
//            for (int i = 0; i < from; i++) {
//                br.readLine();
//            }
//            while ((line = br.readLine())!=null){
//                String[] value = line.split(" ");
//                for(int col=0;col<value.length;col++){
//                    array[row][col] = Integer.parseInt(value[col]);
//                }
//                row++;
//            }
//            br.close();
//            return array;
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

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
        for (int i = 1; i < model.getHeight()-1; i++) {
            for (int j = 1; j < model.getWidth()-1; j++) {
                if(grid[i][j] == 10 || grid[i][j] == 12){//检测是否可以前往上下左右中的任意方向。
                    // 可以被推动需要同时满足两个条件：一侧有空位或者玩家或者目标点并且对侧是空位或者目标点或者玩家
//                    System.out.println(i+" "+j);
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
                    if(sum != 0){//推得动
                        return false;
                    }
                }
            }
        }
        return true;


    }

}
