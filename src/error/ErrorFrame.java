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
        this.setSize(400, 300);
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);


        this.label = FrameUtil.createJLabel(layeredPane, new Point(100, height / 2-40), 500, 50, "ERROR: " + message,JLayeredPane.MODAL_LAYER);
        this.okBtn = FrameUtil.createButton(layeredPane, "OK", new Point(150, height / 2+10 ), 100, 50,JLayeredPane.MODAL_LAYER);

        okBtn.addActionListener(e -> {
            this.setVisible(false);
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    }

}
