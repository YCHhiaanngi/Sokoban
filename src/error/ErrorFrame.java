package error;

import view.FrameUtil;

import javax.swing.*;
import java.awt.*;

public class ErrorFrame extends JFrame{
    private JLabel label;
    private JButton okBtn;

    public ErrorFrame(int width, int height, String message){
        this.setTitle("Error");
        this.setLayout(null);
        this.setSize(width, height);

        //todo:调整窗口、标签的大小

        this.label = FrameUtil.createJLabel(this, new Point(width / 2, height / 2), 500, 50, "ERROR: " + message);
        this.okBtn = FrameUtil.createButton(this, "OK", new Point(width / 2 + 50, height / 2 + 50), 50, 50);

        okBtn.addActionListener(e -> {
            this.setVisible(false);
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

}
