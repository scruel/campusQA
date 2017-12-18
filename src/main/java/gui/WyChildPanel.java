package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author Scruel Tao
 */
public class WyChildPanel extends JPanel {

  Color normalColor = new Color(231, 239, 248);
  private String username;
  private String usersignature;
  private File localPath = new File("src/main/resources/img");

  public WyChildPanel(ImageIcon image, String username, String usersignature) {
//                ImageIcon imageIcon = new ImageIcon(localPath + "/MainPanel_ContactHeadSelect_paddingDraw.png");
    ImageIcon imageIcon = new ImageIcon(localPath + "/MainPanel_ContactHeadSelect_paddingDraw.png");
    this.username = username;
    this.usersignature = usersignature;

    JLabel headJL = new JLabel(image);
    JLabel unJL = new JLabel(username);
    JLabel usJL = new JLabel(usersignature);
    JLabel headBKGJL = new JLabel(imageIcon);

    headBKGJL.setBounds(6, 1, 48, 48);
    headJL.setBounds(10, 3, 45, 45);
    unJL.setBounds(60, 3, 170, 25);
    unJL.setFont(new Font("Arial", 0, 13));
    usJL.setFont(new Font("Microsoft YaHei", 0, 13));
    usJL.setForeground(new Color(0x9B98A3));
    usJL.setBounds(60, 20, 170, 25);
    this.setLayout(null);
    this.add(headJL);
    this.setBackground(normalColor);
    this.add(headBKGJL);
    this.add(unJL);
    this.add(usJL);
    this.setSize(270, 50);
  }


  public String getUsername() {
    return username;
  }
}
