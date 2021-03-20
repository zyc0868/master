import client.Client;

import java.io.IOException;

/**
 * @author: flyboy
 * @Date: 20/03/2021 23:04
 * @Version 1.0
 * @Email: 1308794149@qq.com
 */
public class ClientMain1 {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }
}
