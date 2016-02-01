import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by sheku on 9/01/2016.
 */
public class Server extends JFrame {
    ServerSocket serverSide;
    JTextArea chatArea = new JTextArea();
    Messages messages = new Messages();
    Clients clients = new Clients();


    final String currentID = "0.1";
    int numOfClients = 1;

    ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        super("Server Side");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(530, 400);
        setLayout(new BorderLayout());
        setVisible(true);


        add(chatArea);
        add(messages, BorderLayout.SOUTH);
        add(clients, BorderLayout.NORTH);

        runServer();
    }

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
                clientThreads.add(new ClientThread(client));
                // ClientThread clientThread = new ClientThread(client);
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

    void toAllClients(String message) {
        for(ClientThread c: clientThreads)
        clientThreads.in
            c.sendAll(message);
    }

    private void showMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatArea.append(message);
            }
        });
    }

    class Clients extends JPanel {
        JLabel clientIMG[] = new JLabel[5];

        void showImages() {
            for (int i = 0; i < numOfClients; i++) {
                clientIMG[i] = new JLabel("Client" + i + " \n <html> <font color=\"red\">");
                clientIMG[i].setPreferredSize(new Dimension(50, 30));
                add(clientIMG[i]);
            }
        }

        public Clients() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            setPreferredSize(new Dimension(this.getWidth(), 30));
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.WHITE);

            showImages();
        }
    }

    class Messages extends JPanel {

        final int h = 30;

        public Messages() {
            setLayout(new FlowLayout(0, 0, 0));
            setPreferredSize(new Dimension(this.getWidth(), h));
            setBackground(Color.lightGray);
            setForeground(Color.white);

            JTextField textField = new JTextField();
            textField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    broadcast(e.getActionCommand());
                    textField.setText("");
                }
            });
            textField.setPreferredSize(new Dimension(400, h));

            JButton sendButton = new JButton("Send");
            sendButton.setPreferredSize(new Dimension(100, h));
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    broadcast(textField.getText());
                    textField.setText("");
                }
            });

            add(textField);
            add(sendButton);
        }

    }

    class ClientThread extends Thread {
        ObjectOutputStream output;
        ObjectInputStream input;


        private Socket client = null;

        public ClientThread(Socket socket) throws IOException{
            this.client = socket;
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
                } catch (ClassNotFoundException c) {
                    showMessage("CANT READ OBJECT");
                }
            } while (!message.equalsIgnoreCase("CLIENT: END"));
        }

        void showClient() {

            for (int i = 0; i < numOfClients; i++) {
                clients.clientIMG[i].setText("Client " + i);
            }
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
}