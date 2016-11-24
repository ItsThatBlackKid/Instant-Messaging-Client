import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 215358 on 23/11/2016.
 */
public class MessagePanel extends JPanel {

    public MessagePanel() {
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
