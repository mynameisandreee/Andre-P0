package sql;

import Account.Account;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAccount {

    public void deleteAccount(Context ctx, Connection conn) {

        Account account = ctx.bodyAsClass(Account.class);



        String sql = "DELETE FROM account WHERE account_id = ? AND balance = 0.00";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, account.getAccount_id());

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                ctx.status(200); // HTTP 200 OK
                ctx.result("Account deleted successfully");
            } else {
                ctx.status(404); // HTTP 404 Not Found
                ctx.result("Failed to delete account: Account not found or balance is not zero");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500); // HTTP 500 Internal Server Error
            ctx.result("Failed to delete account: " + e.getMessage());
        }
    }

}
