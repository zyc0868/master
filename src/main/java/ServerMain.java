import server.Server;

import java.io.IOException;

/**
 * @author: flyboy
 * @Date: 20/03/2021 14:46
 * @Version 1.0
 * @Email: 1308794149@qq.com
 */
public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
