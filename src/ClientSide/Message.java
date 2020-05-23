package ClientSide;

import com.alibaba.fastjson.JSON;

public class Message {
    public String type;
    public String string;
    public int senderId;
    public int accepterId;
    public Message(){

    }

    public String getString() {
        return string;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getAccepterId() {
        return accepterId;
    }

    public void setAccepterId(int accepterId) {
        this.accepterId = accepterId;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Message(String type, String string){
        this.type=type;
        this.string = string;
    }
    public Message(String type,String string,int senderId,int accepterId){
        this.string =string;
        this.type = type;
        this.senderId = senderId;
        this.accepterId = accepterId;
    }
    public Message(String type,String string,int senderId){
        this.string = string;
        this.type = type;
        this.senderId = senderId;
    }
    public Message(String type,int accepterId,String string){
        this.type = type;
        this.accepterId = accepterId;
        this.string = string;
    }
    public static void main(String args[]){
        Message message = new Message("添加好友","761702169,761702168",761702169,761702168);
        String jsonstring = JSON.toJSONString(message);
        System.out.println(jsonstring);
        Message message1 = JSON.parseObject(jsonstring,Message.class);
        System.out.println(message1.string);
    }

}
