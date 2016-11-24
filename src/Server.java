import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by 215358 on 23/11/2016.
 */
public class Server {
    ServerSocket serverSide;
    ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();

    public void runServer() {
        try {
            serverSide = new ServerSocket(8888);
        } catch (IOException e) {
            showMessage("Couldn't listen on port: " + 8888);
            e.printStackTrace();
        }

        try {
            while (true) {
                Socket client = serverSide.accept();
                ClientThread c = new ClientThread(client);
                clientThreads.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    void broadcast(String message) {
        for(ClientThread c: clientThreads)
            c.sendMessage(message);
    }

    void showMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {chatArea.append(message);
            }
        });
    }
}
