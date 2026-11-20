
package proledger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

public class TransactionManager extends JPanel {

    private JTextField date = new JTextField(
        LocalDate.now().toString()
    );

    private JTextField description = new JTextField();
    private JTextField amount = new JTextField();

    private JComboBox<String> type = new JComboBox<>(
        new String[]{"EXPENSE", "INCOME"}
    );

    private JComboBox<String> category = new JComboBox<>(
        new String[]{
            "Food Expenses",
            "Transportation Expenses",
            "Utilities Expenses",
            "School Expenses",
            "Office Supplies",
            "Salary Expenses",
            "Rent Expenses",
            "Other Expenses",
            "Uncategorized Receipts"
        }
    );

    private String receiptPath = "";

    public TransactionManager() {

        setLayout(new GridLayout(7, 2, 10, 10));

        add(new JLabel("Date:"));
        add(date);

        add(new JLabel("Type:"));
        add(type);

        add(new JLabel("Category:"));
        add(category);

        add(new JLabel("Description:"));
        add(description);

        add(new JLabel("Amount:"));
        add(amount);

        JButton scan = new JButton("Scan Receipt");
        JButton save = new JButton("Save Transaction");

        add(scan);
        add(save);

        scan.addActionListener(e -> scanReceipt());
        save.addActionListener(e -> saveTransaction());
    }

    private void scanReceipt() {

        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(this)
            != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        scan.setEnabled(false);
    }

    private void saveTransaction() {

        try {
            double value = Double.parseDouble(
                amount.getText().trim()
            );

            if (!Double.isFinite(value) || value <= 0) {
                throw new IllegalArgumentException(
                    "Enter a valid positive amount."
                );
            }

            Database.addTransaction(
                date.getText().trim(),
                type.getSelectedItem().toString(),
                category.getSelectedItem().toString(),
                description.getText().trim(),
                value,
                receiptPath
            );

            JOptionPane.showMessageDialog(
                this, "Transaction saved successfully!"
            );

            description.setText("");
            amount.setText("");
            receiptPath = "";

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, "Error: " + e.getMessage()
            );
        }
    }
}
