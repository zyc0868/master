package server;

import common.Constants;
import common.Message;
import common.MessageDispatch;
import common.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: flyboy
 * @Date: 20/03/2021 15:09
 * @Version 1.0
 * @Email: 1308794149@qq.com
 */
public class Server {
    // register center
    private Map<String, MessageDispatch> messageDispatchByName = new ConcurrentHashMap<>();
    // thread pool support multi client connection
    private ExecutorService executorService = Executors.newCachedThreadPool();
    public Server(){
    }
    public void start() throws IOException {
        System.out.println("server starts");
        ServerSocket serverSocket = new ServerSocket(Constants.serverPort);
        while (true){
            Socket socket = serverSocket.accept();
            // find socket connection
            executorService.submit(new HandlerCenter(messageDispatchByName,socket));
        }
    }
}

class HandlerCenter implements Runnable{
    private Socket socket;
    private Map<String, MessageDispatch> messageDispatchByName;
    private String userName = null;
    public HandlerCenter(Map<String, MessageDispatch> messageDispatchByName,Socket socket){
        this.messageDispatchByName = messageDispatchByName;
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("process "+socket.getRemoteSocketAddress()+" connection request");
        MessageDispatch messageDispatch = null;
        try{
            messageDispatch = new MessageDispatch(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        registerUser(messageDispatch);
        handlerMessage(messageDispatch);
    }

    private void handlerMessage(MessageDispatch messageDispatch){
        while (true){
            try{
                Message message = messageDispatch.receive();
                String receiverName = message.getReceiver();
                if (receiverName.equals(Constants.serverName)){
                    handlerSystemMessage(message);
                }else {
                    handlerNormalMessage(message,messageDispatch);
                }
            } catch (Exception e){
                messageDispatchByName.remove(userName).close();
                return;
            }
        }
    }

    private void handlerSystemMessage(Message message){
        MessageDispatch sender = messageDispatchByName.get(message.getSender());
        String content = message.getContent();
        if (content.equals(Constants.serverSystemCommandLogOut)){
            sender.send(new Message(Constants.serverName,message.getSender(),Constants.serverSystemCommandLogOut));
            sender.close();
            messageDispatchByName.remove(userName).close();
            System.out.println("user "+message.getSender()+" left the chat room");
        } else if (content.equals(Constants.serverSystemCommandUserList)){
            sender.send(new Message(Constants.serverName,message.getSender(),"user list:" + userNameSetString()));
        } else if (content.startsWith(Constants.serverSystemCommandOperation)){
            sender.send(new Message(Constants.serverName,message.getSender(),Constants.operationGuide));
        }
    }

    private void handlerNormalMessage(Message message, MessageDispatch serverMessageDispatch){
        MessageDispatch messageDispatch = messageDispatchByName.get(message.getReceiver());
        if (messageDispatch == null){
            serverMessageDispatch.send(new Message(Constants.serverName,message.getSender(),
                    "user: "+message.getReceiver()+" not exists or log out"));
        } else {
            messageDispatch.send(message);
        }
    }

    private String userNameSetString(){
        String [] nameSet = messageDispatchByName.keySet().toArray(new String[0]);
        StringBuilder nameSetString = new StringBuilder();
        for (String s: nameSet){
            nameSetString.append(s).append(" ");
        }
        return nameSetString.toString();
    }

    private void registerUser(MessageDispatch messageDispatch){
        String error = null;
        String nameSetString = userNameSetString();
        while (true){
            // push message actively when first communication
            String content = (error==null ? "" : "register name error:"+error) + "  existing names: [ "+nameSetString+ " ]"+
                    "  please enter your name: ";
            messageDispatch.send(new Message(Constants.serverName,Constants.nameForRegister,content));
            Message message = null;
            try{
                message = messageDispatch.receive();
            } catch (Exception e){
                messageDispatch.close();
                return;
            }
            // valid name
            String registerName = message.getContent().trim();
            error = Utils.isValidToUserName(registerName);
            if (error.equals(Constants.validReceiverName) && !messageDispatchByName.containsKey(registerName)){
                this.userName = registerName;
                // register
                messageDispatchByName.put(userName,messageDispatch);
                // valid passed message
                messageDispatch.send(new Message(Constants.serverName,userName,Constants.userRegisterValid));
                messageDispatch.send(new Message(Constants.serverName,userName,Constants.operationGuide));
                return;
            }
        }
    }
}