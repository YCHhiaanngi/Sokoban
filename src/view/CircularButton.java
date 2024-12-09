package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * CircularButton is a JButton with a circular shape.
 */
public class CircularButton extends JButton {

    private final double radius;

    public CircularButton(String name, double r) {
        super(name);
        this.radius = r;
        setContentAreaFilled(false); // 确保背景透明，以便自定义绘画
        setBorderPainted(false);     // 隐藏默认边框
        setSize((int)(radius * 2), (int)(radius * 2)); // 设置按钮大小为直径的两倍
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 使用按钮的背景色填充圆形区域
        g2.setColor(getBackground());
        g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

        // 绘制文本
        super.paintComponent(g2);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (isBorderPainted()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(getForeground());
            g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            g2.dispose();
        }
    }

    @Override
    public boolean contains(int x, int y) {
        // 修改包含检测以适应圆形按钮
        Shape shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        return shape.contains(x, y);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int)(radius * 2), (int)(radius * 2));
    }
}