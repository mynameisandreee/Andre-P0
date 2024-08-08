package transaction;

import io.javalin.http.Context;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class TransferMoney {

    public void transferMoney(Context ctx, Connection conn) {
        TransferRequest transferRequest = ctx.bodyAsClass(TransferRequest.class);

        int sourceAccountId = transferRequest.getSourceAccountId();
        int destinationAccountId = transferRequest.getDestinationAccountId();
        BigDecimal amount = transferRequest.getAmount();

        String deductSql = "UPDATE account SET balance = balance - ? WHERE account_id = ? AND balance >= ?";
        String addSql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        String transactionSql = "INSERT INTO transaction (account_id, title, description) VALUES (?, ?, ?)";

        try (PreparedStatement deductStmt = conn.prepareStatement(deductSql);
             PreparedStatement addStmt = conn.prepareStatement(addSql);
             PreparedStatement transactionStmt = conn.prepareStatement(transactionSql)) {

            conn.setAutoCommit(false); // Start transaction

            // Deduct amount from source account
            deductStmt.setBigDecimal(1, amount);
            deductStmt.setInt(2, sourceAccountId);
            deductStmt.setBigDecimal(3, amount); // Ensure sufficient balance
            int rowsUpdated = deductStmt.executeUpdate();

            if (rowsUpdated == 0) {
                conn.rollback(); // Rollback transaction if update fails
                ctx.status(400); // HTTP 400 Bad Request
                ctx.result("Insufficient balance or source account not found");
                return;
            }

            // Add amount to destination account
            addStmt.setBigDecimal(1, amount);
            addStmt.setInt(2, destinationAccountId);
            rowsUpdated = addStmt.executeUpdate();

            if (rowsUpdated == 0) {
                conn.rollback(); // Rollback transaction if update fails
                ctx.status(400); // HTTP 400 Bad Request
                ctx.result("Destination account not found");
                return;
            }

            // Insert transaction record for source account
            transactionStmt.setInt(1, sourceAccountId);
            transactionStmt.setString(2, "Transfer");
            transactionStmt.setString(3, "Sent " + amount + " to account " + destinationAccountId);
            transactionStmt.executeUpdate();

            // Insert transaction record for destination account
            transactionStmt.setInt(1, destinationAccountId);
            transactionStmt.setString(2, "Transfer");
            transactionStmt.setString(3, "Received " + amount + " from account " + sourceAccountId);
            transactionStmt.executeUpdate();

            conn.commit(); // Commit transaction if all operations are successful
            ctx.status(200); // HTTP 200 OK
            ctx.result("Money transferred successfully");

        } catch (SQLException e) {
            try {
                conn.rollback(); // Rollback transaction on SQL exception
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace(); // Handle rollback exception
            }
            e.printStackTrace();
            ctx.status(500); // HTTP 500 Internal Server Error
            ctx.result("Failed to transfer money: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace(); // Handle setAutoCommit exception
            }
        }
    }
}
