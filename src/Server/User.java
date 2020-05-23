package Server;
import java.*;
import java.io.File;
import java.util.*;
import java.io.Serializable;
import java.io.*;

public class User implements Serializable{
    private String name;
    private int id;
    private String passWords;
    public List<User> friends ;
    public List<GroupChat> groupChatList ;
    private static final long serialVersionUID = 1l;
    public User(){

    }
    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }
    public void setFriends(){

    }
    public void setGroupChatList(){

    }
    public User(int id,String name,String passWords){
        this.id = id;
        this.name = name;
        this.passWords = passWords;
        this.friends=new LinkedList<User>();
    }
    public List<User> getFriends(){
        return this.friends;
    }
    public void setFriends(List<User> friends){
        this.friends = friends;
    }
    public List<GroupChat> getGroupChatList(){
            return this.groupChatList;
    }
    public void setGroupChatList(List<GroupChat> groupChatList){
        this.groupChatList = groupChatList;
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
    public String getPassWords(){
        return passWords;
    }
    public void setPassWords(String passWords){
        this.passWords = passWords;
    }

    public void setId(int id) {
        this.id = id;
    }
    public static List<User> inputUsers(){
        //反序列化
        List<User> users = new LinkedList<User>();
        try {



            //File students_file = new File("src//Server//user.obj");

            //ObjectInputStream ios = new ObjectInputStream(new FileInputStream(students_file));
            FileInputStream fos = new FileInputStream("src//Server//User.txt");

            //ObjectInputStream oos = new ObjectInputStream(fos);
            ObjectInputStream oos = null;
            User user = null;
            while (fos.available()>0){
                oos = new ObjectInputStream(fos);
                user = (User)oos.readObject();
                users.add(user);


            }
            if(oos!=null) {
                oos.close();
            }
            return users;



        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("已读完txt");
        }
        return users;

    }
    public static void outputUser(User user){
      //序列化
        try {
            File userfile = new File("src//Server//User.txt");
            FileOutputStream out = new FileOutputStream(userfile,true);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(user);
            oout.close();
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public static User stringToUser(String string){
            String[] strings = string.split(",");
            return new User(Integer.parseInt(strings[0]),strings[1],strings[2]);
    }
    public static String userToString(User user){
        return user.id+","+user.name+","+user.passWords;
    }
    public static void main(String args[]){
       User user = new User(761702168,"田洲","123456");
       User friend = new User(123456,"好友","123456");
       user.friends.add(friend);
    }
    public boolean isFriend(int id){
        List<User> friends = this.getFriends();
        if(friends!=null) {
            for (int i = 0; i < friends.size(); i++) {
                if (id == friends.get(i).getId()) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean havaGroupChat(int id){
        List<GroupChat> groupChatList = this.getGroupChatList();
        if(groupChatList!=null) {
            for (int i = 0; i < groupChatList.size(); i++) {
                if (id == groupChatList.get(i).getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
