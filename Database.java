
package proledger;

import java.sql.*;

public class Database {

    private static final String URL =
        "jdbc:sqlite:database/proledger.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        try {
            java.nio.file.Files.createDirectories(
                java.nio.file.Paths.get("database")
            );

            try (Connection con = connect();
                 Statement st = con.createStatement()) {

                st.execute("""
                    CREATE TABLE IF NOT EXISTS transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        date TEXT NOT NULL,
                        type TEXT NOT NULL,
                        category TEXT NOT NULL,
                        description TEXT,
                        amount REAL NOT NULL,
                        receipt_path TEXT
                    )
                """);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTransaction(
        String date,
        String type,
        String category,
        String description,
        double amount,
        String receipt
    ) throws SQLException {

        String sql = """
            INSERT INTO transactions
            (date,type,category,description,amount,receipt_path)
            VALUES (?,?,?,?,?,?)
        """;

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, date);
            ps.setString(2, type);
            ps.setString(3, category);
            ps.setString(4, description);
            ps.setDouble(5, amount);
            ps.setString(6, receipt);

            ps.executeUpdate();
        }
    }

    public static double getTotal(String type)
        throws SQLException {

        String sql = """
            SELECT COALESCE(SUM(amount),0)
            FROM transactions WHERE type=?
        """;

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, type);

            ResultSet rs = ps.executeQuery();

            return rs.getDouble(1);
        }
    }
}
