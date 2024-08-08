package sql;

import io.javalin.http.Context;
import model.User;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class CreateUser {

    public void createUser( Context ctx, Connection conn){

        User user = ctx.bodyAsClass(User.class);

        try {
            String sql = "INSERT INTO users (first_name, last_name, username, password) VALUES (?, ?, ?, ?)";

            PreparedStatement ptsmt = conn.prepareStatement(sql);
            ptsmt.setString(1, user.getFirstName());
            ptsmt.setString(2, user.getLastName());
            ptsmt.setString(3, user.getUsername());
            ptsmt.setString(4, user.getPassword());

            ptsmt.executeUpdate();

            ctx.status(201); // Created status
            ctx.result("User Created");

        }catch (Exception e){
            ctx.status(500); // Internal Server Error status
            ctx.result("Error creating user: " + e.getMessage());
        }
    }
}
