
package proledger;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Database.initialize();

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {}

            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }
}
