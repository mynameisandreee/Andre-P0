package transaction;

import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class GetTransaction {

    public void getTransactions(Context ctx, Connection conn) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));

        String sql = "SELECT transaction_id, account_id, title, description, date_of_transaction FROM transaction WHERE account_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setAccountId(rs.getInt("account_id"));
                transaction.setTitle(rs.getString("title"));
                transaction.setDescription(rs.getString("description"));
                transaction.setDateOfTransaction(rs.getTimestamp("date_of_transaction").toLocalDateTime());
                transactions.add(transaction);
            }

            ctx.json(transactions); // Return the transactions as a JSON response
            ctx.status(200); // HTTP 200 OK

        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500); // HTTP 500 Internal Server Error
            ctx.result("Failed to retrieve transactions: " + e.getMessage());
        }
    }
}