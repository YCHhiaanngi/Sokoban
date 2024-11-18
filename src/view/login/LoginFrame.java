package view.login;

import view.FrameUtil;
import view.level.LevelFrame;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class LoginFrame extends JFrame {
    private JTextField username;
    private JTextField password;
    private JButton submitBtn;
    private JButton resetBtn;
    private JButton signinBtn;
    private JButton guestBtn;
    private LevelFrame levelFrame;
    private static int count;
    private JButton chooseDaytime;
    private JButton chooseNight;
    private BufferedImage backgroundImage;
    private JLabel messageLabel;
    private static String userName;
    private static String passWord;
    private JPanel messagePanel;

    public static boolean isDayTheme;
    public static boolean isNightTheme;


    public LoginFrame(int width, int height) {
        this.setTitle("Login Frame");
        this.setLayout(null);
        this.setSize(600, 400);
        try {
            backgroundImage = ImageIO.read(new File("img/login.jpg"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load background image", e);
        }
        view.login.BackgroundPanel backgroundPanel = new view.login.BackgroundPanel(backgroundImage);
        this.setContentPane(backgroundPanel);
        this.setLayout(null);

        JLabel userLabel = FrameUtil.createJLabel(this, new Point(680, 300), 70, 40, "username:");
        JLabel passLabel = FrameUtil.createJLabel(this, new Point(680, 400), 70, 40, "password:");
        username = FrameUtil.createJTextField(this, new Point(750, 300), 120, 40);
        password = FrameUtil.createJTextField(this, new Point(750, 400), 120, 40);

        submitBtn = FrameUtil.createButton(this, "Confirm", new Point(540, 310), 100, 40);
        resetBtn = FrameUtil.createButton(this, "Reset", new Point(950, 310), 100, 40);
        signinBtn = FrameUtil.createButton(this, "Sign in", new Point(570, 400), 100, 40);
        guestBtn = FrameUtil.createButton(this, "Guest Mode", new Point(920, 400), 100, 40);
        chooseDaytime = FrameUtil.createButton(this, "Daytime", new Point(610, 490), 100, 40);
        chooseNight = FrameUtil.createButton(this, "Night",new Point(890, 490), 100, 40);

        messagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        messagePanel.setBounds(610, 600, 400, 50);
        messagePanel.setLayout(new BorderLayout());


        messageLabel = new JLabel();
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        messagePanel.setBorder(BorderFactory.createEmptyBorder());
        messagePanel.setOpaque(false);

        this.add(messagePanel);

        PropertyChangeListener propertyChangeListener = (PropertyChangeEvent evt) -> {
            if ("text".equals(evt.getPropertyName())) {
                String text = (String) evt.getNewValue();
                if (text != null && !text.isEmpty()) {
                    messagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                    messagePanel.setBackground(new Color(211, 211, 211, 128));
                } else {
                    messagePanel.setBorder(BorderFactory.createEmptyBorder());
                    messagePanel.setBackground(new Color(0, 0, 100, 0));
                }
            }
        };//todo:目前还不知道为什么底部框不能跟随字出现而出现
        submitBtn.addActionListener(e -> {

            System.out.println("Username = " + username.getText());
            System.out.println("Password = " + password.getText());

            File folder = new File("userfile");
            File[] list = folder.listFiles();
            for(File files : list){
                if((username.getText()+".txt").equals(files.getName())){
                    try {
                        FileReader reader = new FileReader(files);
                        BufferedReader bReader = new BufferedReader(reader);
                        String line = bReader.readLine();
                        if(line.equals(password.getText())){
                            messageLabel.setText("Password Correct");
                            userName = username.getText();
                            passWord = password.getText();
                            if (this.levelFrame != null) {
                                this.levelFrame.setVisible(true);
                                this.setVisible(false);
                            }
                        }else{
                            messageLabel.setText("Password incorrect.\n" +
                                    " Please check your username and password.");
                            return;
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            messageLabel.setText("You have not registered yet.");


        });
        resetBtn.addActionListener(e -> {
            username.setText("");
            password.setText("");
        });

        signinBtn.addActionListener(e -> {
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                messageLabel.setText("Please input your name and password first!");
            }else {
                String fileName = "userfile/"+ username.getText() + ".txt";
                File file = new File(fileName);
                if (file.exists()) {
                    messageLabel.setText("User already exists! Please login");
                    username.setText("");
                    password.setText("");
                } else {
                    try {
                        if (file.createNewFile()) {
                            messageLabel.setText("New User Created");
                            FileWriter writer = new FileWriter(fileName);
                            BufferedWriter output = new BufferedWriter(writer);
                            output.write(password.getText()+"\n");
                            output.write(1);
                            output.close();
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        guestBtn.addActionListener(e -> {
            username.setText("Guest");
            password.setText("");
            if (this.levelFrame != null) {
                this.levelFrame.setVisible(true);
                this.setVisible(false);
            }
        });

        this.chooseDaytime.addActionListener(e ->{
            isDayTheme = true;
            isNightTheme = false;
            messageLabel.setText("You have chosen daytime theme");
        });
        this.chooseNight.addActionListener(e ->{
            isDayTheme = false;
            isNightTheme = true;
            messageLabel.setText("You have chosen night theme");
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setLevelFrame(LevelFrame levelFrame) {
        this.levelFrame = levelFrame;
    }

    public static String getUserName(){
        return userName;
    }

    public static String getPassword(){
        return passWord;
    }
}
