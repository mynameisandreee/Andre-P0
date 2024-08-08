package sql;

import io.javalin.http.Context;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateUser {


    public static void updateUser(Context ctx, Connection conn) {

        User user = ctx.bodyAsClass(User.class);
       String username = ctx.pathParam("username");

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String updateUserInfoSql = "UPDATE users SET first_name = ?, last_name = ? WHERE username = ?";

        try (PreparedStatement updateStmt = conn.prepareStatement(updateUserInfoSql)) {
            updateStmt.setString(1, firstName);
            updateStmt.setString(2, lastName);
            updateStmt.setString(3, username);

            int affectedRows = updateStmt.executeUpdate();
            if (affectedRows > 0) {
                ctx.status(200).result("User information updated successfully.");
                System.out.println("Username: " + username);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
            } else {
                ctx.status(404).result("User not found or no changes were made.");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        }
    }
}
