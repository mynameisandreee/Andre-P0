package sql;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetUser {
    //Dependency inject the connection object into our database
    Connection conn;

    public GetUser(Connection conn){
        this.conn = conn;
    }


    public List<User> getUser(Connection conn) {
        Statement statement = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            String sql = "SELECT * FROM users";
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);



            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));

                users.add((user));

            }



        } catch (Exception e) {
            System.out.println("Error fetching user data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return users;
    }

    public User getUserByUsername( String username) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        User user = null;

        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            rs = statement.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            System.out.println("Error fetching user data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return user;
    }









}

