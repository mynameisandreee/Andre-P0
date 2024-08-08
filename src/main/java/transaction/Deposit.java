package transaction;

import Account.Account;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Deposit {

    public static void deposit(Context ctx, Connection conn) {
        Account account = ctx.bodyAsClass(Account.class);
        String getBalanceSql = "SELECT balance FROM account WHERE account_id = ?";
        String updateBalanceSql = "UPDATE account SET balance = ? WHERE account_id = ?";
        String insertTransactionSql = "INSERT INTO transaction (account_id, title, description, date_of_transaction) VALUES (?, ?, ?, ?)";

        try (PreparedStatement getBalanceStmt = conn.prepareStatement(getBalanceSql);
             PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceSql);
             PreparedStatement insertTransactionStmt = conn.prepareStatement(insertTransactionSql)) {

            // Retrieve the current balance
            getBalanceStmt.setInt(1, account.getAccount_id());
            //execute the query
            ResultSet rs = getBalanceStmt.executeQuery();

            if (rs.next()) {
                BigDecimal currentBalance = rs.getBigDecimal("balance");
                BigDecimal newBalance = currentBalance.add(account.getBalance());

                // Update the balance
                updateBalanceStmt.setBigDecimal(1, newBalance);
                updateBalanceStmt.setInt(2, account.getAccount_id());

                int affectedRows = updateBalanceStmt.executeUpdate();
                if (affectedRows > 0) {
                    // Insert transaction record
                    String transactionDescription = "You have deposited $" + account.getBalance() + " to your account";
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    insertTransactionStmt.setInt(1, account.getAccount_id());
                    insertTransactionStmt.setString(2, "Deposit");
                    insertTransactionStmt.setString(3, transactionDescription);
                    insertTransactionStmt.setTimestamp(4, timestamp);

                    int rowsInserted = insertTransactionStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        ctx.status(200).result("Deposit successful.");
                    } else {
                        ctx.status(500).result("Failed to record transaction.");
                    }
                } else {
                    ctx.status(404).result("Account not found.");
                }
            } else {
                ctx.status(404).result("Account not found.");
            }

        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        }
    }
}
