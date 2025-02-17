package view.clock;

import view.game.GameFrame;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ClockFrame extends JFrame {

    public static Timer timer;
    public static int playTimeSecond;

    public ClockFrame(GameFrame gameFrame){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(300, 150);
        this.setLayout(new BorderLayout());
        this.setTitle("time counter");
        this.setFocusable(false);

        JLabel timeLabel = new JLabel("00:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        this.add(timeLabel, BorderLayout.CENTER);
        // 初始化计时变量
        final int[] elapsedSeconds = {0};
        // 每秒更新一次时间
        timer = new Timer(1000, e -> {
            elapsedSeconds[0]++;
            int minutes = (elapsedSeconds[0] % 3600) / 60;
            int seconds = elapsedSeconds[0] % 60;
            playTimeSecond = elapsedSeconds[0];
            timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
        });
        // 启动定时器
        timer.start();
    }

    public void stopTimer(){
        timer.stop();
        this.setVisible(false);
    }
}
