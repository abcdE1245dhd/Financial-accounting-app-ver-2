
package proledger;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    private JLabel income = new JLabel();
    private JLabel expenses = new JLabel();
    private JLabel balance = new JLabel();

    public Dashboard() {

        setTitle("ProLedger Accounting System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel header = new JPanel();
        header.setBackground(new Color(20, 35, 60));

        JLabel title = new JLabel(
            "PROLEDGER - ACCOUNTING SYSTEM"
        );

        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        header.add(title);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Dashboard", dashboardPanel());
        tabs.addTab("Transactions", new TransactionManager());
        tabs.addTab("Financial Statements", statementPanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        refresh();
    }

    private JPanel dashboardPanel() {

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        panel.add(makeCard("TOTAL INCOME", income));
        panel.add(makeCard("TOTAL EXPENSES", expenses));
        panel.add(makeCard("NET BALANCE", balance));

        return panel;
    }

    private JPanel makeCard(String title, JLabel value) {

        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBorder(
            BorderFactory.createTitledBorder(title)
        );

        value.setFont(new Font("Arial", Font.BOLD, 22));

        card.add(new JLabel(title));
        card.add(value);

        return card;
    }

    private JPanel statementPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JTextArea output = new JTextArea();
        output.setEditable(false);

        JButton generate = new JButton("Generate Statement");

        generate.addActionListener(e -> {
            try {
                output.setText(
                    FinancialStatements.generateIncomeStatement()
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this, ex.getMessage()
                );
            }
        });

        panel.add(generate, BorderLayout.NORTH);
        panel.add(new JScrollPane(output));

        return panel;
    }

    public void refresh() {

        try {
            double in = Database.getTotal("INCOME");
            double out = Database.getTotal("EXPENSE");

            income.setText("PHP " + in);
            expenses.setText("PHP " + out);
            balance.setText("PHP " + (in - out));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
