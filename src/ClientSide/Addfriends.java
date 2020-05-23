package ClientSide;

import Server.User;
import com.alibaba.fastjson.JSON;
import sun.awt.im.InputMethodJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

public class Addfriends extends JInternalFrame{
    User ownUser;
    Writer writer;
    public JTextField id;
    public Socket socket;
    public Addfriends(User ownUser,Socket socket){
        this.ownUser = ownUser;
        this.socket = socket;
        JFrame ji = new JFrame("添加好友");
        Container container = ji.getContentPane();
        container.setBackground(Color.white);
        ji.setSize(540,427);

        //设置界面为程序处于屏幕中央
        ji.setLocationRelativeTo(null);
        FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
        ji.setLayout(f1);
        JLabel jLabel = new JLabel();
        jLabel.setPreferredSize(new Dimension(540,75));
        ji.add(jLabel);
        JLabel jLabel2 = new JLabel();
        jLabel2.setPreferredSize(new Dimension(110,30));
        ji.add(jLabel2);
        JLabel jLabel3 = new JLabel("ID：");
        ji.add(jLabel3);
        id = new JTextField();
        id.setPreferredSize(new Dimension(220,30));
        ji.add(id);
        JLabel jLabel4 = new JLabel();
        jLabel4.setPreferredSize(new Dimension(110,30));
        ji.add(jLabel4);
        JLabel jLabel5 = new JLabel();
        jLabel5.setPreferredSize(new Dimension(200,30));
        ji.add(jLabel5);
        JButton jButton = new JButton("添加");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMethod();
            }
        });
        jButton.setPreferredSize(new Dimension(80,30));
        ji.add(jButton);
        ji.setVisible(true);
        ji.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
    public void addMethod(){
        try {
            Writer writer = new OutputStreamWriter(socket.getOutputStream());
            String string = ownUser.getId()+","+id.getText();
            //不是好友则执行
            if(!ownUser.isFriend(Integer.parseInt(id.getText()))) {
                Message message = new Message("添加好友", string, ownUser.getId(), Integer.parseInt(id.getText()));
                String jsonstring = JSON.toJSONString(message);
                System.out.println(jsonstring);
                //将添加好友请求发送到服务器
                writer.write(jsonstring);
                writer.flush();
            }
            else {
                new Notice("对方已经是你的好友");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
