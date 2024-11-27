package view.bgm;

import error.ErrorFrame;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BGMFrame extends JFrame {

    private static FloatControl volumeControl;

    public BGMFrame(int width,int height){
        this.setTitle("BGM Setting");
        this.setSize(width,height);
        this.setLayout(new BorderLayout());

        // 创建 JLabel 显示音量值
        JLabel volumeLabel = new JLabel("Volume: 50%", JLabel.CENTER);
        volumeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        // 创建 JSlider
        JSlider volumeSlider = new JSlider(0, 100, 50);
        JButton okBtn = new JButton("OK");
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);

        // 添加事件监听器
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int volume = volumeSlider.getValue();
                volumeLabel.setText("Volume: " + volume + "%");
                setVolume(volume);
            }
        });

        this.add(volumeLabel, BorderLayout.NORTH);
        this.add(volumeSlider, BorderLayout.CENTER);
        this.add(okBtn, BorderLayout.SOUTH);

        okBtn.addActionListener(e ->{
            this.setVisible(false);
        });

        this.setVisible(true);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private static void setVolume(int volume) {
        if (volumeControl != null) {
            // 将滑块的 0-100 值转换为分贝范围
            float min = volumeControl.getMinimum(); // 通常为 -80.0 dB
            float max = volumeControl.getMaximum(); // 通常为 6.0 dB
            float newVolume = min + (max - min) * (volume / 100.0f);
            volumeControl.setValue(newVolume);
        }
    }

    public static void playMusic(){
        File musicFile = new File("sound/bgm.wav");
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);

            // 获取音量控制器
            if (audioClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                ErrorFrame errorFrame = new ErrorFrame(500,200,"Cannot get the controller");
                errorFrame.setVisible(true);
            }

            // 播放音乐
            audioClip.start();
            audioClip.loop(Clip.LOOP_CONTINUOUSLY); // 循环播放
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        BGMFrame frame = new BGMFrame(500,200);
//        frame.setVisible(true);
//    }
}

