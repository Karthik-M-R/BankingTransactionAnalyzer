import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Transaction class
class Transaction {
    String transactionId;
    String type;
    double amount;

    Transaction(String transactionId, String type, double amount) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ID: " + transactionId + " | Type: " + type + " | Amount: ₹" + amount;
    }
}

// Comparator class
class AmountComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return Double.compare(t1.amount, t2.amount);
    }
}

// Main class with Swing UI
public class BankingTransactionAnalyzer extends JFrame {

    ArrayList<Transaction> transactions = new ArrayList<>();
    JTextArea outputArea;
    JTextField inputField;

    BankingTransactionAnalyzer() {
        setTitle("Banking Transaction Analyzer");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Enter Log (e.g. TXN1002|DEPOSIT|5000):");
        inputField = new JTextField(25);
        JButton addBtn = new JButton("Add Transaction");
        inputPanel.add(label);
        inputPanel.add(inputField);
        inputPanel.add(addBtn);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton displayAllBtn = new JButton("Display All");
        JButton filterBtn = new JButton("Amount > ₹10000");
        JButton sortBtn = new JButton("Sort by Amount");
        JButton clearBtn = new JButton("Clear");
        buttonPanel.add(displayAllBtn);
        buttonPanel.add(filterBtn);
        buttonPanel.add(sortBtn);
        buttonPanel.add(clearBtn);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add some sample data
        addSampleData();

        // Button Actions

        // Add transaction using substring()
        addBtn.addActionListener(e -> {
            String log = inputField.getText().trim();
            if (log.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a transaction log.");
                return;
            }
            try {
                // Parsing using substring() and indexOf()
                int firstPipe = log.indexOf("|");
                int secondPipe = log.indexOf("|", firstPipe + 1);

                String txnId = log.substring(0, firstPipe);
                String type = log.substring(firstPipe + 1, secondPipe);
                double amount = Double.parseDouble(log.substring(secondPipe + 1));

                transactions.add(new Transaction(txnId, type, amount));
                outputArea.setText("✅ Transaction added: " + txnId);
                inputField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid format! Use: TXNID|TYPE|AMOUNT");
            }
        });

        // Display all
        displayAllBtn.addActionListener(e -> {
            if (transactions.isEmpty()) {
                outputArea.setText("No transactions found.");
                return;
            }
            StringBuilder sb = new StringBuilder("=== All Transactions ===\n\n");
            for (Transaction t : transactions) {
                sb.append(t.toString()).append("\n");
            }
            outputArea.setText(sb.toString());
        });

        // Filter > 10000
        filterBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("=== Transactions > ₹10,000 ===\n\n");
            boolean found = false;
            for (Transaction t : transactions) {
                if (t.amount > 10000) {
                    sb.append(t.toString()).append("\n");
                    found = true;
                }
            }
            if (!found) sb.append("No transactions above ₹10,000.");
            outputArea.setText(sb.toString());
        });

        // Sort by amount
        sortBtn.addActionListener(e -> {
            Collections.sort(transactions, new AmountComparator());
            StringBuilder sb = new StringBuilder("=== Sorted by Amount ===\n\n");
            for (Transaction t : transactions) {
                sb.append(t.toString()).append("\n");
            }
            outputArea.setText(sb.toString());
        });

        // Clear output
        clearBtn.addActionListener(e -> outputArea.setText(""));

        setVisible(true);
    }

    void addSampleData() {
        // Pre-loaded sample transactions
        String[] logs = {
                "TXN1001|DEPOSIT|15000",
                "TXN1002|WITHDRAWAL|3000",
                "TXN1003|DEPOSIT|8500",
                "TXN1004|WITHDRAWAL|25000",
                "TXN1005|DEPOSIT|500"
        };
        for (String log : logs) {
            int first = log.indexOf("|");
            int second = log.indexOf("|", first + 1);
            String id = log.substring(0, first);
            String type = log.substring(first + 1, second);
            double amount = Double.parseDouble(log.substring(second + 1));
            transactions.add(new Transaction(id, type, amount));
        }
    }

    public static void main(String[] args) {
        new BankingTransactionAnalyzer();
    }
}