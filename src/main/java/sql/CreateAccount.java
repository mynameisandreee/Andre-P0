package sql;

import Account.Account;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateAccount {


    public void createAccount(Context ctx, Connection conn)  {
        Account account = ctx.bodyAsClass(Account.class);

        String sql = "INSERT INTO account (user_id, balance) SELECT user_id, 0.00 FROM users u WHERE u.user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, account.getUser_id());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                ctx.status(201); // HTTP 201 Created
                ctx.result("Account created successfully");
            } else {
                ctx.status(500); // HTTP 500 Internal Server Error
                ctx.result("Failed to create account user does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500); // HTTP 500 Internal Server Error
            ctx.result("Failed to create account: " + e.getMessage());
        }
    }

 }



