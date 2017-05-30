package gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Scruel on 2016/6/14.
 * Personal blog  --  http://wysum.com
 */
public class LoginWindow extends JFrame {
    private Socket client = null;
    private JLabel usernameJL = new JLabel("username:");
    private JLabel userpassJL = new JLabel("password:");
    private File localPath = new File("src/main/resources/img");

    private JTextField usernameJTF = new JTextField("user");
    private JPasswordField userpassJTF = new JPasswordField();
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    public LoginWindow(String title) {
        super(title);
        String host = "127.0.0.1";
        int port = 8899;
        try {
            client = new Socket(host, port);
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(null, "服务器未开启", "服务器信息", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        System.out.println(client);

        try {
            reader = new ObjectInputStream(client.getInputStream());
            writer = new ObjectOutputStream(client.getOutputStream());
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(null, "读写异常", "信息", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ImageIcon backgroundImage = new ImageIcon(localPath + "/afternoon.jpg");
        JLabel backgroundJL = new JLabel(backgroundImage);
        backgroundJL.setBounds(0, 0, 430, 184);

        //最小化+关闭按钮，代码。。。繁多。。。
        ImageIcon closeImage = new ImageIcon(localPath + "/btn_close_normal.png");
        ImageIcon closeImage_hover = new ImageIcon(localPath + "/btn_close_highlight.png");
        ImageIcon closeImage_down = new ImageIcon(localPath + "/btn_close_down.png");
        JButton closeBT = new JButton(closeImage);
        closeBT.setBounds(400, 0, 30, 30);
//                closeBT.setOpaque(false);//设置背景透明
        closeBT.setContentAreaFilled(false);//设置背景色透明
        closeBT.setBorderPainted(false); //设置外边框透明
        closeBT.setFocusPainted(false);//设置内边框透明
        closeBT.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        closeBT.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent arg0) {
                closeBT.setIcon(closeImage_hover);

            }

            public void mouseExited(MouseEvent arg0) {
                closeBT.setIcon(closeImage);
            }

            @Override public void mousePressed(MouseEvent e) {
                closeBT.setIcon(closeImage_down);
            }
        });
        ImageIcon miniImage = new ImageIcon(localPath + "/btn_mini_normal.png");
        ImageIcon miniImage_hover = new ImageIcon(localPath + "/btn_mini_highlight.png");
        ImageIcon miniImage_down = new ImageIcon(localPath + "/btn_mini_down.png");

        JButton miniBT = new JButton(miniImage);

        miniBT.setBounds(370, 0, 30, 30);
//                closeBT.setOpaque(false);//设置背景透明
        miniBT.setContentAreaFilled(false);//设置背景色透明
        miniBT.setBorderPainted(false); //设置外边框透明
        miniBT.setFocusPainted(false);//设置内边框透明
        miniBT.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                setExtendedState(JFrame.ICONIFIED);//ICONIFIED代表最小化
            }
        });
        miniBT.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent arg0) {
                miniBT.setIcon(miniImage_hover);
            }

            public void mouseExited(MouseEvent arg0) {
                miniBT.setIcon(miniImage);
            }

            @Override public void mousePressed(MouseEvent e) {
                miniBT.setIcon(miniImage_down);
            }
        });

        backgroundJL.add(miniBT);
        backgroundJL.add(closeBT);
        //最小化+关闭按钮代码结束

        ImageIcon loginImage = new ImageIcon(localPath + "/button_login_normal.png");
        ImageIcon loginImage_hover = new ImageIcon(localPath + "/button_login_hover.png");
        ImageIcon loginImage_down = new ImageIcon(localPath + "/button_login_down.png");
        JButton loginBT = new JButton("登    录");
        JLabel loginBg = new JLabel(loginImage);
        loginBT.setForeground(Color.WHITE);
        loginBT.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent arg0) {
                loginBg.setIcon(loginImage_hover);

            }

            public void mouseExited(MouseEvent arg0) {
                loginBg.setIcon(loginImage);
            }

            @Override public void mousePressed(MouseEvent e) {
                loginBg.setIcon(loginImage_down);
            }
        });
        JLabel findPassJL = new JLabel("找回密码");
        findPassJL.setForeground(new Color(0x61B3F6));

        findPassJL.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "正在筹划。。。告知服务器消息内容", "信息", JOptionPane.WARNING_MESSAGE);
            }
        });
        findPassJL.setBounds(350, 210, 190, 30);
