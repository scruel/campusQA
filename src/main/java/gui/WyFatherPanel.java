package gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;


/**
 * Created by Scruel on 2016/6/22.
 * Personal github - https://github.com/scruel
 */
public class WyFatherPanel extends JScrollPane {
    Color normalColor = new Color(231, 239, 248);
    Color color_child_hover = new Color(255, 240, 193);
    int x;
    int y;
    /**
     * 使用add方法将子JPanel加入到此一类JPanel中
     *
     * @author Scruel
     */
    private int chSum = 0;
    private int lastChIndex = -1;
    private int clickedChIndex = -1;
    private JPanel JPanelchildAll;
    private JPanel jPanelTitle;
    private JLabel iconJL;
    private JPopupMenu jMenuJp = new JPopupMenu();
    private JPopupMenu jMenuTitle = new JPopupMenu();
    private JPopupMenu jMenuChild = new JPopupMenu();
    private HashMap<Integer, WyChildPanel> chMap = new HashMap<>();
    private boolean closed = true;
    private File localPath = new File("src/main/resources/img");
    private int fIndex;
    private ChartClient watched;

    public WyFatherPanel(String title, int fIndex, ChartClient wathched) {
        ImageIcon imageIcon = new ImageIcon(localPath + "/mainpanel_foldernode_collapsetexture.png");
        this.fIndex = fIndex;
        this.watched = wathched;
        MouseWheelListener[] ms = this.getMouseWheelListeners();
        for (MouseWheelListener i : ms) {
            this.removeMouseWheelListener(i);
        }
        jPanelTitle = new JPanel(null);
        JPanelchildAll = new JPanel(null);
        iconJL = new JLabel(imageIcon);

        JLabel titleJL = new JLabel(title, SwingConstants.CENTER);
        titleJL.setFont(new Font("Microsoft YaHei", 0, 14));
        titleJL.setForeground(Color.BLACK);
        int titleLen = title.getBytes().length * 8;
//                JLabel numsJL = new JLabel("" + chSum);//在线人数/总人数
//                numsJL.setBounds(titleLen+38, 6, 14, 100);
//                System.out.println(titleLen + " " + chSum);
//                System.out.println(numsJL);
        iconJL.setBounds(10, 6, 14, 14);
        titleJL.setBounds(30, 6, titleLen, 14);
        jPanelTitle.setBounds(0, 0, 280, 25);
        JPanelchildAll.setBounds(0, 25, 280, 0);
        jPanelTitle.add(iconJL);
        jPanelTitle.add(titleJL);
//                jMenuTitle.add(numsJL);
        jPanelTitle.setBackground(normalColor);
        JPanelchildAll.setBackground(normalColor);


        JMenuItem jmenuItem1 = new JMenuItem("添加分组");
        jMenuJp.add(jmenuItem1);

        JMenuItem jmenuItem2 = new JMenuItem("重命名分组");
        JMenuItem jmenuItem3 = new JMenuItem("删除分组");//删除后所有的都放到我的好友里，之前加个确认提示
        jMenuTitle.add(jmenuItem2);
        jMenuTitle.add(jmenuItem3);

        JMenuItem jmenuItem4 = new JMenuItem("添加好友");//添加好友的时候，提示要放到哪个分组，可以双向验证，也可以单向验证。
        JMenuItem jmenuItem5 = new JMenuItem("删除好友");
        JMenuItem jmenuItem6 = new JMenuItem("与好友聊天");
        jmenuItem6.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String target = chMap.get(clickedChIndex).getUsername();
                wathched.openChartWindow(target);
            }
        });
        jMenuChild.add(jmenuItem4);
        jMenuChild.add(jmenuItem5);
        jMenuChild.add(jmenuItem6);

        this.add(JPanelchildAll);
        this.add(jPanelTitle);
        this.setLayout(null);
        this.setSize(280, 25);
        this.add(jMenuChild);
        this.add(jMenuTitle);
        this.add(jMenuJp);

        this.addMouseListener(new fatherPanelListener());
        this.addMouseMotionListener(new fatherPanelListener());

    }

    public int getFIndex() {
        return fIndex;
    }

    public void addChildJPanel(WyChildPanel childPanel) {
        JPanelchildAll.add(childPanel);
        chMap.put(chSum, childPanel);
        childPanel.setLocation(0, 0 + 50 * chSum);
        chSum++;
        JPanelchildAll.setSize(280, 50 * chSum);

    }

    public void unExpandAll() {
        iconJL.setIcon(new ImageIcon(localPath + "/mainpanel_foldernode_collapsetexture.png"));
        this.setSize(280, 25);
        closed = true;

    }

    public void expandAll() {
        this.setSize(280, JPanelchildAll.getHeight() + 25);
        iconJL.setIcon(new ImageIcon(localPath + "/MainPanel_FolderNode_expandTextureHighlight.png"));
        closed = false;
    }

    public void removeAllChildren() {
        for (int i : chMap.keySet()) {
            JPanel jp = chMap.get(i);
            jp = null;
        }
        JPanelchildAll.removeAll();
        JPanelchildAll.setSize(280, 0);
        this.setSize(280, 25);
        chSum = 0;
    }

    public boolean isClosed() {
        return closed;
    }

    class fatherPanelListener extends MouseAdapter {
        @Override public void mouseMoved(MouseEvent e)//代替了子控件的mouseExited和mouseEntered
        {
            y = e.getY();
            x = e.getX();
            if (y > 25) {
                int num = (y - 25) / 50;
                if (clickedChIndex != num) {
                    WyChildPanel wcp = chMap.get(num);
                    wcp.setBackground(color_child_hover);

                    if (lastChIndex != clickedChIndex && lastChIndex != -1 && lastChIndex != num) {
                        WyChildPanel owcp = chMap.get(lastChIndex);
                        owcp.setBackground(normalColor);
                    }
                    lastChIndex = num;
                }
            }
        }

        @Override public void mouseClicked(MouseEvent e) {
            if (y <= 25)//标题处理
            {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (isClosed() && chSum != 0) {
                        expandAll();
                    } else {
                        unExpandAll();
                    }

                    watched.upDateUI();
                    jPanelTitle.setBackground(normalColor);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    jMenuTitle.show(jPanelTitle, x, y);
                }
            } else//子处理
            {
                int num = (y - 25) / 50;
                WyChildPanel wcp = chMap.get(num);
                for (int i : chMap.keySet()) {
                    chMap.get(i).setBackground(normalColor);
                }
                wcp.setBackground(new Color(253, 235, 169));
                clickedChIndex = num;
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    String target = wcp.getUsername();
                    watched.openChartWindow(target);

                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    jMenuChild.show(JPanelchildAll, x, y);
                }

            }
        }

        @Override public void mouseEntered(MouseEvent e) {
            if (y <= 25)//标题处理
            {
                if (isClosed() && chSum != 0) {
                    iconJL.setIcon(new ImageIcon(localPath + "/mainpanel_foldernode_collapsetexturehighlight.png"));
                    jPanelTitle.setBackground(new Color(236, 245, 251));//变色
                }
            }
        }


        @Override public void mouseExited(MouseEvent e) {
            if (y <= 25)//标题处理
            {
                if (isClosed() && chSum != 0) {
                    iconJL.setIcon(new ImageIcon(localPath + "/mainpanel_foldernode_collapsetexture.png"));
                    jPanelTitle.setBackground(normalColor);
                }
            } else {
                WyChildPanel owcp = chMap.get(lastChIndex);
                if (lastChIndex != -1 && lastChIndex != clickedChIndex) owcp.setBackground(normalColor);
            }
        }


    }

}
