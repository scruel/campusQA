package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;


/**
 * Created by Scruel on 2016/6/14.
 * Personal blog  --  http://wysum.com
 */
public class ChartClient extends JFrame {
    private String username;
    private int fIndex = 0;
    private Socket client;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Object[] onlineUser;
    private HashMap<String, ChartWindow> chartingMap = new HashMap<>();
    private JPanel jp = new JPanel(null);
    private JScrollPane jScrollPane = new JScrollPane();
    private WyFatherPanel onlineFP = new WyFatherPanel("当前在线", fIndex, this);
    private HashMap<Integer, WyFatherPanel> fatherMap = new HashMap<>();
    private File localPath = new File("src/main/resources/img");


//        public static void main(String[] args) throws Exception
//        {
//                new ChartClient("Scruel", new Socket(), new ObjectOutputStream(new FileOutputStream(new File("src/main/resources/data.pro"))), new ObjectInputStream(new FileInputStream(new File("src/main/resources/data.pro"))));
//        }

    ChartClient(String username, Socket socket, ObjectOutputStream writer, ObjectInputStream reader) {

        super(username + "的聊天界面");
        this.reader = reader;
        this.writer = writer;
        this.client = socket;
        this.username = username;
        System.out.println(username);
        fatherMap.put(fIndex++, onlineFP);
        WyFatherPanel fatherPanel2 = new WyFatherPanel("我的好友", fIndex, this);
        fatherMap.put(fIndex++, fatherPanel2);
        //添加分组的时候可以用：1.创建FP  2.置入map 3.更新jpPsize 4.更新UI
        int countHeigh = 0;
        for (int i : fatherMap.keySet()) {
            jp.add(fatherMap.get(i));
            countHeigh += fatherMap.get(i).getHeight();
            if (i == 0) continue;
            fatherMap.get(i).setLocation(0, fatherMap.get(i - 1).getY() + fatherMap.get(i - 1).getHeight());
        }
        jp.setPreferredSize(new Dimension(230, countHeigh + 50));

        jp.setBackground(new Color(231, 239, 248));
        jScrollPane.setViewportView(jp);
        upDateUI();
        jScrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override public void mouseWheelMoved(MouseWheelEvent e) {
                JScrollBar sbr = jScrollPane.getVerticalScrollBar();
                sbr.setValue(sbr.getValue() + e.getWheelRotation() * 40);
            }
        });

        ImageIcon backgroundImage = new ImageIcon(localPath + "/skin/skin.png");
        JLabel backgroundJL = new JLabel(backgroundImage);
        backgroundJL.setBounds(0, 0, 280, 165);
        jScrollPane.setBounds(0, 165, 280, 600);
        //最小化+关闭按钮，代码。。。繁多。。。
        ImageIcon closeImage = new ImageIcon(localPath + "/btn_close_normal.png");
        ImageIcon closeImage_hover = new ImageIcon(localPath + "/btn_close_highlight.png");
        ImageIcon closeImage_down = new ImageIcon(localPath + "/btn_close_down.png");
        JButton closeBT = new JButton(closeImage);
        closeBT.setBounds(250, 0, 30, 30);
