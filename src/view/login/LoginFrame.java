package view.login;

import error.ErrorFrame;
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
        this.setTitle("Login");
        this.setLayout(null);
        this.setSize(1900, 900);
        JLayeredPane layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);
        try {
            backgroundImage = ImageIO.read(new File("img/login.jpg"));
        } catch (IOException e) {
            ErrorFrame error = new ErrorFrame(500,200,"Background Image not found");
            error.setVisible(true);
        }
        view.login.BackgroundPanel backgroundPanel = new view.login.BackgroundPanel(backgroundImage);
        backgroundPanel.setBounds(0, 0, 1750, 890);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        JLabel userLabel = FrameUtil.createJLabel(layeredPane, new Point(680, 300), 70, 40, "username:",JLayeredPane.MODAL_LAYER);
        JLabel passLabel = FrameUtil.createJLabel(layeredPane, new Point(680, 400), 70, 40, "password:",JLayeredPane.MODAL_LAYER);
        username = FrameUtil.createJTextField(layeredPane, new Point(750, 300), 120, 40,JLayeredPane.MODAL_LAYER);
        password = FrameUtil.createJTextField(layeredPane, new Point(750, 400), 120, 40,JLayeredPane.MODAL_LAYER);

        submitBtn = FrameUtil.createButton(layeredPane, "Confirm", new Point(540, 310), 100, 40,JLayeredPane.MODAL_LAYER);
        resetBtn = FrameUtil.createButton(layeredPane, "Reset", new Point(950, 310), 100, 40,JLayeredPane.MODAL_LAYER);
        signinBtn = FrameUtil.createButton(layeredPane, "Sign in", new Point(570, 400), 100, 40,JLayeredPane.MODAL_LAYER);
        guestBtn = FrameUtil.createButton(layeredPane, "Guest Mode", new Point(920, 400), 100, 40,JLayeredPane.MODAL_LAYER);
        chooseDaytime = FrameUtil.createButton(layeredPane, "Daytime", new Point(610, 490), 100, 40,JLayeredPane.MODAL_LAYER);
        chooseNight = FrameUtil.createButton(layeredPane, "Night",new Point(890, 490), 100, 40,JLayeredPane.MODAL_LAYER);

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
                            this.setVisible(false);
                            LevelFrame levelFrame = new LevelFrame(500,200);
                            levelFrame.setVisible(true);
                        }else{
                            messageLabel.setText("Password incorrect.\n" +
                                    " Please check your username and password.");
                            return;
                        }
                    } catch (IOException ex) {
                        ErrorFrame error = new ErrorFrame(500,200,"User file not found");
                        error.setVisible(true);
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
                        ErrorFrame error = new ErrorFrame(500,200,"Failed to create new account");
                        error.setVisible(true);
                    }
                }
            }
        });

        guestBtn.addActionListener(e -> {
            username.setText("");
            password.setText("");
            this.setVisible(false);
            LevelFrame levelFrame = new LevelFrame(500,200);
            levelFrame.setVisible(true);
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
