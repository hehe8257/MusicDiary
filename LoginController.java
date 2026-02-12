

import javax.swing.*;
import model.User;
import view.LoginFrame;

public class LoginController implements LoginFrame.Listener {
    private User model;
    private LoginFrame view;

    public void start() {
        model = new User();
        view = new LoginFrame(this);
        view.setVisible(true);
    }

    public void onAdd() {
        JTextField t1 = new JTextField();
        JTextField t2 = new JTextField();
        JTextField t3 = new JTextField("2025-10-29");

        Object[] msg = {"Title:", t1, "Artist:", t2, "Date (yyyy-MM-dd):", t3};
        int opt = JOptionPane.showConfirmDialog(null, msg, "Add Song", JOptionPane.OK_CANCEL_OPTION);
        if (opt != JOptionPane.OK_OPTION) return;

        if (!model.addSong(t1.getText(), t2.getText(), t3.getText()))
            JOptionPane.showMessageDialog(null, "Wrong input. Please check the formats.");
        else
            JOptionPane.showMessageDialog(null, "Song added successfully!");
    }

    public void onComment() {
        JTextField title = new JTextField();
        JTextField score = new JTextField();
        JTextField comment = new JTextField();
        Object[] msg = {"Title:", title, "Score (0–10):", score, "Comment:", comment};

        int opt = JOptionPane.showConfirmDialog(null, msg, "Comment Song", JOptionPane.OK_CANCEL_OPTION);
        if (opt != JOptionPane.OK_OPTION) return;

        Integer s = null;
        try { s = Integer.valueOf(score.getText()); } catch (Exception ignored) {}
        String err = model.commentSong(title.getText(), s, comment.getText());
        if (err != null) JOptionPane.showMessageDialog(null, err);
        else JOptionPane.showMessageDialog(null, "Comment saved!");
    }

    public void onDisplay() {
        String date = JOptionPane.showInputDialog(null, "Please enter the date today(yyyy-MM-dd):");
        if (date == null || !model.validDate(date)) {
            JOptionPane.showMessageDialog(null, "Wrong date format.");
            return;
        }
        Object[][] data = model.buildDisplayTable(date);
        String[] cols = {"#", "Title", "Artist", "Date", "Score", "Days"};

        JTable table = new JTable(data, cols);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 1) {
                    String[] res = model.getCommentByRow(data, row);
                    JOptionPane.showMessageDialog(null, "Comment for \"" + res[0] + "\":\n" + res[1] + "\nScore: " + res[2]);
                }
            }
        });

        JFrame f = new JFrame("Display Songs");
        f.setSize(600, 400);
        f.add(new JScrollPane(table));
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
