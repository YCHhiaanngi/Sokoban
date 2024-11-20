package view.ai;

import view.FrameUtil;
import view.game.AIResult;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class AIFrame extends JFrame {

    private JLabel pathLabel;
    private JLabel lengthLabel;

    private JButton closeBtn;

    public AIFrame(int width, int height, AIResult result) throws FileNotFoundException {
        this.setTitle("AI Result");
        this.setLayout(null);
        this.setSize(400, 300);
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);
        this.pathLabel = FrameUtil.createJLabel(layeredPane, new Point(80, height / 2 - 200), 500, 50, "Path:" + result.getPath(),JLayeredPane.MODAL_LAYER);
        this.lengthLabel = FrameUtil.createJLabel(layeredPane, new Point(80, height / 2-150), 500, 50, "Length:" + result.getLength(),JLayeredPane.MODAL_LAYER);
        if(result.getLength() == -1){
            pathLabel.setText("1.No Valid Path. Please try again");
            lengthLabel.setText("2.Please make sure you are not on the goal grid");
        }
        this.closeBtn = FrameUtil.createButton(layeredPane, "Close", new Point(140, height / 2 -80), 100, 50,JLayeredPane.MODAL_LAYER);

        closeBtn.addActionListener(e -> {
            this.setVisible(false);
        });
    }
}
