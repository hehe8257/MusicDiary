

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    public interface Listener {
        void onAdd();
        void onComment();
        void onDisplay();
    }

    public LoginFrame(final Listener listener) {
        setTitle("Simple Song App");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color cimaBlue = new Color(80, 150, 200);
        Color cimaBlueDark = new Color(50, 100, 150);

        JLabel title = new JLabel("Welcome！", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(cimaBlueDark);
        title.setPreferredSize(new Dimension(500, 70));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        buttonPanel.setBackground(cimaBlue);
        buttonPanel.setBorder(new EmptyBorder(40, 100, 40, 100));

        JButton addButton = new JButton("Add");
        JButton commentButton = new JButton("Comment");
        JButton displayButton = new JButton("Display Songs");

        Font f = new Font("SansSerif", Font.PLAIN, 18);
        for (JButton b : new JButton[]{addButton, commentButton, displayButton}) {
            b.setFont(f);
            b.setBackground(Color.WHITE);
            buttonPanel.add(b);
        }

        add(buttonPanel, BorderLayout.CENTER);
        getContentPane().setBackground(cimaBlue);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { listener.onAdd(); }
        });
        commentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { listener.onComment(); }
        });
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { listener.onDisplay(); }
        });
    }
}