//                closeBT.setOpaque(false);//设置背景透明,不知道为什么不能用。。
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

            @Override public void mouseExited(MouseEvent arg0) {
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
        miniBT.setBounds(220, 0, 30, 30);
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

            @Override public void mouseExited(MouseEvent arg0) {
                miniBT.setIcon(miniImage);
            }

            @Override public void mousePressed(MouseEvent e) {
                miniBT.setIcon(miniImage_down);
            }
        });

        backgroundJL.add(miniBT);
        backgroundJL.add(closeBT);
        //最小化+关闭按钮代码结束


        JButton jButtonUserName = new JButton(username);
        int jbulen = username.length() * 30 + 20;
        jButtonUserName.setBounds(10, 70, jbulen, 25);
        jButtonUserName.setHorizontalAlignment(SwingConstants.LEFT);//两个一起才有效果
        jButtonUserName.setMargin(new Insets(0, 0, 0, 0));//两个一起才有效果
        jButtonUserName.setFont(new Font("Microsoft YaHei", 0, 18));
        jButtonUserName.setForeground(Color.WHITE);
        jButtonUserName.setContentAreaFilled(false);//设置背景色透明
        jButtonUserName.setBorderPainted(false); //设置外边框透明
        jButtonUserName.setFocusPainted(false);//设置内边框透明

        String usersignature = "请编辑个性签名";//服务器可以做接收

        JTextField JTextFieldUS = new JTextField(usersignature);
        JLabel JLabelUS = new JLabel(usersignature);

        JTextFieldUS.setBounds(12, 95, 230, 20);
        JLabelUS.setBounds(12, 95, 230, 20);
        JTextFieldUS.setFont(new Font("Microsoft YaHei", 0, 13));
        JTextFieldUS.setVisible(false);
        JLabelUS.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                JTextFieldUS.setVisible(true);
                JLabelUS.setVisible(false);
                JTextFieldUS.requestFocus();
            }
        });
        JTextFieldUS.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                JTextFieldUS.setVisible(false);
                JLabelUS.setVisible(true);
                JLabelUS.setText(JTextFieldUS.getText());

            }
        });
        JTextFieldUS.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                JTextFieldUS.selectAll();
            }

            @Override public void focusLost(FocusEvent e) {
                JTextFieldUS.setVisible(false);
                JLabelUS.setVisible(true);
                JLabelUS.setText(JTextFieldUS.getText());
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override public void eventDispatched(AWTEvent event) {
                MouseEvent mouseEvent = (MouseEvent) event;

                if (mouseEvent.getID() == MouseEvent.MOUSE_CLICKED) {
                    if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                        if (mouseEvent.getY() > 115 || mouseEvent.getY() < 95) {
                            JTextFieldUS.setVisible(false);
                            JLabelUS.setVisible(true);
                            JLabelUS.setText(JTextFieldUS.getText());
                            JTextFieldUS.grabFocus();
                        }
//                                                System.out.println(mouseEvent.getY());
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
        JLabelUS.setFont(new Font("Microsoft YaHei", 0, 13));
        JLabelUS.setForeground(Color.WHITE);
        JTextFieldUS.setBorder(new EmptyBorder(0, 0, 0, 0)); //设置外边框透明
        backgroundJL.add(jButtonUserName);
        backgroundJL.add(JTextFieldUS);
        backgroundJL.add(JLabelUS);

        jScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));//去掉边框

        MoveEveryWhereMouseListener ma = new MoveEveryWhereMouseListener(this);
        this.addMouseMotionListener(ma);
        this.addMouseListener(ma);
        this.setUndecorated(true);
        this.setLayout(null);
        this.getContentPane().add(jScrollPane);
        this.getContentPane().add(backgroundJL);
        this.addWindowListener(new MyWindowListener());
        this.setSize(280, 600);
        this.setVisible(true);
        new Thread(new Monitor()).start();
    }

    public void openChartWindow(String target) {
        if (!target.equals(username)) {
            if (chartingMap.containsKey(target)) {
                chartingMap.get(target).setVisible(true);
            } else {
                ChartWindow cw = new ChartWindow(username, target, writer);
                chartingMap.put(target, cw);
            }
        }
    }

    public void upDateUI() {
        int height = 0;
        for (int i : fatherMap.keySet()) {
            WyFatherPanel fp = fatherMap.get(i);
            height += fp.getHeight();
            if (i == 0) continue;

            fp.setLocation(0, fatherMap.get(i - 1).getY() + fatherMap.get(i - 1).getHeight());

        }
        jp.setPreferredSize(new Dimension(230, height + 20));
        jScrollPane.validate();
    }

    class Monitor extends Thread//监视
    {
        @Override public void run() {
            try {
                //直接展开
                onlineFP.expandAll();
                upDateUI();
                while (true) {
                    String megType;
                    if ((megType = (String) reader.readObject()) != null) {
                        if (megType.equals("updateUserList")) {
                            onlineUser = (Object[]) reader.readObject();

                            onlineFP.removeAllChildren();
                            boolean flag = onlineFP.isClosed();//true 关 false开
                            if (!flag) {
                                onlineFP.unExpandAll();
                            }
                            for (Object s : onlineUser) {
                                onlineFP.addChildJPanel(new WyChildPanel(new ImageIcon(localPath + "/head/176.png"), (String) s, "个性签名xxx"));
                            }
                            if (!flag) {
                                onlineFP.expandAll();
                            }
                            upDateUI();

                        }
                        if (megType.equals("exit")) {
                            client.close();
                            reader.close();
                            writer.close();
                            System.exit(0);
                        }
                        //列表更新结束

                        //聊天处理开始
                        if (megType.equals("chartingRe")) {
                            String target = (String) reader.readObject();//得到要和谁聊天
//                                                查看是否已经有了我与charting的聊天窗口
                            openChartWindow(target);
                            String nextContent = (String) reader.readObject();//得到聊天内容
                            chartingMap.get(target).putNextContent(nextContent, target);
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "客户端读写异常" + e, "信息", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    class MyWindowListener extends WindowAdapter {
        @Override public void windowClosing(WindowEvent e) {
            try {
                writer.writeObject("exit");
                writer.flush();
                client.close();
                reader.close();
                writer.close();

            } catch (Exception ee) {
                JOptionPane.showMessageDialog(null, "读写异常" + ee, "信息", JOptionPane.WARNING_MESSAGE);
            }
            System.exit(0);

        }
    }
}
