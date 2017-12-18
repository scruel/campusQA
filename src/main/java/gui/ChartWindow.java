package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author Scruel Tao
 */
public class ChartWindow extends JFrame {
  Color btn_normal = new Color(15, 146, 238);
  Color btn_hover = new Color(76, 176, 253);
  private String userName, targetName;
  private TextArea output = new TextArea("", 20, 18, TextArea.SCROLLBARS_BOTH);
  private TextArea input = new TextArea("", 20, 18, TextArea.SCROLLBARS_VERTICAL_ONLY);
  private ObjectOutputStream writer;
  private String nextContent;
//        Color btn_down = new Color(31, 129, 212);

//        public static void main(String[] args) throws Exception
//        {
//                new ChartWindow("w", "xx", new ObjectOutputStream(new FileOutputStream(new File("K:\\Program\\编程\\javaWorkSpace\\current\\bin\\socket\\data.pro"))));
//        }

  public ChartWindow(String userName, String targetName, ObjectOutputStream writer) {
    super("我(" + userName + ")与 " + targetName + " 的对话");
    this.writer = writer;
    this.userName = userName;
    this.targetName = targetName;
    JPanel p1 = new JPanel(null);
//                JButton face = new JButton("表情");
//                JButton front = new JButton("字体");
//                JButton front_color = new JButton("字体颜色");
    JButton clearMessage = new JButton("清空消息");
    JButton sent = new JButton("发送");
    clearMessage.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        input.setText("");
      }
    });

    sent.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sentMessage();
      }
    });

    input.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == 10) sentMessage();
      }
    });
    sent.setBackground(btn_normal);
    clearMessage.setBackground(btn_normal);
    sent.setForeground(Color.WHITE);
    clearMessage.setForeground(Color.WHITE);
    sent.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        sent.setBackground(btn_hover);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        sent.setBackground(btn_normal);
      }


    });
    clearMessage.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        clearMessage.setBackground(btn_hover);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        clearMessage.setBackground(btn_normal);
      }

    });
    sent.setBorderPainted(false); //设置外边框透明
    sent.setFocusPainted(false);//设置内边框透明
    clearMessage.setBorderPainted(false); //设置外边框透明
    clearMessage.setFocusPainted(false);//设置内边框透明

    output.setBackground(new Color(0xEBF2F9));
    output.setBounds(4, 0, 420, 250);
    output.setFont(new Font("楷体", Font.PLAIN, 14));
    output.setEditable(false);

    input.setFont(new Font("楷体", Font.PLAIN, 14));
    input.setBackground(new Color(0xEBF2F9));
    input.setBounds(4, 290, 420, 125);

    clearMessage.setBounds(4, 420, 120, 30);
    sent.setBounds(360, 420, 60, 30);
//                face.setBounds(4, 254, 60, 30);
//                p1.add(face);
//                front.setBounds(65, 254, 60, 30);
//                p1.add(front);
//                front_color.setBounds(126, 254, 90, 30);
//                p1.add(front_color);
    p1.setBackground(new Color(232, 238, 248));
    p1.add(output);
    p1.add(input);
    p1.add(clearMessage);
    p1.add(sent);
    this.setSize(450, 490);
    this.setLocationRelativeTo(null);//窗口居中
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(p1, "Center");
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  public boolean putNextContent(String nextContent, String target) {
    output.append(new Date() + "   " + target + ":\n" + nextContent + "\n");
    return true;
  }

  public void sentMessage() {
    input.setText(input.getText().trim());
    if (input.getText().equals("")) {

      JOptionPane.showMessageDialog(null, "请勿发送空内容", "错误", JOptionPane.WARNING_MESSAGE);
      return;
    }
    try {
      String nextContent = input.getText();
      output.append(new Date() + "   " + "我:" + "\n" + nextContent + "\n");
      writer.writeObject("chartingSe");
      writer.flush();
      writer.writeObject(userName);
      writer.flush();
      writer.writeObject(targetName);
      writer.flush();
      writer.writeObject(nextContent);
      input.setText("");

    } catch (Exception ee) {
      JOptionPane.showMessageDialog(null, "读写异常" + ee, "信息", JOptionPane.WARNING_MESSAGE);
      return;
    }
  }


}
