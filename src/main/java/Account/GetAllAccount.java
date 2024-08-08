package Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetAllAccount {
    Connection conn;

    public GetAllAccount(Connection conn) {
        this.conn = conn;
    }

    public static List<Account> getAccount(Connection conn) {
        Statement statement = null;
        ResultSet rs = null;
        List<Account> accounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM account";
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setUserId(rs.getInt("user_id"));
                account.setUsername(rs.getString("username"));
                account.setBalance(rs.getBigDecimal("balance"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return accounts;
    }
}
