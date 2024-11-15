package view.login;

import view.FrameUtil;
import view.level.LevelFrame;

import javax.swing.*;
import java.awt.*;
import java.io.*;


public class LoginFrame extends JFrame {
    private JTextField username;
    private JTextField password;
    private JButton submitBtn;
    private JButton resetBtn;
    private JButton signinBtn;
    private JButton guestBtn;
    private LevelFrame levelFrame;
    private static int count;
    private static String userName;
    private static String passWord;


    public LoginFrame(int width, int height) {
        this.setTitle("Login Frame");
        this.setLayout(null);
        this.setSize(width, height);
        JLabel userLabel = FrameUtil.createJLabel(this, new Point(50, 20), 70, 40, "username:");
        JLabel passLabel = FrameUtil.createJLabel(this, new Point(50, 80), 70, 40, "password:");
        username = FrameUtil.createJTextField(this, new Point(120, 20), 120, 40);
        password = FrameUtil.createJTextField(this, new Point(120, 80), 120, 40);

        submitBtn = FrameUtil.createButton(this, "Confirm", new Point(40, 140), 100, 40);
        resetBtn = FrameUtil.createButton(this, "Reset", new Point(160, 140), 100, 40);
        signinBtn = FrameUtil.createButton(this, "Sign in", new Point(40, 200), 100, 40);
        guestBtn = FrameUtil.createButton(this, "Guest Mode", new Point(160, 200), 100, 40);

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
                            System.out.println("Password Correct");
                            userName = username.getText();
                            passWord = password.getText();
                            if (this.levelFrame != null) {
                                this.levelFrame.setVisible(true);
                                this.setVisible(false);
                            }
                        }else{
                            System.out.println("Password incorrect.\n" +
                                    "Please Check your username and password");
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else{
                    System.out.println("You have not register yet.");
                }
            }


            //todo:把这里的控制台输出改成JLabel输出，下面同理，可以改文字的内容



        });
        resetBtn.addActionListener(e -> {
            username.setText("");
            password.setText("");
        });

        signinBtn.addActionListener(e -> {
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                System.out.println("Please input your name and password first!");
            }else {
                String fileName = "userfile/"+ username.getText() + ".txt";
                File file = new File(fileName);
                if (file.exists()) {
                    System.out.println("User already exists! Please login");
                    username.setText("");
                    password.setText("");
                } else {
                    try {
                        if (file.createNewFile()) {
                            System.out.println("New User Created");
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
