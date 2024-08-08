package authentication;

import exceptions.BadPassword;
import exceptions.NoUser;
import model.User;
import sql.GetUser;
import util.ConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UserAuthenticate {
    //We need to create connection and pass it into the paramater
    Connection conn = ConnectionManager.getConn();

    GetUser getUser = new GetUser(conn);

    public UserAuthenticate() throws IOException {
    }

    public String authenticateUser(String username, String password) throws SQLException, NoUser, BadPassword {

        User result;
        result = getUser.getUserByUsername(username);
        String success = "Log in success! " + username;
        if (result.getPassword().equals(password)) {

            return success;
        } else {
            throw new BadPassword("Password mismatch!");
        }
    }

}
