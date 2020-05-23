package ClientSide;
import Server.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import javax.swing.*;
import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Login  extends JFrame{
    Socket socket;

    public Login(){
        ParserConfig.getGlobalInstance().setAsmEnable(false);
        final boolean logined;
        final JFrame ji = new JFrame("登录界面");
        Container container = ji.getContentPane();
        container.setBackground(Color.white);
        ji.setSize(540,427);

        //设置界面为程序处于屏幕中央
        ji.setLocationRelativeTo(null);
        FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
        ji.setLayout(f1);
       //URL url = Login.class.getResource("/chatroomfile/login.png");
        //导入图片
        ImageIcon login = new ImageIcon("chatroomfile/login.png");
        Image img=login.getImage().getScaledInstance(540, 227, Image.SCALE_DEFAULT);
        ImageIcon login1 = new ImageIcon(img);
        JLabel pic1 = new JLabel(login1);
        //添加入容器

        //pic1.setBounds(0,100,540,427);
        pic1.setPreferredSize(new Dimension(540,200));
        ji.add(pic1);




        // 创建一个空的JLabel，它的长度宽度为110,30，因为窗口是流式左对齐，为了将”账号”一栏添加在正中间，所以左侧由空的JLabel填充
        JLabel name1 = new JLabel();
        // 设置空JLabel长度大小，此处不能使用setSize设置大小，setSize只能设置顶级容器大小，此处用setPreferredSize，Dimension给出大小，
        name1.setPreferredSize(new Dimension(110, 30));
        ji.add(name1);
        // 同上，此处添加的不是空JLabel，而是内容为“账号”的JLabel
        final JLabel name = new JLabel("账号：");
        ji.add(name);
        // JTextField在窗口上添加一个可输入可见文本的文本框，需要添加的包名为javax.swing.JTextField.
        final JTextField nametext = new JTextField();

        nametext.setPreferredSize(new Dimension(220, 30));
        ji.add(nametext);
        // 同name1
        JLabel name2 = new JLabel();
        name2.setPreferredSize(new Dimension(110, 30));
        ji.add(name2);

        // 同name1
        JLabel name3 = new JLabel();
        name3.setPreferredSize(new Dimension(110, 30));
        ji.add(name3);

        // 同name
        JLabel password = new JLabel("密码：");
        ji.add(password);

        // JPasswordField创建一个密码文本框，里面输入的文本是不可见的，其他同nametext
        final JPasswordField passwordtext = new JPasswordField();
        passwordtext.setPreferredSize(new Dimension(220, 30));
        ji.add(passwordtext);

        // 同name1
        JLabel name4 = new JLabel();
        name4.setPreferredSize(new Dimension(110, 30));
        ji.add(name4);

        // 同name1
        JLabel name5 = new JLabel();
        name5.setPreferredSize(new Dimension(170, 30));
        ji.add(name5);

        JButton loginButton = new JButton("登陆");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String ID = nametext.getText();


                //char[] passWords = passwordtext.getPassword();
                //String stringPassWords = passwordtext.getText();
                //User ownuser = new User("觉",761702168);
                    //MainPage a = new MainPage(123,"觉",ownuser);
                try {
                    //System.out.println(passwordtext.getText());
                    //sendMessage(id.getText()+","+name.getText()+","+passwordField.getText());
                    Socket clientSocket = new Socket("0.0.0.0", 8999);
                    Writer writer = new OutputStreamWriter(clientSocket.getOutputStream());
                    writer.write(nametext.getText()+","+passwordtext.getText());
                    writer.flush();
                    Reader reader = new InputStreamReader(clientSocket.getInputStream());
                    char chars[] = new char[1024];
                    int len;
                    User user;
                    while ((len = ((Reader) reader).read(chars)) != -1){
                        String message = new String(chars,0,len);
                        System.out.println(message);
                        if(message.equals("错误")){
                            //System.out.println("C");
                            new Notice("账号或者密码错误");
                        }
                        else if(message.equals("该账号已经登陆")){
                            new Notice("该账号已经登陆");
                        }
                        else {

                            user = JSON.parseObject(message,User.class);

                            new MainPage(user);
                            ji.dispose();
                        }
                    }
                    reader.close();
                    //System.out.println("12314555");
                    writer.close();
                    clientSocket.close();
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        loginButton.setPreferredSize(new Dimension(80,30));
        ji.add(loginButton);
        JLabel name6 = new JLabel();
        name6.setPreferredSize(new Dimension(5, 30));
        ji.add(name6);
        JButton loginButton1 = new JButton("注册");
        loginButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    Register re = new Register();

                }
                catch (Exception e1){
                        e1.printStackTrace();
                    }
            }
        });
        loginButton1.setPreferredSize(new Dimension(80,30));
        ji.add(loginButton1);
        JLabel name7 = new JLabel();
        name7.setPreferredSize(new Dimension(170, 30));
        ji.add(name7);

        ji.setVisible(true);
        ji.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);




    }
    public static void main(String args[]){
        Login lo = new Login();
    }





}
   class Register extends JFrame {

       public Register() {

           JFrame ji = new JFrame("注册");
           Container container = ji.getContentPane();
           container.setBackground(Color.white);
           ji.setSize(540, 427);

           //设置界面为程序处于屏幕中央
           ji.setLocationRelativeTo(null);
           FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
           ji.setLayout(f1);
           JLabel jLabel1 = new JLabel();
           jLabel1.setPreferredSize(new Dimension(540, 75));
           ji.add(jLabel1);
           JLabel jLabel2 = new JLabel();
           jLabel2.setPreferredSize(new Dimension(110, 30));
           ji.add(jLabel2);
           JLabel jLabe3 = new JLabel("账号:");
           ji.add(jLabe3);
           final JTextField id = new JTextField();
           id.setPreferredSize(new Dimension(220, 30));
           ji.add(id);
           JLabel jLabe4 = new JLabel();
           jLabe4.setPreferredSize(new Dimension(110, 30));
           ji.add(jLabe4);
           JLabel jLabe5 = new JLabel();
           jLabe5.setPreferredSize(new Dimension(110, 30));
           ji.add(jLabe5);
           JLabel jLabe6 = new JLabel("昵称:");
           ji.add(jLabe6);
           final JTextField name = new JTextField();
           name.setPreferredSize(new Dimension(220, 30));
           ji.add(name);
           JLabel jLabel7 = new JLabel();
           jLabel7.setPreferredSize(new Dimension(110, 30));
           ji.add(jLabel7);
           JLabel jLabel8 = new JLabel();
           jLabel8.setPreferredSize(new Dimension(110, 30));
           ji.add(jLabel8);
           JLabel jLabel9 = new JLabel("密码:");
           ji.add(jLabel9);
           final JPasswordField passwordField = new JPasswordField();
           passwordField.setPreferredSize(new Dimension(220, 30));
           ji.add(passwordField);
           JLabel jLabel10 = new JLabel();
           jLabel10.setPreferredSize(new Dimension(110, 30));
           ji.add(jLabel10);
           JLabel jLabel11 = new JLabel();
           jLabel11.setPreferredSize(new Dimension(200, 30));
           ji.add(jLabel11);
           JButton jb = new JButton("注册");
           jb.setPreferredSize(new Dimension(80, 30));
           jb.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   try {
                       //sendMessage(id.getText()+","+name.getText()+","+passwordField.getText());
                       Socket clientSocket = new Socket("0.0.0.0", 8899);
                       Writer writer = new OutputStreamWriter(clientSocket.getOutputStream());
                       User user = new User(Integer.parseInt(id.getText()),name.getText(),passwordField.getText());
                       String jsonstring = JSON.toJSONString(user);
                       //String jsonstring = JSON.toJSONString(user, SerializerFeature.WriteMapNullValue);


                       writer.write(jsonstring);
                       System.out.println("已成功向服务器发送消息："+jsonstring);
                       writer.flush();
                       Reader reader = new InputStreamReader(clientSocket.getInputStream());
                       char chars[] =new char[64];
                       int len;
                       while ((len = reader.read(chars))!=-1){
                           String string = new String(chars,0,len);
                           if(string.equals("失败")) {
                               new Notice(string + ":此账号已被人注册");
                           }
                           else {
                               new Notice(string);
                           }
                       }
                       writer.close();
                       clientSocket.close();
                   }
                   catch (Exception e1){
                       e1.printStackTrace();
                   }
               }
           });
           ji.add(jb);

           ji.setVisible(true);
           ji.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


       }
   }


