package util;

import connection.DbConnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionManager {

    public static Connection getConn() throws IOException {
        InputStream inputStream = DbConnection.class.getClassLoader().getResourceAsStream("application.properties");
        Properties props = new Properties();
        props.load(inputStream);
        DbConnection db = new DbConnection();
        Connection conn = db.connect_to_db( props.getProperty("dbname"), props.getProperty("username"), props.getProperty("password"));
        return conn;
    }
}
