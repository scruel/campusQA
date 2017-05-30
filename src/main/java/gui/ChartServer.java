package gui;


import neuralNetWok.CampusQA;
import neuralNetWok.QA;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Scruel on 2016/6/14.
 * Personal blog  --  http://wysum.com
 */
public class ChartServer extends JFrame {
    static MultiLayerNetwork model;
    private JTextArea jta = new JTextArea();
    private HashMap<String, OnlineUserData> onlineMap = new HashMap<>();
    private HashMap<String, char[]> userDataMap = new HashMap<>();
    private File localPath = new File("src/main/resources/data.pro");
    private QA campusQA;


    public ChartServer() {

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(new JScrollPane(jta), BorderLayout.CENTER);
        this.setTitle("ChartServer");
        this.setSize(500, 300);
        this.setVisible(true);
        jta.setEditable(false);
        try {
//                        System.out.println(localPath.exists());
//                        System.out.println(localPath);
            if (localPath.exists()) {
                ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(localPath));
                userDataMap = (HashMap<String, char[]>) inStream.readObject();
                inStream.close();
            }
        } catch (Exception ee) {
        }

        this.addWindowListener(new WindowAdapter() {

            @Override public void windowClosing(WindowEvent e) {
                try {
                    if (!localPath.exists()) localPath.createNewFile();
                    ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(localPath));
                    stream.writeObject(userDataMap);
                    stream.close();
                    ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(localPath));
                    userDataMap = (HashMap) inStream.readObject();
                    inStream.close();

                    for (String s : onlineMap.keySet()) {
                        onlineMap.get(s).writer.writeObject("exit");
                        onlineMap.get(s).writer.flush();
                    }
                    System.exit(0);
                } catch (Exception ee) {
                }

            }
        });


