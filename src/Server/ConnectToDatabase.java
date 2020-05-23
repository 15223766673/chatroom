package Server;
import com.alibaba.fastjson.JSON;

import java.sql.*;
import java.util.*;

public class ConnectToDatabase {
    static Connection connection;
    static PreparedStatement sql;

    public Connection getConnection(){
        return this.connection;
    }
    public ConnectToDatabase(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatroom?serverTimezone=UTC","root","123456");
            System.out.println("连接数据库成功");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //public List<User> getusers(){

    //}
    public boolean outUser(User user){
        //如果ID不重复 注册
        if(!existUser(user)) {
            try {
                String string = User.userToString(user);
                String string1[] = string.split(",");
                sql = connection.prepareStatement("INSERT INTO user_tbl values (?,?,?)");
                sql.setString(1, string1[0]);
                sql.setString(2, string1[1]);
                sql.setString(3, string1[2]);
                sql.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //ID 重复  返回失败
        return false;
    }
    public boolean existUser(User user){
        List<User> users = getUser();
        for (int i = 0;i<users.size();i++){
            if(user.getId() == users.get(i).getId()){
                return true;
            }
        }
        return false;
    }
    public List<User> getUser(){

        try {
            sql = connection.prepareStatement("select * from user_tbl");
            //ResultSet  不能设为静态变量  否则getgroupchat和getfriends会改变他的值
            ResultSet resultSet;
            resultSet = sql.executeQuery();
            List<User> users = new LinkedList<User>();
            User user ;
            while (resultSet.next()){
                String string = resultSet.getInt(1)+","+resultSet.getString(2)+","+resultSet.getString(3);
                user = User.stringToUser(string);
                user = userGetFriends(user);
                user = userGetGroupChat(user);
                users.add(user);
            }
            return users;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    public User userGetGroupChat(User user){
        try {
            sql = connection.prepareStatement("select * from groupchatusers");
            ResultSet resultSet;
            resultSet = sql.executeQuery();
            List<GroupChat> groupChatList = new LinkedList<GroupChat>();
            GroupChat groupChat ;
            while (resultSet.next()){
                if(user.getId() == resultSet.getInt(3)){
                    //通过群聊ID 从数据库chargroup表中实例化groupchat对象
                    int groupchatid = resultSet.getInt(2);
                    if((groupChat=getGroupChat(groupchatid))!=null){
                        groupChatList.add(groupChat);
                    }
                }
            }
            if (groupChatList.size()!=0) {
                user.setGroupChatList(groupChatList);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
    //这个方法得到的只有创建者User对象（含id和名字）群聊名和群聊ID 的groupchat对象
    public GroupChat getGroupChat(int groupchatid){
        try {
            sql = connection.prepareStatement("select * from groupchat");
            ResultSet resultSet;
            resultSet = sql.executeQuery();
            while (resultSet.next()){
                if(resultSet.getInt(3)==groupchatid){
                    User buliduser = getUserWithIdAndName(resultSet.getInt(2));
                    GroupChat groupChat = new GroupChat(buliduser,resultSet.getInt(3),resultSet.getString(4));
                    return groupChat;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //得到包含users的groupchat提供给服务端并让它转发消息
    public GroupChat getGroupChatHaveUsers(int id){
        GroupChat groupChat = getGroupChat(id);
        List<User> users = new LinkedList<User>();
        try {
            sql = connection.prepareStatement("select * from groupchatusers");
            ResultSet resultSet;
            resultSet = sql.executeQuery();
            while (resultSet.next()){
                if(resultSet.getInt(2)==id) {
                    User user = getUserWithIdAndName(resultSet.getInt(3));
                    users.add(user);
                }
            }
            groupChat.setUserList(users);

            return groupChat;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //不会得到空指针
        return null;

    }
    public boolean addGroupChat(int groupchatid,int senderid){
        try {
             if(existGroupChat(groupchatid)){
                 sql = connection.prepareStatement("insert into groupchatusers(groupchatid,userid) values (?,?)");
                 sql.setInt(1,groupchatid);
                 sql.setInt(2,senderid);
                 sql.executeUpdate();
                 return true;
             }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean existGroupChat(int groupchatid){
        try {
            sql = connection.prepareStatement("select * from groupchat");
            ResultSet resultSet;
            resultSet = sql.executeQuery();
            while (resultSet.next()){
                if(groupchatid == resultSet.getInt(3)){
                    return true;
                }
            }

        }
        catch (Exception e){

        }
        return false;
    }

    public User userGetFriends(User user){
        try {
            sql = connection.prepareStatement("select * from Friends");
            ResultSet resultSet;
            resultSet = sql.executeQuery();
            List<User> friends = new LinkedList<User>();
            User friend;
            //该用户的id
            int user_id= user.getId();
            //遍历数据库
            while (resultSet.next()){
                if(resultSet.getInt(2)==user_id){
                    //通过第三列的数得到对象（只含名字和ID)
                    friend = getUserWithIdAndName(Integer.parseInt(resultSet.getString(3)));
                    friends.add(friend);
                }
            }
            //如果为size为0也 添加 会得到friends集合不为空  但没有元素的对象json字符串反序列为对象会失败
            if(friends.size()!=0) {
                user.setFriends(friends);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
    public User getUserWithIdAndName(int id){
        User user = null;
        try {
            sql = connection.prepareStatement("select * from user_tbl");
            ResultSet resultSet;
            resultSet=sql.executeQuery();
            while (resultSet.next()){
                if(Integer.parseInt(resultSet.getString(1))==id){
                    user = new User(resultSet.getString(2),resultSet.getInt(1));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
    public boolean addfriend(int senderId,int acceptId){
        try {
            sql = connection.prepareStatement("insert into Friends(user_id,friend_id) values (?,?)");
            sql.setInt(1,senderId);
            sql.setInt(2,acceptId);
            sql.executeUpdate();
            sql.setInt(1,acceptId);
            sql.setInt(2,senderId);
            sql.executeUpdate();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean groupIdExist(int id){
        try {
            sql = connection.prepareStatement("select * from groupchat");
            ResultSet resultSet;
            resultSet = sql.executeQuery();
            while (resultSet.next()){
                if(id == resultSet.getInt(3)){
                    return true;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean outGroupChat(int builterid,String name,int groupid){
        try {
            sql = connection.prepareStatement("insert into groupchat(groupchat_builderid,groupchat_id,groupchat_name) values (?,?,?)");
            sql.setInt(1,builterid);
            sql.setInt(2,groupid);
            sql.setString(3,name);
            sql.executeUpdate();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }
    public static void main(String args[]){
        ConnectToDatabase connectToDatabase = new ConnectToDatabase();

        List<User> users = connectToDatabase.getUser();
        for(int i = 0;i<users.size();i++){
            System.out.println(JSON.toJSONString(users.get(i)));
        }
//        User user = new User(761702167,"田洲2号","t987654321");
//        User user1 = new User(761702168,"田洲2号","t987654321");
//        connectToDatabase.outUser(user);
//        connectToDatabase.outUser(user1);
//        users = connectToDatabase.getUser();
//        for (int i = 0 ;i<users.size();i++){
//            System.out.println(User.userToString(users.get(i)));
//        }
    }
}
