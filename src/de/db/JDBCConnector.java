package de.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import de.services.exceptions.DatabaseException;

public class JDBCConnector {

    private final static String DB_DRIVER = "jdbc:mysql://";
    private final static String DB_HOST = "mysql:";//localhost/";
    private final static String DB_PORT = "3306/";//8080
    private final static String DB_NAME = "JokeeDB";
    static String url = "jdbc:mysql://localhost/Jokee";
//    private final static String DB_URL = url;
    private final static String DB_URL = DB_DRIVER + DB_HOST + DB_PORT + DB_NAME + "?autoReconnect=true&useSSL=false";
    private static Connection con;
    private static Statement stmt;
    private static final String DB_USER = "root";
    private static final String DB_PWD = "neuwied94";

    public static void initConnection() throws DatabaseException {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                Properties props = new Properties();
                props.setProperty("user", DB_USER);
                props.setProperty("password", DB_PWD);
                con = DriverManager.getConnection(DB_URL, props);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Statement getStatement() throws DatabaseException {
        Statement stmt = null;
        try {
            if (con.isClosed()) {
                initConnection();
            }
            stmt = con.createStatement();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return stmt;
    }

    public static PreparedStatement getPreparedStatement(String pstmt) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            if (con.isClosed()) {
                initConnection();
            }
            stmt = con.prepareStatement(pstmt, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return stmt;
    }

    public static void closeConnect() throws DatabaseException {
        try {
            con.close();
        } catch (SQLException ex) {
            throw new DatabaseException();
        }
    }

    public static String getDB_URL() {
        return DB_URL;
    }

    public static String getDB_USER() {
        return DB_USER;
    }

    public static String getDB_PWD() {
        return DB_PWD;
    }

}
