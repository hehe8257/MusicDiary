package demo;

import javax.swing.SwingUtilities;
import controller.LoginController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginController().start();
            }
        });
    }
}
