package client;

import common.Constants;
import common.Message;
import common.MessageDispatch;
import common.Utils;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author: flyboy
 * @Date: 20/03/2021 15:09
 * @Version 1.0
 * @Email: 1308794149@qq.com
 */
public class Client {
    private String userName;
    MessageDispatch messageDispatch;
    public Client() throws IOException {
        System.out.println("The client is connecting...");
        Socket socket = new Socket(Constants.serverAddress,Constants.serverPort);
        System.out.println("The client is connected with server successfully");
        messageDispatch = new MessageDispatch(socket);
    }

    public void start(){
        initUserInfo();
        ReadThread readThread = new ReadThread(messageDispatch);
        WriteThread writeThread = new WriteThread(messageDispatch,userName);
        Thread readThread1 = new Thread(readThread,"client read thread");
        Thread writeThread1 = new Thread(writeThread,"client write thread");
        readThread1.start();
        writeThread1.start();
    }

    private void initUserInfo(){
        String tempName = null;
        while (true){
            // server finds the socket connection automatic, so serve will send message first
            Message validMessage = null;
            try {
                validMessage = messageDispatch.receive();
            }catch (Exception e){
                messageDispatch.close();
                return;
            }
            String content = validMessage.getContent();
            if (content.equals(Constants.userRegisterValid)){
                userName = tempName;
                System.out.println("register successfully");
                break;
            }
            System.out.println(content);
            Scanner input = new Scanner(System.in);
            tempName = input.nextLine();
            messageDispatch.send(new Message(Constants.nameForRegister,Constants.serverName,tempName.trim()));
        }
    }
}

class ReadThread implements Runnable{
    private MessageDispatch messageDispatch;
    public ReadThread(MessageDispatch messageDispatch){
        this.messageDispatch = messageDispatch;
    }
    @Override
    public void run() {
        while (true){
            try{
                Message message = messageDispatch.receive();
                // judge logout
                if (message.getSender().equals(Constants.serverName)
                        && message.getContent().trim().equals(Constants.serverSystemCommandLogOut)){
                    messageDispatch.close();
                    System.out.println("logout successfully, good bye");
                    System.exit(0);
                }
                System.out.println("from "+message.getSender()+": "+message.getContent());
            } catch (Exception e){
                System.out.println("client haves something wrong");
                messageDispatch.close();
                System.exit(-2);
            }
        }
    }
}

class WriteThread implements Runnable{
    private MessageDispatch messageDispatch;
    private String userName;
    public WriteThread(MessageDispatch messageDispatch,String userName){
        this.messageDispatch = messageDispatch;
        this.userName = userName;
    }
    @Override
    public void run() {
        Scanner input = new Scanner(System.in);
        while (true){
            String inputContent = input.nextLine().trim();
            String receiver = null,content = null;
            boolean validMessage = false;
            try{
                if (inputContent.startsWith(Constants.chatStartSymbol)){
                    // should valid message receiver
                    receiver = inputContent.substring(1, inputContent.indexOf(" ")).trim();
                    if (!Utils.isValidToUserName(receiver).equals(Constants.validReceiverName)){
                        receiver = null;
                    }
                    content = inputContent.substring(inputContent.indexOf(" ") + 1);
                    validMessage = true;
                }else {
                    System.out.println("a chat should start with @someone, if you want to talk to him/her");
                }
            }catch (Exception e){
                System.out.println("input message is wrong");
                continue;
            }
            if (validMessage){
                Message message = new Message(userName,receiver,content);
                messageDispatch.send(message);
            }
        }
    }
}

