package ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarEntry;

public class Notice extends JFrame {

    public Notice(String S){
        final JFrame jFrame = new JFrame();
        FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
        jFrame.setLayout(f1);
        jFrame.setTitle("通知");
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
        jLabe4.setPreferredSize(new Dimension(100,30));
        jFrame.add(jLabe4);
        //JLabel jLabe5 = new JLabel(S);
        //jLabe5.setPreferredSize(new Dimension(300,30));
        //jFrame.add(jLabe5);
        JButton jButton = new JButton("确定");
        jButton.setPreferredSize(new Dimension(80,40));
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }
        });
        jFrame.add(jButton);
        jFrame.setVisible(true);
    }
    public static void main(String args[]){
        new Notice("注意");
    }
}
