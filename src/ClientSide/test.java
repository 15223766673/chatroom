package ClientSide;
import Server.User;
import com.alibaba.fastjson.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.*;

public class test {
    public int id;
    public String passwords;
    public test(int id , String passwords){
        this.id=id;
        this.passwords = passwords;
    }
    public test(){

    }
    public void setId(int id){
        this.id = id;
    }
    public void setPasswords(String passwords){
        this.passwords = passwords;
    }
    public int getId(){
        return id;
    }
    public String getPasswords(){
        return passwords;
    }
//    public void toString(){
//        System.out.println("ID:"+this.id+"密码:"+this.passwords);
//    }
    public void getString(){
        System.out.println("ID:"+this.id+"密码："+this.passwords);
    }

    public static void main(String args[]){
        test test1 = new test();
        test1.setId(761702168);
        test1.setPasswords("asdfg");
        test test = new test(76170216,"asdfxxxx");
        //1
        List<test> list = new ArrayList<test>();
        list.add(test);
        list.add(test1);
        System.out.println(JSON.toJSON(list));

        //2
        JSONObject jsonObject = (JSONObject) JSON.toJSON(test1);
        System.out.println(jsonObject.toString());
        //3
        String string = JSON.toJSONString(test1);
        System.out.println(string);
        //4
        Map<String,String> user = new HashMap<String, String>();
        user.put("1","1号选手");
        user.put("2","2号选手");
        System.out.println(JSON.toJSON(user));
    }
}

