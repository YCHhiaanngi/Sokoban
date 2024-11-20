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
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);

        //todo:调整窗口、标签的大小

        this.label = FrameUtil.createJLabel(layeredPane, new Point(width / 2, height / 2), 500, 50, "ERROR: " + message,JLayeredPane.MODAL_LAYER);
        this.okBtn = FrameUtil.createButton(layeredPane, "OK", new Point(width / 2 + 50, height / 2 + 50), 50, 50,JLayeredPane.MODAL_LAYER);

        okBtn.addActionListener(e -> {
            this.setVisible(false);
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

}
