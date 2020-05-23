package Server;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class GroupChat {
    public User builduser;
    public List<User> userList;
    public int id;
    public String name;
//    public JTextField jTextField;
//
//    public JTextField getjTextField() {
//        return jTextField;
//    }
//
//    public void setjTextField(JTextField jTextField) {
//        this.jTextField = jTextField;
//    }

    public List<User> getUserList() {
        return userList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public User getBuilduser() {
        return builduser;
    }

    public void setBuilduser(User builduser) {
        this.builduser = builduser;
    }


    //只是创建群聊的时候使用这个方法
    //获取用户集合与添加新用户都与数据库进行交互，相关方法在ConnectToDatabase中
    public GroupChat(User builduser,int id,String name){
        this.builduser =builduser;
        this.id = id;
        this.name = name;
    }
    public GroupChat(){

    }
}
