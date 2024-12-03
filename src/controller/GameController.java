package controller;

import error.ErrorFrame;
import model.Direction;
import model.MapMatrix;
import view.game.*;
import view.lose.LoseFrame;
import view.win.WinFrame;

import javax.sound.sampled.Clip;
import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static view.game.AISolver.aiSolve;
import static view.game.Hero.heroDirection;
import static view.level.LevelFrame.getCurrentLevel;
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

    private Stack<int[][]> track;//后进先出，用栈存储游戏记录以实现撤销功能

    private static ScheduledExecutorService scheduler;
    private static Runnable task;

    private static Clip clip;

    public GameController(GamePanel view, MapMatrix model) throws FileNotFoundException {
        this.view = view;
        this.model = model;
        this.track = new Stack<>();
        int[][] grid = deepCopy(model.getMatrix());
        track.push(grid);
        view.setController(this);
    }

    // 启动定时任务
    public void autoSave(long intervalSeconds) {
        scheduler = Executors.newSingleThreadScheduledExecutor();  // 创建单线程调度任务池
        task = new Runnable() {
            @Override
            public void run() {
                saveGame();
            }
        };
        // 每隔intervalSeconds秒执行一次任务
        scheduler.scheduleAtFixedRate(task, 0, intervalSeconds, TimeUnit.SECONDS);
    }

    // 停止定时任务
    public static void stopAutoSave() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();  // 停止调度器
            System.out.println("定时任务已停止");
        }
    }

    public void restartGame() {//重新开始（关卡模式）
        File file = new File("map/Level"+getCurrentLevel()+".txt");//从同关卡的初始文件中覆盖当前的MapMatrix
        int[][] map = null;
        map = readArray(file,0);
        view.removeAll();
        model.setMatrix(map);
        view.initialGame();
        track.clear();
        int[][] grid = deepCopy(model.getMatrix());
        track.push(grid);
        heroDirection = Direction.DOWN;
    }

    public void restartGame(String path) {//重新开始（自定义模式）
        File file = new File(path);//从path中加载关卡文件
        int[][] map = null;
        map = readArray(file,0);
        view.removeAll();
        model.setMatrix(map);
        view.initialGame();
        track.clear();
        int[][] grid = deepCopy(model.getMatrix());
        track.push(grid);
        heroDirection = Direction.DOWN;
    }

    public void loadGame(String path) throws FileNotFoundException{//加载自定义关卡
        File file = new File(path);//载入文件
        int[][] map = readArray(file,0);
        model.setMatrix(map);
        loadLevelFrame = new LoadLevelFrame(2000,1000,model,path);//创建新loadLevel窗口
        loadLevelFrame.setVisible(true);
        stopAutoSave();//停止自动保存
        heroDirection = Direction.DOWN;
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
            sb.append(view.getCurrentStep()).append("\n");
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
            ErrorFrame errorFrame = new ErrorFrame(500,200,"Failed to save");
            errorFrame.setVisible(true);
        }
    }

    public void loadProgress(){
        String userName = getUserName();
        String path = "userfile/" + userName + ".txt";
        File file = new File(path);
        int currentStep;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            int[][] map = null;
            br.readLine();//第一行是密码，不要读
            int previousLevel = Integer.parseInt(br.readLine());//第二行是上次保存时的关卡
            if(getCurrentLevel() == previousLevel){
                currentStep = Integer.parseInt(br.readLine());//第三行是上一次保存时的步数
                map = readArray(file,4);//第4行开始读取关卡数据
                System.out.println("Previous progress loaded.");
                view.removeAll();
                model.setMatrix(map);
                view.initialGame();
                view.setCurrentStep(currentStep);
                heroDirection = Direction.DOWN;
            }else {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"You don't have any progress in this level.");
                errorFrame.setVisible(true);
            }

            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void undo(){
        //重置地图
        if(track.size() == 1){
            ErrorFrame errorFrame = new ErrorFrame(500,200,"empty");
        }else {
            view.removeAll();
            int[][] grid = track.pop();
            model.setMatrix(grid);
//            for (int i = 0; i < grid.length; i++) {
//                for (int j = 0; j < grid[0].length; j++) {
//                    System.out.print(grid[i][j] + " ");
//                }
//                System.out.println();
//            }
            int step = view.getCurrentStep();//因为initialGame()方法会将step重置为0，所以要将当前步骤事先存储起来
            view.initialGame();
            view.setCurrentStep(step-1);
        }
    }

    private int[][] deepCopy(int[][] original) {//辅助方法：深度复制二维数组的值
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }


    public boolean doMove(int row, int col, Direction direction) {//判断是否可以移动，如果可以，就移动，同时判断是否获胜或者已经失败
        GridComponent currentGrid = view.getGridComponent(row, col);
        //target row can column.
        int tRow = row + direction.getRow();
        int tCol = col + direction.getCol();
        int bRow = row + 2*direction.getRow();
        int bCol = col + 2*direction.getCol();
        GridComponent targetGrid = view.getGridComponent(tRow, tCol);
        GridComponent boxGrid;
        if(bRow>=0 && bRow< model.getHeight() && bCol>=0 && bCol < model.getWidth()) {
            boxGrid = view.getGridComponent(bRow, bCol);
        }else{
            boxGrid = null;
        }
        int[][] map = model.getMatrix();
        int[][] grid = deepCopy(map);
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
            //先复制，再将地图数据压入栈
            track.push(grid);

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

            if(model.getMatrix()[bRow][bCol] == 12){
                box.setOnGoal(true);
            }
            if(model.getMatrix()[bRow][bCol] == 10){
                box.setOnGoal(false);
            }

            track.push(grid);

            if (isWin()) {
                h.setIsWin(true);
                winFrame = new WinFrame(600, 200, getCurrentLevel());
                winFrame.setVisible(true);
                double time = 0;
                PlayerData data = new PlayerData(time,getUserName(), view.getCurrentStep());
                try {
                    data.updateData();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (isLose() && !isWin()) {
                h.setIsLose(true);
                loseFrame = new LoseFrame(600, 200, getCurrentLevel());
                loseFrame.setVisible(true);
            }

            return true;
        }

        return false;
    }

    //todo: add other methods such as loadGame, saveGame...

    public AIResult AISolve(){//调用aiSolve方法生成最佳路径
        return aiSolve(model.getMatrix());
    }

    public static int[][] readArray(File file, int from)  {
        try {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWin(){//辅助方法：判定是否胜利
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
