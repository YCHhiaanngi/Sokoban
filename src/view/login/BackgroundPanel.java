package view.login;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;

    public BackgroundPanel(BufferedImage image) {
        this.backgroundImage = image;
        setOpaque(false); // 使背景透明，以便背景图像显示
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // 确保 BackgroundPanel 总是位于最底层
        if (getParent() instanceof JLayeredPane) {
            ((JLayeredPane) getParent()).setLayer(this, JLayeredPane.FRAME_CONTENT_LAYER);
        }
    }
}
