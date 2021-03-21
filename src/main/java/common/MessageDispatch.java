package common;

import java.io.*;
import java.net.Socket;

/**
 * @author: flyboy
 * @Date: 20/03/2021 15:11
 * @Version 1.0
 * @Email: 1308794149@qq.com
 */
public class MessageDispatch {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    public MessageDispatch(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void send(Message message){
        // transform before write
        writer.println(message.generateMessageString());
        writer.flush();
    }

    public Message receive() throws IOException {
        String content = null;
        // loop waiting to receive message
        while (true){
            content = reader.readLine();
            // now system is only support single line input
            if (content != null && content.length() > 0){
                break;
            }
        }
        // transform
        return Message.generateMessage(content);
    }

    public void close(){
        try {
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
