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

    private final static String url = "jdbc:mysql://localhost/Jokee";
    private static Connection con;
    private static Statement stmt;
    private static String user = "root";
    private static String pw = "";

    public static void initConnection() throws DatabaseException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties props = new Properties();
            props.setProperty("user", user);
//			props.setProperty("password", pw);
            con = DriverManager.getConnection(url, props);
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

}
