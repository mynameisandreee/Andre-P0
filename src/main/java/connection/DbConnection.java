package connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbConnection {
    //connection to database

    public Connection connect_to_db(String dbname, String username, String password) {
        Connection conn = null;

        try {
            InputStream inputStream = DbConnection.class.getClassLoader().getResourceAsStream("application.properties");
            Properties props = new Properties();
            props.load(inputStream);
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));

            if (conn != null) {
                System.out.println("You are now connected to the database");
            } else {
                System.out.println("Connection failed!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }



}
