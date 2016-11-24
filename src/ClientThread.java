import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by 215358 on 23/11/2016.
 */
public class ClientThread extends Thread implements Server {

        ObjectOutputStream output;
        ObjectInputStream input;


        private Socket client = null;

        public ClientThread(Socket socket) throws IOException {
            this.client = socket;
            showMessage("Now Connecting");
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
            broadcast("a new user has connected " );
            start();
        }

    public void run() {
        try {
            connected();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }

    private void connected() throws IOException {
        String message = "";

        do {
            try {
                message = (String) input.readObject();
                toAllClients(message);
                showMessage(message + "\n");
                broadcast(message);
            } catch (ClassNotFoundException c) {
                showMessage("CANT READ OBJECT");
            }
        } while (!message.equalsIgnoreCase("CLIENT: END"));
    }

    void sendAll(String message) {
        try {
            output.writeObject(message);
            output.flush();
        }catch (IOException e) {
            showMessage("CAN'T SEND\n " );
        }
    }

    void sendMessage(String message) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    output.writeObject("SERVER: " + message);
                    output.flush();
                    showMessage("SERVER: " + message + "\n");
                } catch (IOException i) {
                    showMessage("UNABLE TO SEND... PLEASE TRY AGAIN \n");
                }
            }});

    }
}
