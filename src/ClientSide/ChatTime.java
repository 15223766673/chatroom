package ClientSide;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.util.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import Server.*;
import com.alibaba.fastjson.JSON;

import java.util.concurrent.ThreadPoolExecutor;

enum choice{群聊,私聊};
public class ChatTime extends JFrame {
    public JTextArea jTextArea;
    public JTextArea send;
    public Writer writer;
    public User ownuser;
    public JFrame jFrame;
    public int acceptid;

    public ChatTime(choice choice, final User ownuser, final Writer writer, final int acceptid){
        this.ownuser = ownuser;
        this.writer = writer;
        this.acceptid = acceptid;
        switch (choice) {
            case 群聊: {
                String string = "聊天室";
                 jFrame = new JFrame(string);
                /*
                Container container = jFrame.getContentPane();
                container.setBackground(Color.white);*/
                jFrame.setSize(840, 627);

                //设置界面为程序处于屏幕中央
                jFrame.setLocationRelativeTo(null);

                FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
                //jFrame.setLayout(f1);
                JSplitPane vSplitPane = new JSplitPane();
                vSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                vSplitPane.setDividerLocation(400);
                vSplitPane.setDividerSize(10);
                jFrame.add(vSplitPane);
                JSplitPane hSplitPane = new JSplitPane();
                hSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                hSplitPane.setDividerSize(10);
                vSplitPane.setTopComponent(hSplitPane);
                hSplitPane.setDividerLocation(600);
                //vSplitPane.setLayout(f1);
                //创建一个面板包含一个文本域和一个按钮来发送数据
                JPanel myPanel = new JPanel();
                myPanel.setSize(840,200);
                myPanel.setLayout(f1);
                //创建文本域
                send = new JTextArea();
                send.setPreferredSize(new Dimension(500,60));
                myPanel.add(send);
                //创建并添加一个按钮
                JButton jButton = new JButton("发送");
                jButton.setPreferredSize(new Dimension(60,50));
                jButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            try {
                                Message message = new Message("群聊", send.getText(), ownuser.getId(), acceptid);

                                String string = JSON.toJSONString(message);
                                jTextArea.append("我说:"+send.getText()+'\n');
                                send.setText("");
                                //设置光标到最后一行
                                jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
                                writer.write(string);
                                writer.flush();
                            }
                            catch (Exception e1){
                                e1.printStackTrace();
                            }


                    }
                });
                myPanel.add(jButton);
                //添加一个面板包含一个文本域显示信息
//                JPanel myPanel1 = new JPanel();
//                myPanel1.setSize(600,600);
//                myPanel.setLayout(f1);
                jTextArea = new JTextArea();
                JScrollPane myPanel1 = new JScrollPane(jTextArea);
                //设置垂直滚动条
                myPanel1.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                myPanel1.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                myPanel1.setSize(840,400);
                //将滚动面板添加到上方面板的左方
                hSplitPane.setLeftComponent(myPanel1);

                //accept.append("测试的信息");
                jTextArea.setEditable(false);

                //jTextArea.setPreferredSize(new Dimension(580,390));
                hSplitPane.setLeftComponent(myPanel1);


                vSplitPane.setBottomComponent(myPanel);
                jFrame.setVisible(false);

                jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                jFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent arg0) {
                        try {

                            jFrame.setVisible(false);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                //添加窗口事件


                break;
            }
            case 私聊:{
                String string = "私聊";
                jFrame = new JFrame(string);
                /*
                Container container = jFrame.getContentPane();
                container.setBackground(Color.white);*/
                jFrame.setSize(840, 627);

                //设置界面为程序处于屏幕中央
                jFrame.setLocationRelativeTo(null);

                FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
                //jFrame.setLayout(f1);
                JSplitPane vSplitPane = new JSplitPane();
                vSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                vSplitPane.setDividerLocation(400);
                vSplitPane.setDividerSize(10);
                jFrame.add(vSplitPane);
                JSplitPane hSplitPane = new JSplitPane();
                hSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                hSplitPane.setDividerSize(10);
                vSplitPane.setTopComponent(hSplitPane);
                hSplitPane.setDividerLocation(600);
                //vSplitPane.setLayout(f1);
                //创建一个面板包含一个文本域和一个按钮来发送数据
                JPanel myPanel = new JPanel();
                myPanel.setSize(840,200);
                myPanel.setLayout(f1);

                //创建文本域
                send = new JTextArea();
                send.setPreferredSize(new Dimension(500,60));
                myPanel.add(send);
                //创建并添加一个按钮
                JButton jButton = new JButton("发送");
                jButton.setPreferredSize(new Dimension(60,50));
                jButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Message message = new Message("私聊", send.getText(), ownuser.getId(), acceptid);
                            String string1 = JSON.toJSONString(message);
                            jTextArea.append("我说:"+send.getText()+'\n');
                            send.setText("");
                            jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
                            writer.write(string1);
                            writer.flush();
                        }
                        catch (Exception e1){
                            e1.printStackTrace();
                        }


                    }
                });
                myPanel.add(jButton);
                //添加一个面板包含一个文本域显示信息
//                JPanel myPanel1 = new JPanel();
//                myPanel1.setSize(600,600);
//                myPanel.setLayout(f1);
                jTextArea = new JTextArea();
                JScrollPane myPanel1 = new JScrollPane(jTextArea);
                //设置垂直滚动条
                myPanel1.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                myPanel1.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                myPanel1.setSize(840,400);
                //将滚动面板添加到上方面板的左方

                //accept.append("测试的信息");
                jTextArea.setEditable(false);
                //jTextArea.setPreferredSize(new Dimension(580,390));
                hSplitPane.setLeftComponent(myPanel1);


                vSplitPane.setBottomComponent(myPanel);
                jFrame.setVisible(false);

                jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                jFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent arg0) {
                        try {

                            jFrame.setVisible(false);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });



            }
        }

    }

}




