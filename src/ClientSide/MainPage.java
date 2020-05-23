package ClientSide;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import Server.User;
import Server.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MainPage extends JFrame {
    public Reader reader;
    public Writer writer;
    public User ownuser;
    public  Socket socket;
    public JFrame ji;
    public Map<Integer,ChatTime> groupChat = new ConcurrentHashMap<Integer,ChatTime>();
    public static Map<Integer,ChatTime> privateChat = new ConcurrentHashMap<Integer,ChatTime>();

    public MainPage(){

    }
    public MainPage(User user){
        this.ownuser = user;
        //和服务端建立连接
        final Thread thread ;
        try {
            socket = new Socket("0.0.0.0",8889);
            writer = new OutputStreamWriter(socket.getOutputStream());
            reader = new InputStreamReader(socket.getInputStream());
            //吧用户ID发到服务器
            writer.write(String.valueOf(ownuser.getId()));
            writer.flush();
            thread =new Thread(new Runnable() {
                @Override
                public void run() {
                    listenMessage(socket);
                }
            });
            thread.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        ji = new JFrame(user.getName());
        Container container = ji.getContentPane();
        container.setBackground(Color.white);
        ji.setBounds(1370,130,400,750);
        FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
        ji.setLayout(f1);
        ImageIcon imageIcon = new ImageIcon("chatroomfile/qq.png");
        Image image = imageIcon.getImage().getScaledInstance(400,150,Image.SCALE_DEFAULT);
        ImageIcon imageIcon1 = new ImageIcon(image);
        JLabel s = new JLabel(imageIcon);
        s.setPreferredSize(new Dimension(370,150));
        ji.add(s);
       /* JList jList = new JList(list);
        jList.setPreferredSize(new Dimension(50,50));



        ji.add(jList);*/
       //一级目录表
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("群聊");

        //二级目录
        List<User> Friends = new LinkedList<User>();
        if(ownuser.getGroupChatList()!=null) {

            List<GroupChat> groupChatList = ownuser.getGroupChatList();
            for(int i =0 ;i<groupChatList.size();i++){
                ChatTime chatTime = new ChatTime(choice.群聊,ownuser,writer,groupChatList.get(i).getId());
                groupChat.put(groupChatList.get(i).getId(),chatTime);
                String string = groupChatList.get(i).getName();
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(string);
                rootNode.add(node);
            }
        }
        //创建树组件
        JTree tree = new JTree(rootNode);
        //
        // 设置树bu显示根节点句柄
        tree.setShowsRootHandles(false);

        // 设置树节点可编辑
        tree.setEditable(false);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println("当前被选中的节点: " + e.getPath());
                List<GroupChat> groupChatList = ownuser.getGroupChatList();
                //当对象有群聊的时候 groupChatList不为空
                String string[] = e.getPath().toString().split(" ");
                String name = string[1].substring(0,string[1].length()-1);
                if(groupChatList!=null) {
                    for (int i = 0; i < groupChatList.size(); i++) {
                        if (name.equals(groupChatList.get(i).getName())) {
                            groupChat.get(groupChatList.get(i).getId()).jFrame.setVisible(true);
                        }
                    }
                }
            }
        });
        //tree.setPreferredSize(new Dimension());
        ji.add(tree);
        //创建一个标签占据后面的空间
        final JLabel occupy = new JLabel();
        occupy.setPreferredSize(new Dimension(330,10));
        ji.add(occupy);

        DefaultMutableTreeNode rootNode1 = new DefaultMutableTreeNode("好友");
        java.util.List<User> friends = ownuser.getFriends();
        if(friends!=null){
            for(int i =0;i < friends.size() ;i++) {
                ChatTime chatTime = new ChatTime(choice.私聊,ownuser,writer,friends.get(i).getId());
                privateChat.put(friends.get(i).getId(),chatTime);
                String string = friends.get(i).getName();

                DefaultMutableTreeNode node = new DefaultMutableTreeNode(string);
                rootNode1.add(node);
            }
        }


        JTree tree1 = new JTree(rootNode1);
        //
        // 设置树bu显示根节点句柄
        tree1.setShowsRootHandles(false);

        // 设置树节点可编辑
        tree1.setEditable(false);
        tree1.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println("当前被选中的节点: " + e.getPath());
                List<User> friends = ownuser.getFriends();
                String string[] = e.getPath().toString().split(" ");
                String name = string[1].substring(0,string[1].length()-1);
                if(friends!=null){
                    for(int i=0;i<friends.size();i++){
                        if(name.equals(friends.get(i).getName())){
                            //通过id找到对应ChatTime
                            privateChat.get(friends.get(i).getId()).jFrame.setVisible(true);
                        }
                    }
                }
            }
        });
        //tree.setPreferredSize(new Dimension());
        ji.add(tree1);
        JLabel occupy1 = new JLabel();
        occupy1.setPreferredSize(new Dimension(330,10));
        ji.add(occupy1);
        JButton add = new JButton("添加好友");
        add.setPreferredSize(new Dimension(150,30));
        ji.add(add);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("123");
                Addfriends addfriends = new Addfriends(ownuser,socket);
            }
        });
        JButton jButton = new JButton("创建群聊");
        jButton.setPreferredSize(new Dimension(150,30));
        ji.add(jButton);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //群聊方法
                buildGroupChat();
            }
        });

        JButton jButton1 = new JButton("添加群聊");
        jButton1.setPreferredSize(new Dimension(150,30));
        ji.add(jButton1);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addGroupchat();
            }
        });
        JButton jButton2 = new JButton("刷新");
        jButton2.setPreferredSize(new Dimension(150,30));
        ji.add(jButton2);
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("123");
                //重新从服务端得到USER对象 实例化MainPage对象
                getNewMianPage(ownuser);
            }
        });
        ji.setVisible(true);
        ji.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.connect();
        ji.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                // TODO Auto-generated method stub
                //super.windowClosing(arg0);
                //try {
                //    if(clientSocket != null){
                //        clientSocket.close();
                //    }

                // } catch (IOException e) {
                //     // TODO Auto-generated catch block
                //    e.printStackTrace();
                // }
                // if(thread != null){
                //    thread.stop();
                // }
                try {
                    Message message = new Message("关闭请求","关闭",ownuser.getId());
                    String string = JSON.toJSONString(message);
                    writer.write(string);
                    writer.flush();
                    //socket.close();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }
    public static void main(String args[]){
        User ownuser = new User("觉",761702168);
        MainPage mainPage = new MainPage(ownuser);
    }
    public void getNewMianPage(User ownuser){
        try {
            //这是刷新的代码
            Message message = new Message("刷新","刷新",ownuser.getId());
            String string = JSON.toJSONString(message);
            writer.write(string);
            writer.flush();
            //重新获取用户的mainpage
            Socket clientSocket = new Socket("0.0.0.0", 8999);
            Writer writer = new OutputStreamWriter(clientSocket.getOutputStream());
            writer.write(ownuser.getId()+","+ownuser.getPassWords());
            writer.flush();
            Reader reader = new InputStreamReader(clientSocket.getInputStream());
            char chars[] = new char[1024];
            int len;
            User user;
            while ((len = reader.read(chars))!=-1){
                String message1 = new String(chars,0,len);

                user = JSON.parseObject(message1,User.class);
                new MainPage(user);
                System.out.println("已成功从服务器获取新用户");
                ji.dispose();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void listenMessage(Socket socket){
        char chars[]=new char[1024];
        int len;
        System.out.println("监听服务端信息");

        try {
            reader = new InputStreamReader(socket.getInputStream());//
            while ((len=reader.read(chars))!=-1){
                String string = new String(chars,0,len);
                System.out.println(string);
                Message message = JSON.parseObject(string,Message.class);
                if(message.type.equals("添加好友")){
                    //应该是申请人ID,他想添加的ID
                    //现在给服务端反馈 反馈发送者是被加好友的人   接收者是加好友的人
                    new Notice2(message.getSenderId()+"申请添加你为好友",writer,socket,ownuser.getId(),message.senderId);
                }
                else if(message.type.equals("添加好友反馈")){
                    new Notice("添加好友结果："+message.string);
                }
                else if(message.type.equals("创建群聊反馈")){
                    new Notice(message.string);
                }
                else if(message.type.equals("添加群聊反馈")){
                    new Notice(message.string);
                }
                else if(message.type.equals("私聊")){
                    //如果被私聊用户不在线则会得到反馈如果发送者的ID和此主页用户ID一样则说明是反馈信息
                    if(message.getSenderId()== ownuser.getId()){
                        //通过本应接收消息的那个用户的ID得到chattime窗口
                        int id = message.getAccepterId();
                        System.out.println(id);
                        privateChat.get(id).jTextArea.append(message.string+'\n');
                        privateChat.get(id).jTextArea.setCaretPosition(privateChat.get(id).jTextArea.getDocument().getLength());
                    }
                    else{
                        int id = message.getSenderId();
                        //senderid才是本用户的好友的ID
                        if(privateChat.get(id)!=null){
                            privateChat.get(id).jTextArea.append(getNameFromFriends(message.senderId)+"对你说:"+message.string+'\n');
                            privateChat.get(id).jTextArea.setCaretPosition(privateChat.get(id).jTextArea.getDocument().getLength());
                        }

                    }
                }
                else if(message.type.equals("群聊")){
                    //通过群聊id找到对应chattime
                    int id = message.getAccepterId();
                    if(ownuser.getId()!=message.getSenderId()) {
                        groupChat.get(id).jTextArea.append(getNameFromFriends(message.senderId) + "(" + message.getSenderId() + ")" + ":" + message.string + '\n');
                        groupChat.get(id).jTextArea.setCaretPosition(groupChat.get(id).jTextArea.getDocument().getLength());
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void buildGroupChat(){
        JFrame ji = new JFrame("创建群聊");
        Container container = ji.getContentPane();
        container.setBackground(Color.white);
        ji.setSize(540, 427);

        //设置界面为程序处于屏幕中央
        ji.setLocationRelativeTo(null);
        FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
        ji.setLayout(f1);
        JLabel jLabel = new JLabel();
        jLabel.setPreferredSize(new Dimension(540,90));
        ji.add(jLabel);
        JLabel jLabel1 = new JLabel();
        jLabel1.setPreferredSize(new Dimension(110,30));
        ji.add(jLabel1);
        JLabel jLabel2 = new JLabel("群聊ID");
        ji.add(jLabel2);
        final JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(220,30));
        ji.add(jTextField);
        JLabel jLabel3 = new JLabel();
        jLabel3.setPreferredSize(new Dimension(110,30));
        ji.add(jLabel3);
        JLabel jLabel4 = new JLabel();
        jLabel4.setPreferredSize(new Dimension(110,30));
        ji.add(jLabel4);

        JLabel jLabel5 = new JLabel("群聊名");
        ji.add(jLabel5);
        final JTextField jTextField1 = new JTextField();
        jTextField1.setPreferredSize(new Dimension(220,30));
        ji.add(jTextField1);
        JLabel jLabel6 = new JLabel();
        jLabel6.setPreferredSize(new Dimension(200,30));
        ji.add(jLabel6);
        JButton jButton = new JButton("创建");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String string = jTextField.getText() + "," + jTextField1.getText();
                    Message message = new Message("创建群聊", string, ownuser.getId());
                    String jsonString = JSON.toJSONString(message);
                    writer.write(jsonString);
                    writer.flush();
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        ji.add(jButton);
        ji.setVisible(true);
        ji.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public void addGroupchat(){
        JFrame jFrame = new JFrame("添加群聊");
        Container container = jFrame.getContentPane();
        container.setBackground(Color.white);
        jFrame.setSize(540, 427);

        //设置界面为程序处于屏幕中央
        jFrame.setLocationRelativeTo(null);
        FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
        jFrame.setLayout(f1);
        JLabel jLabel = new JLabel();
        jLabel.setPreferredSize(new Dimension(540,150));
        jFrame.add(jLabel);
        JLabel jLabel1 = new JLabel();
        jLabel1.setPreferredSize(new Dimension(110,30));
        jFrame.add(jLabel1);
        JLabel jLabel2 = new JLabel("群聊ID");
        jFrame.add(jLabel2);
        final JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(220,30));
        jFrame.add(jTextField);
        JLabel jLabel3 = new JLabel();
        jLabel3.setPreferredSize(new Dimension(210,30));
        jFrame.add(jLabel3);
        JLabel jLabel4 = new JLabel();
        jLabel4.setPreferredSize(new Dimension(1,30));
        jFrame.add(jLabel4);
        JButton jButton = new JButton("添加");
        jButton.setPreferredSize(new Dimension(80,30));
        jFrame.add(jButton);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ownuser.havaGroupChat(Integer.parseInt(jTextField.getText()))){
                    try {
                        Message message = new Message("添加群聊", jTextField.getText(), ownuser.getId());
                        String string = JSON.toJSONString(message);
                        writer.write(string);
                        writer.flush();
                    }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
                else{
                    new Notice("已经添加群聊");
                }
            }
        });
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
    public String getNameFromFriends(int id){
        List<User> friends = ownuser.getFriends();
        for(int i=0;i<friends.size();i++){
            if(id == friends.get(i).getId()){
                return friends.get(i).getName();
            }
        }
        return "不知道是谁";
    }

}