//关闭的时候加一个给所有客户端的结束命令
        try {
            int port = 8899;
            jta.append(new Date() + " 正在初始化(生成深度学习神经网络)..." + "\n");
            campusQA = new CampusQA();
            jta.append(new Date() + " 神经网络初始化完成...服务器启动中..." + "\n");
            ServerSocket server = new ServerSocket(port);
            jta.append(new Date() + " 服务器已开启..." + "\n");
            jta.append(new Date() + " 用户数据存储路径" + localPath + "\n");
            //启动校园问答机器人

            onlineMap.put("robot", new OnlineUserData(null, null, null));

//                        onlineMap.put("robot",new OnlineUserData(Socket client, ObjectOutputStream writer, ObjectInputStream reader))
            while (true) {
                Socket socket = server.accept();
                new Thread(new ChartClientHandle(socket)).start();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "服务器sokect地址被占用！" + ex, "信息", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    class UserData {
        public UserData() {
        }
    }

    class OnlineUserData {
        Socket client;
        ObjectOutputStream writer;
        ObjectInputStream reader;

        public OnlineUserData(Socket client, ObjectOutputStream writer, ObjectInputStream reader) {
            this.client = client;
            this.writer = writer;
            this.reader = reader;
        }

//                OutputStreamWriter writer;
//                InputStreamReader reader;
//
//                public OnlineUserData(Socket client, ObjectOutputStream writer, ObjectInputStream reader)
//                {
//                        this.client = client;
//                        this.writer = new OutputStreamWriter(writer);
//                        this.reader = new InputStreamReader(reader);
//                }
    }

    class ChartClientHandle extends Thread {
        private Socket socket;
        private String currentUsername;

        public ChartClientHandle(Socket socket) {
            this.socket = socket;
        }

        public void closeCurrentUserOption() throws IOException {
            onlineMap.remove(currentUsername);
            jta.append(new Date() + currentUsername + "下线了" + "\n");
            updateTree();

        }

        public void updateTree() throws IOException {

            for (String s : onlineMap.keySet()) {
                if ("robot".equals(s)) continue;
                if (!onlineMap.get(s).client.isConnected()) {
                    onlineMap.remove(s);
                } else {
                    onlineMap.get(s).writer.writeObject("updateUserList");
                    onlineMap.get(s).writer.flush();
                    onlineMap.get(s).writer.writeObject(onlineMap.keySet().toArray());
                    onlineMap.get(s).writer.flush();
                    System.out.println(s);
                }
            }
        }

        public void chartContent(String senterName, String targetName, String nextContent) throws IOException {
            if (senterName.equals("everyone")) {
                for (String s : onlineMap.keySet()) {
                    onlineMap.get(s).writer.writeObject("updateUserList");
                    onlineMap.get(s).writer.flush();
                    onlineMap.get(s).writer.writeObject(onlineMap.keySet().toArray());
                    onlineMap.get(s).writer.flush();
                }
            } else {
                if (!onlineMap.containsKey(targetName)) {
                    nextContent = "对方已经下线！";
                    System.out.println(nextContent);
                    onlineMap.get(senterName).writer.writeObject("chartingRe");
                    onlineMap.get(senterName).writer.flush();
                    onlineMap.get(senterName).writer.writeObject(targetName);
                    onlineMap.get(senterName).writer.flush();
                    onlineMap.get(senterName).writer.writeObject(nextContent);
                    onlineMap.get(senterName).writer.flush();
                } else if ("robot".equals(targetName)) {
                    onlineMap.get(senterName).writer.writeObject("chartingRe");
                    onlineMap.get(senterName).writer.flush();
                    onlineMap.get(senterName).writer.writeObject(targetName);
                    onlineMap.get(senterName).writer.flush();
                    onlineMap.get(senterName).writer.writeObject(campusQA.getAnswer(nextContent));
                    onlineMap.get(senterName).writer.flush();
                } else {
                    onlineMap.get(targetName).writer.writeObject("chartingRe");
                    onlineMap.get(targetName).writer.flush();
                    onlineMap.get(targetName).writer.writeObject(senterName);
                    onlineMap.get(targetName).writer.flush();
                    onlineMap.get(targetName).writer.writeObject(nextContent);
                    onlineMap.get(targetName).writer.flush();
                }

            }
        }

        public void run() {
            try {
                ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
                String username;
                char[] password;
                String megType;


                while (true) {
                    if ((megType = (String) reader.readObject()) != null) {
                        //关闭处理
                        if ("exit".equals(megType)) {
                            closeCurrentUserOption();
                            break;
                        }
                        //关闭处理结束

                        //聊天处理
                        if ("chartingSe".equals(megType)) {
                            String senterName = (String) reader.readObject();
                            String targetName = (String) reader.readObject();
                            String nextContent = (String) reader.readObject();
                            chartContent(senterName, targetName, nextContent);
                        }

                        //聊天处理结束
                        //登陆处理
                        if ("login".equals(megType)) {
                            while (true) {
                                if ((username = (String) reader.readObject()) != null) {
                                    if (onlineMap.containsKey(username)) {

                                        writer.writeObject(("fail"));
                                        writer.flush();
                                        break;
                                    } else {
                                        writer.writeObject("success");
                                        writer.flush();

                                        while (true) {
                                            if ((password = (char[]) reader.readObject()) != null) {
                                                if (!userDataMap.containsKey(username))//检查是否登录过
                                                {
                                                    jta.append(new Date() + " " + username + "上线了" + "\n");
                                                    writer.writeObject("success");
                                                    onlineMap.put(username, new OnlineUserData(socket, writer, reader));
                                                    this.currentUsername = username;
                                                    userDataMap.put(username, password);
                                                    updateTree(); //通知所有用户更新在线用户信息
                                                } else//登录过
                                                {
                                                    if (!onlineMap.containsKey(username))//检查是否在线
                                                    {
                                                        String s1 = new String(userDataMap.get(username));
                                                        String s2 = new String(password);
                                                        if (s1.equals(s2)) {
                                                            jta.append(new Date() + " " + username + "上线了" + "\n");
                                                            writer.writeObject("success");
                                                            onlineMap.put(username, new OnlineUserData(socket, writer, reader));
                                                            this.currentUsername = username;
                                                            updateTree();
                                                        } else writer.writeObject("fail");
                                                    }
                                                }
                                                writer.flush();
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }

                        }
                        //登录处理结束
                    }
                }

            } catch (Exception ex) {
                try {
                    closeCurrentUserOption();
                } catch (IOException ee) {
                    System.err.println(ee);
                }
                System.out.println(ex);
            }

        }
    }
}
