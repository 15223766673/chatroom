package ClientSide;

import com.alibaba.fastjson.JSON;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.jar.JarEntry;

public class Notice2 extends JFrame {
    public Writer writer;
    public Socket socket;
    public Notice2(String S, final Writer writer, final Socket socket, final int senderId, final int accepterId){
        this.writer =writer;
        final JFrame jFrame = new JFrame();
        FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
        jFrame.setLayout(f1);
        jFrame.setTitle("好友申请通知");
        jFrame.setSize(300,200);
        jFrame.setLocationRelativeTo(null);

        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel jLabel = new JLabel(S);
        jLabel.setPreferredSize(new Dimension(300,30));
        jFrame.add(jLabel);
        JLabel jLabe2 = new JLabel();
        jLabe2.setPreferredSize(new Dimension(300,30));
        jFrame.add(jLabe2);
        JLabel jLabe3 = new JLabel();
        jLabe3.setPreferredSize(new Dimension(300,30));
        jFrame.add(jLabe3);
        JLabel jLabe4 = new JLabel();
        jLabe4.setPreferredSize(new Dimension(40,30));
        jFrame.add(jLabe4);
        //JLabel jLabe5 = new JLabel(S);
        //jLabe5.setPreferredSize(new Dimension(300,30));
        //jFrame.add(jLabe5);
        JButton jButton = new JButton("同意");
        jButton.setPreferredSize(new Dimension(80,40));
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Message message = new Message("添加好友反馈","同意",senderId,accepterId);
                    String string = JSON.toJSONString(message);
                    System.out.println(string);
                    writer.write(string);
                    writer.flush();
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
                jFrame.dispose();
            }
        });
        jFrame.add(jButton);
        JLabel jLabe5 = new JLabel();
        jLabe5.setPreferredSize(new Dimension(20,30));
        jFrame.add(jLabe5);
        JButton jButton2 = new JButton("拒绝");
        jButton2.setPreferredSize(new Dimension(80,40));
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Message message = new Message("添加好友反馈","拒绝",senderId,accepterId);
                    String string = JSON.toJSONString(message);
                    writer.write(string);
                    writer.flush();
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
                jFrame.dispose();

            }
        });
        jFrame.add(jButton2);

        jFrame.setVisible(true);
    }

}