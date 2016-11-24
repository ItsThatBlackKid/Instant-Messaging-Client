import javax.swing.*;
import java.awt.*;

/**
 * Created by 215358 on 23/11/2016.
 */
public class ClientPanel extends JPanel {

    JLabel clientIMG[] = new JLabel[5];

    void showImages() {
        for (int i = 0; i < numOfClients; i++) {
            clientIMG[i] = new JLabel("Client" + i + " \n <html> <font color=\"red\">");
            clientIMG[i].setPreferredSize(new Dimension(50, 30));
            add(clientIMG[i]);
        }
    }

    public ClientPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setPreferredSize(new Dimension(, 30));
        setBackground(Color.LIGHT_GRAY);
        setForeground(Color.WHITE);

        showImages();
    }
}
