import connection.DbConnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import controllers.UserController;
import io.javalin.Javalin;
import model.User;
import sql.CreateUser;
import sql.GetUser;
import util.ConnectionManager;

public class main {

    public static void main(String[] args) throws IOException {
        Javalin app = Javalin.create().start(8888);
        UserController userController = new UserController();
        userController.userController(app, ConnectionManager.getConn());

    }
}
