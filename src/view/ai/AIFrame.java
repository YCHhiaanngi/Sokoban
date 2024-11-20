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

    //todo:调整窗口、标签的大小
    public AIFrame(int width, int height, AIResult result) throws FileNotFoundException {
        this.setTitle("AI Result");
        this.setLayout(null);
        this.setSize(width, height);
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);
        this.pathLabel = FrameUtil.createJLabel(layeredPane, new Point(width / 2, height / 2 - 100), 500, 50, "Path:" + result.getPath(),JLayeredPane.MODAL_LAYER);
        this.lengthLabel = FrameUtil.createJLabel(layeredPane, new Point(width / 2, height / 2), 500, 50, "Length:" + result.getLength(),JLayeredPane.MODAL_LAYER);
        if(result.getLength() == -1){
            pathLabel.setText("1.No Valid Path. Please try again");
            lengthLabel.setText("2.Please make sure you are not on the goal grid");
        }
        this.closeBtn = FrameUtil.createButton(layeredPane, "Close", new Point(width / 2 + 50, height / 2 + 50), 100, 50,JLayeredPane.MODAL_LAYER);

        closeBtn.addActionListener(e -> {
            this.setVisible(false);
        });
    }
}