//                findPass.setContentAreaFilled(false);//设置背景色透明
        usernameJL.setBounds(65, 195, 80, 30);
        userpassJL.setBounds(65, 225, 80, 30);
        usernameJTF.setBounds(140, 195, 190, 30);
        userpassJTF.setBounds(140, 225, 190, 30);
        loginBT.setBounds(140, 265, 190, 28);
        loginBg.setBounds(140, 265, 190, 30);
        loginBT.setContentAreaFilled(false);//设置背景色透明
        loginBT.setBorderPainted(false); //设置外边框透明
        loginBT.setFocusPainted(false);//设置内边框透明
        loginBT.addActionListener(new LoginActionListener());
        this.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                try {
                    // 可以弹出一个确认框
                    client.close();
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "读写异常", "服务器信息", JOptionPane.WARNING_MESSAGE);
                    return;
                }

            }
        });
        MoveEveryWhereMouseListener ma = new MoveEveryWhereMouseListener(this);
        this.addMouseMotionListener(ma);
        this.addMouseListener(ma);
        this.setLocationRelativeTo(null);//窗口居中
//                this.setAlwaysOnTop(true);
        this.setSize(430, 310);
        this.setLayout(null);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(0xEBF2F9));

        this.getContentPane().add(usernameJL);
        this.getContentPane().add(userpassJL);
        this.getContentPane().add(usernameJTF);
        this.getContentPane().add(userpassJTF);
        this.getContentPane().add(loginBT);
        this.getContentPane().add(loginBg);
        this.getContentPane().add(findPassJL);
        this.getContentPane().add(backgroundJL);
        this.setVisible(true);
    }

    //实现了任意位置点击移动窗口


    class LoginActionListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {


            String username = usernameJTF.getText();
            char[] password = userpassJTF.getPassword();

            if (username.equals("")) {

                JOptionPane.showMessageDialog(null, "登录失败，用户名不能为空", "登录信息", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (password.equals("")) {
                JOptionPane.showMessageDialog(null, "登录失败，密码不能为空", "登录信息", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (password.length > 16 || password.length < 6) {
                JOptionPane.showMessageDialog(null, "登录失败，密码长度必须在6-16位之间", "登录信息", JOptionPane.WARNING_MESSAGE);
                return;
            }
//                        System.out.println("xxx1" + username);

            //+用户名重复判断
            //密码判断，不存在则创建
            try {
                String temp;
                writer.writeObject("login");
                writer.flush();
                writer.writeObject(username);
                writer.flush();
                while (true) {
                    if ((temp = (String) reader.readObject()) != null) {
                        if (("fail").equals(temp)) {
                            JOptionPane.showMessageDialog(null, "登录失败，该用户已在线", "登录信息", JOptionPane.WARNING_MESSAGE);
                            return;
                        } else break;
                    }

                }

                writer.writeObject(password);

                writer.flush();
                while (true) {
                    if ((temp = (String) reader.readObject()) != null) {
                        if (("fail").equals(temp)) {
                            JOptionPane.showMessageDialog(null, "登录失败，密码错误", "登录信息", JOptionPane.WARNING_MESSAGE);
                            return;
                        } else break;

                    }
                }

            } catch (Exception ee) {
                JOptionPane.showMessageDialog(null, "读写异常", "服务器信息", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "登录成功", "登录信息", JOptionPane.INFORMATION_MESSAGE);

            new ChartClient(username, client, writer, reader);
            dispose();

        }
    }

}
