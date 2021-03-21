package common;

/**
 * @author: flyboy
 * @Date: 20/03/2021 15:17
 * @Version 1.0
 * @Email: 1308794149@qq.com
 */
public class Message {
    private String sender,receiver;
    private String content;

    public Message() {
    }

    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }


    public static Message generateMessage(String message){
        Message realMessage = new Message();
        // message is split by Character.UNASSIGNED
        String [] messageSplit  = message.split(Constants.message_sep_string);
        if (messageSplit.length != 3){
            return null;
        }
        String senderName = messageSplit[0].trim();
        String receiverName = messageSplit[1].trim();
        realMessage.content = messageSplit[2].trim();
        realMessage.sender = senderName;
        realMessage.receiver = receiverName;
        return  realMessage;
    }

    public String generateMessageString(){
        StringBuilder message = new StringBuilder();
        message.append(sender).append(Constants.message_sep_string);
        message.append(receiver).append(Constants.message_sep_string);
        message.append(content);
        return message.toString();
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
