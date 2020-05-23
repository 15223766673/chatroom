package Server;
import ClientSide.ChatTime;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.*;

import ClientSide.Message;
import ClientSide.*;


import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.*;

public class Server extends JFrame {

    public JTextArea jTextArea;
    public ConnectToDatabase connectToDatabase;
    protected Map<Integer, Socket> clients = new HashMap<Integer, Socket>();
    private java.util.List<User> users;


    public Server() {
        ParserConfig.getGlobalInstance().setAsmEnable(false);
        //连接数据库,所有与数据库的操作通过ConnectToDatabase的对象完成
        connectToDatabase = new ConnectToDatabase();
        //
        //获取文档中的对象并未集合
        //users = User.inputUsers();

        //  创建界面
        JFrame jFrame = new JFrame("服务端");
        Container container = jFrame.getContentPane();
        container.setBackground(Color.white);
        jFrame.setSize(840,627);
        jFrame.setLocationRelativeTo(null);

        //分割面板
        JSplitPane jSplitPane = new JSplitPane();
        jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jSplitPane.setDividerLocation(400);
        jSplitPane.setDividerSize(10);
        jFrame.add(jSplitPane);
        //第二次分割面板，上面
        JSplitPane jSplitPane1 = new JSplitPane();
        jSplitPane1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane1.setDividerLocation(600);
        jSplitPane1.setDividerSize(10);
        jSplitPane.setTopComponent(jSplitPane1);
        //创建面板
//        JPanel jPanel = new JPanel();
        FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
//        jPanel.setLayout(f1);
//        jPanel.setSize(840,400);
        jTextArea = new JTextArea();
        JScrollPane myPanel1 = new JScrollPane(jTextArea);
        //设置垂直滚动条
        myPanel1.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        myPanel1.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        myPanel1.setSize(840,400);
        //将滚动面板添加到上方面板的左方
        jSplitPane1.setLeftComponent(myPanel1);
        //创建文本域作为服务器日志

        jTextArea.setEditable(false);
        //jTextArea.setPreferredSize(new Dimension(580,390));
        //在面板的下面创建几个按钮
        JPanel jPanel2 = new JPanel();
        jPanel2.setSize(840,227);
        jPanel2.setLayout(f1);
        jSplitPane.setBottomComponent(jPanel2);
        //按钮S
        JButton jButton1 = new JButton("发布公告");
        jButton1.setPreferredSize(new Dimension(100,40));
        jPanel2.add(jButton1);
        //2
        JButton jButton2 = new JButton("ban");
        jButton2.setPreferredSize(new Dimension(100,40));
        jPanel2.add(jButton2);
        //在右上方添加在线列表
        //listModel = new DefaultListModel();
        //userList = new JList(listModel);

        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //建立连接
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    getRegister();
            }
        });
        thread.start();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                getLogin();
            }
        });
        thread1.start();
        this.listenClient();










    }
    public static void main(String args[]){
        Server server = new Server();


    }

    public void sendMsgToAll(Socket fromSocket, String msg) {
        Set<Integer> keset = this.clients.keySet();
        java.util.Iterator<Integer> iter = keset.iterator();
        while(iter.hasNext()){
            int key = iter.next();
            Socket socket = clients.get(key);
            if(socket != fromSocket){
                try {
                    if(socket.isClosed() == false){
                        if(socket.isOutputShutdown() == false){

                            Writer writer = new OutputStreamWriter(
                                    socket.getOutputStream());
                            writer.write(msg);
                            writer.flush();
                        }

                    }
                } catch (SocketException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }
    public void getRegister(){
        int port = 8899;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                try {
                    InputStreamReader reader = null;
                    Writer writer;
                    char chars[] = new char[64];
                    int len;

                    System.out.println("等待注册信息");
                    Socket socket = serverSocket.accept();
                    reader = new InputStreamReader(socket.getInputStream());
                    writer = new OutputStreamWriter(socket.getOutputStream());
                    String temp = "注册端" + socket.getPort() + "连接" + '\n';
                    jTextArea.append(temp);
                    while ((len = ((Reader) reader).read(chars)) != -1) {

                        String s = new String(chars,0,len);
                        User user = JSON.parseObject(s,User.class);
                        //注册成功则返回ture,这行很重要
                        Boolean register=connectToDatabase.outUser(user);
                        if(register){
                            writer.write("成功");
                            writer.flush();
                            jTextArea.append("注册成功");
                        }
                        else {
                            writer.write("失败");
                            writer.flush();
                            jTextArea.append("注册失败");
                        }
                        temp = User.userToString(user);
                        jTextArea.append(temp + '\n');
                        break;
                    }
                    //关闭套接字和流
                    reader.close();
                    writer.close();
                    socket.close();
                    jTextArea.append("成功处理一个注册信息");
                    jTextArea.append("注册端" + socket.getPort() + "关闭");


                } catch (Exception e) {
                    e.printStackTrace();
                    jTextArea.append("连接异常");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void getLogin() {

        try {
            int port = 8999;
            ServerSocket serverSocket1 = new ServerSocket(port);

            while (true) {


                try {
                    InputStreamReader reader = null;
                    char chars[] = new char[64];
                    int len;
                    System.out.println("等待登陆");
                    Socket socket = serverSocket1.accept();
                    List<User> users = connectToDatabase.getUser();
                    reader = new InputStreamReader(socket.getInputStream());
                    System.out.println("登录端已连接");
                    String temp = "登陆端" + socket.getPort() + "连接" + '\n';
                    jTextArea.append(temp);
                    while ((len = ((Reader) reader).read(chars)) != -1) {
                        //这个string  应该是id,passwords形式
                        String string = new String(chars, 0, len);
                        jTextArea.append("用户尝试登陆"+string+'\n');
                        String s[] = string.split(",");
                        int i;
                        for (i = 0; i < users.size(); i++) {

                            if (s[0].equals(String.valueOf(users.get(i).getId())) && s[1].equals(users.get(i).getPassWords())) {
                                //如果没登陆则返回用户信息
                                if(!isLogined(users.get(i).getId())) {
                                    String string1 = users.get(i).getId() + "登陆成功" + '\n';
                                    jTextArea.append(string1);
                                    //吧该用户的字节传到Login界面
                                    Writer writer = new OutputStreamWriter(socket.getOutputStream());
                                    String jsonstring = JSON.toJSONString(users.get(i));
                                    jTextArea.append(jsonstring + '\n');
                                    jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
                                    writer.write(jsonstring);
                                    writer.flush();
                                    break;
                                    //输出信息
                                }
                                //如果已经登陆则返回已经登陆的信息
                                else{
                                    System.out.println("已经登陆了");
                                    Writer writer= new OutputStreamWriter(socket.getOutputStream());
                                    writer.write("该账号已经登陆");
                                    writer.flush();
                                    break;
                                }

                            }

                        }
                        if (i == users.size()) {
                            System.out.println("账号或者密码错误");
                            Writer writer = new OutputStreamWriter(socket.getOutputStream());
                            writer.write("错误");
                            writer.flush();

                        }

                        break;
                    }
                    socket.close();
                    System.out.println("登录端关闭");
                    jTextArea.append("登录端" + socket.getPort() + "关闭"+'\n');
                    jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
                } catch (Exception e) {
                    e.printStackTrace();
                    jTextArea.append("登陆异常");
                }

            }
        }


        catch (Exception e){
            e.printStackTrace();
        }


    }
    public boolean isLogined(int id){
        Set<Integer> keySet = clients.keySet();
        java.util.Iterator<Integer> iter = keySet.iterator();
        while (iter.hasNext()){
            if(id == iter.next()){
                return true;
            }
        }
        return false;

    }
    public void listenClient(){
        int port = 8889;
        try {

            ServerSocket server = new ServerSocket(port);
            // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
            while (true) {
                System.out.println("服务器端正在监听");
                System.out.println(server.getInetAddress());
                Socket socket = server.accept();
                Reader reader = new InputStreamReader(socket.getInputStream());
                char chars[] = new char[64];
                int len;
                len = reader.read(chars);
                    int socket_id = Integer.parseInt(new String(chars,0,len));
                    //吧客户端用户ID与套接字放到集合
                    clients.put(socket_id, socket);
                    String temp = "客户端Id:" + socket_id + ":连接" + '\n';
                    jTextArea.append(temp);
                jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
                    new mythread(socket, this,socket_id,connectToDatabase).start();

            }


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }



}
class mythread extends Thread{
    private int id;
    private Socket socket = null;
    private Server server = null;
    private Reader reader = null;
    private Writer writer = null;
    private ConnectToDatabase connectToDatabase;
    //保证接收消息完整
    char chars[] = new char[1024];
    int len;
    private String temp = null;
    public mythread(Socket socket, Server server,int id,ConnectToDatabase connectToDatabase) {
        // TODO Auto-generated constructor stub
        this.socket = socket;
        this.server = server;
        this.id = id;
        this.connectToDatabase = connectToDatabase;
        try {
            reader = new InputStreamReader(socket.getInputStream());
            writer = new OutputStreamWriter(socket.getOutputStream());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println(socket.getPort()+"的子线程开始工作");
            try {
                //
                while (((len = ((Reader) reader).read(chars)) != -1)) {

                    temp = new String(chars, 0, len);

                    try {
                        System.out.println(temp);
                        Message message = JSON.parseObject(temp,Message.class);

                        String string[] = message.string.split(",");
                        //应该给message 加上发出者和接收者id属性
                        if (message.type.equals("添加好友")) {
                            addFriend(message);

                        } else if (message.type.equals("添加好友反馈")) {
                            System.out.println(message.string);

                            //反馈应发给前一个人
                            Socket socket = getSocketFromId(message.getAccepterId());
                            Writer writer = new OutputStreamWriter(socket.getOutputStream());
                            writer.write(temp);
                            writer.flush();
                            if(message.string.equals("同意")){
                                //执行成功返回true
                                if(connectToDatabase.addfriend(message.senderId,message.accepterId)) {
                                        System.out.println(message.senderId+"与"+message.accepterId+"成为好友");
                                }
                            }

                        }
                        else if(message.type.equals("创建群聊")){
                            handlingCreatingGroupChat(message);
                        }
                        else if(message.type.equals("关闭请求")){
                            server.clients.remove(message.getSenderId());
                            temp = "客户端"+socket.getPort()+":退出";
                            server.jTextArea.append(temp);
                            socket.close();
                            break;
                        }
                        else if(message.type.equals("添加群聊")){
                            //使用senderid和string(message的属性)完成添加群聊功能，若成功则返回ture
                            if(connectToDatabase.addGroupChat(Integer.parseInt(message.string),message.senderId)){
                                Message message1 = new Message("添加群聊反馈",message.senderId,"添加群聊成功");
                                String string1 = JSON.toJSONString(message1);
                                writer.write(string1);
                                writer.flush();
                            }
                            else {
                                Message message1 = new Message("添加群聊反馈",message.senderId,"添加群聊失败");
                                String string1 = JSON.toJSONString(message1);
                                writer.write(string1);
                                writer.flush();
                            }
                        }
                        else if(message.type.equals("私聊")){
                            Socket socket;
                            if((socket=getSocketFromId(message.accepterId))!=null){
                                //传给被发送者的
                                Writer writer = new OutputStreamWriter(socket.getOutputStream());
                                writer.write(temp);
                                writer.flush();
                            }
                            else{
                                Message message1 = new Message("私聊","对方不在线",message.senderId,message.accepterId);
                                String string1 = JSON.toJSONString(message1);
                                writer.write(string1);
                                writer.flush();
                            }
                        }
                        else if(message.type.equals("群聊")){
                            handlingGroupChat(message);
                        }
                        else if(message.type.equals("刷新")){
                            server.clients.remove(message.senderId);
                            server.jTextArea.append(message.senderId+"已退出");
                            socket.close();
                            break;
                        }
                        server.jTextArea.setCaretPosition(server.jTextArea.getDocument().getLength());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    //System.out.println("来自客户端" + socket.getPort() + "的消息:" + temp);
                    //server.jTextArea.append("来自客户端" + socket.getPort() + "的消息:" + temp+'\n');
                    //server.sendMsgToAll(this.socket, "客户端" + socket.getPort() + "说:" + temp);
                    //server.sendMsgToAll(this.socket, "客户端" + socket.getPort() + "说:" + temp);
                }


                }

            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();

                try {
                    //发生异常关闭套接字
                    socket.close();
                    temp = "客户端"+socket.getPort()+":退出";
                    server.jTextArea.append(temp);
                    server.clients.remove(id);
                    System.out.println("已经吧集合中的断开连接的移除");
                    System.out.println("异常已经处理");
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }


        }
    }
    //如果map集合里面没有这个id则说明该用户没有登陆并返回空值  否则返回对应socket
    public Socket getSocketFromId(int id){
        Set<Integer> keset = server.clients.keySet();
        java.util.Iterator<Integer> iter = keset.iterator();
        //遍历连接的客户端集合
        while (iter.hasNext()){
            if(id == iter.next()){
                return server.clients.get(id);
            }
        }
        return null;

    }

    //处理来自客户端的添加好友请求
    public void addFriend(Message message){
        try {

            String string1 = JSON.toJSONString(message);
            Socket socket;
            //如果added在client集合中 则返回对应套接字，否则返回null
            if ((socket = getSocketFromId(message.getAccepterId())) != null) {
                Writer writer = new OutputStreamWriter(socket.getOutputStream());
                writer.write(string1);
                writer.flush();
                System.out.println("已经向被添加好友者发送请求");
            }
            else {
                Message message1 = new Message("添加好友反馈","对方不在线");
                String string = JSON.toJSONString(message1);
                writer.write(string);
                writer.flush();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handlingCreatingGroupChat(Message message){
        String string[] =message.string.split(",");
        if(connectToDatabase.groupIdExist(Integer.parseInt(string[0]))){
            try {
                Message message1 = new Message("创建群聊反馈", message.senderId, "群聊ID已经存在");
                String string1 = JSON.toJSONString(message1);
                writer.write(string1);
                writer.flush();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else {
            //string[0]是群聊ID string[1]是群聊名称 message.senderid是创建群聊的人
            if(connectToDatabase.outGroupChat(message.senderId,string[1],Integer.parseInt(string[0]))){
                try {
                    Message message1 = new Message("创建群聊反馈", message.senderId, "创建群聊成功");
                    String string1 = JSON.toJSONString(message1);
                    writer.write(string1);
                    writer.flush();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Message message1 = new Message("创建群聊反馈",message.senderId,"创建失败");
                    String string1 = JSON.toJSONString(message1);
                    writer.write(string1);
                    writer.flush();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            //吧信息写进数据库
        }
    }
    public void handlingGroupChat(Message message){
        GroupChat groupChat = connectToDatabase.getGroupChatHaveUsers(message.getAccepterId());
        List<User> groupChatUsers = groupChat.getUserList();

        //尝试发送给群聊中的每一个人
        for(int i = 0;i<groupChatUsers.size();i++){
            Socket socket;
            System.out.println("该群聊中有"+groupChatUsers.get(i).getId());
            if((socket=getSocketFromId(groupChatUsers.get(i).getId()))!=null){
                try {
                    Writer writer = new OutputStreamWriter(socket.getOutputStream());
                    String string = JSON.toJSONString(message);
                    writer.write(string);
                    writer.flush();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}

