package view.replay;

import error.ErrorFrame;
import model.MapMatrix;
import view.FrameUtil;
import view.game.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static view.login.LoginFrame.isDayTheme;

public class ReplayFrame extends JFrame {

    private GamePanel gamePanel;
    private MapMatrix mapMatrix;
    private BufferedImage loadImage;
    private Stack<int[][]> track;

    public ReplayFrame() throws IOException, InterruptedException {
        this.setTitle("Replay");
        this.setLayout(null);
        this.setSize(1500,900);

        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);

        if (isDayTheme) {
            try {
                loadImage = ImageIO.read(new File("img/level-green.jpg"));
            } catch (IOException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Failed to load day theme image");
                errorFrame.setVisible(true);
            }
        } else {
            try {
                loadImage = ImageIO.read(new File("img/level-night.jpeg"));
            } catch (IOException e) {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Failed to load night theme image");
                errorFrame.setVisible(true);
            }
        }

        // 创建背景面板
        view.login.BackgroundPanel backgroundPanel = new view.login.BackgroundPanel(loadImage);
        backgroundPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        layeredPane.add(backgroundPanel, JLayeredPane.FRAME_CONTENT_LAYER);



        // 创建游戏面板
        track = readReplay("Replay/demo.txt");
        mapMatrix = new MapMatrix(track.getFirst());
        gamePanel = new GamePanel(mapMatrix);
        gamePanel.setVisible(true);
        gamePanel.setLocation(0, 0);
        JLabel stepLabel = FrameUtil.createJLabel(layeredPane, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50, JLayeredPane.PALETTE_LAYER);
        gamePanel.setStepLabel(stepLabel);
        layeredPane.add(gamePanel, JLayeredPane.MODAL_LAYER);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        AtomicInteger i= new AtomicInteger(1);

        JButton leftBtn = FrameUtil.createButton(layeredPane, "←", new Point(gamePanel.getWidth() + 50, 520), 70, 70, JLayeredPane.PALETTE_LAYER);
        JButton rightBtn = FrameUtil.createButton(layeredPane, "→", new Point(gamePanel.getWidth() + 190, 520), 70, 70, JLayeredPane.PALETTE_LAYER);

        leftBtn.addActionListener(e -> {
            i.getAndDecrement();
            update(i.get());
        });
        rightBtn.addActionListener(e -> {
            i.getAndIncrement();
            update(i.get());
        });
    }

    public static void writeReplay(Stack<int[][]> stack, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        // 遍历栈中的每个二维数组
        for (int[][] array : stack) {
            writer.write(array.length + " " + array[0].length);  // 写入二维数组的行列数
            writer.newLine();

            // 写入二维数组的每个元素
            for (int[] row : array) {
                for (int value : row) {
                    writer.write(value + " ");
                }
                writer.newLine();
            }
        }
        writer.close();
    }

    // 从文件读取并恢复Stack<int[][]>
    public static Stack<int[][]> readReplay(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        Stack<int[][]> stack = new Stack<>();

        String line;
        while ((line = reader.readLine()) != null) {
            // 读取二维数组的行列数
            String[] dims = line.split(" ");
            int rows = Integer.parseInt(dims[0]);
            int cols = Integer.parseInt(dims[1]);

            int[][] array = new int[rows][cols];

            // 读取二维数组的元素
            for (int i = 0; i < rows; i++) {
                line = reader.readLine();
                String[] values = line.split(" ");
                for (int j = 0; j < cols; j++) {
                    array[i][j] = Integer.parseInt(values[j]);
                }
            }
            stack.push(array);  // 将数组推入栈
        }
        reader.close();
        return stack;

    }

    public static void main(String[] args) {
        try {
            ReplayFrame replayFrame = new ReplayFrame();
            replayFrame.setVisible(true);
            replayFrame.update(2);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int i){
        gamePanel.removeAll();
        mapMatrix.setMatrix(track.get(i));
        gamePanel.initialGame();
    }
}
