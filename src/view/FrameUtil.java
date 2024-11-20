package view;

import javax.swing.*;
import java.awt.*;

/**
 * This class is to create basic JComponent and add them to the specified layer of a JLayeredPane.
 */
public class FrameUtil {
    public static JLabel createJLabel(JLayeredPane layeredPane, Point location, int width, int height, String text, int layer) {
        JLabel jLabel = new JLabel(text);
        jLabel.setSize(width, height);
        jLabel.setLocation(location);
        layeredPane.add(jLabel, layer);
        return jLabel;
    }

    public static JLabel createJLabel(JLayeredPane layeredPane, String name, Font font, Point location, int width, int height, int layer) {
        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setLocation(location);
        label.setSize(width, height);
        layeredPane.add(label, layer);
        return label;
    }

    public static JTextField createJTextField(JLayeredPane layeredPane, Point location, int width, int height, int layer) {
        JTextField jTextField = new JTextField();
        jTextField.setSize(width, height);
        jTextField.setLocation(location);
        layeredPane.add(jTextField, layer);
        return jTextField;
    }

    public static JButton createButton(JLayeredPane layeredPane, String name, Point location, int width, int height, int layer) {
        JButton button = new JButton(name);
        button.setLocation(location);
        button.setSize(width, height);
        layeredPane.add(button, layer);
        return button;
    }
}