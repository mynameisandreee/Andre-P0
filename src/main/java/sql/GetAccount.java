package sql;

import Account.Account;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetAccount {
    Connection conn;

    public GetAccount(Connection conn){
        this.conn= conn;
    }

    public Account getAccountByUsername(String username) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Account account = null;

        try {
            String sql = "SELECT a.account_id, a.user_id, a.balance FROM account a JOIN users u ON a.user_id = u.user_id WHERE u.username = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            rs = statement.executeQuery();

            if (rs.next()) {
                account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setUserId(rs.getInt("user_id"));
                account.setBalance(rs.getBigDecimal("balance"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching account data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return account;
    }

}
