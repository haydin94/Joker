package de.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.services.exceptions.DatabaseException;
import de.services.exceptions.SqlQueryException;

/**
 * @author haydin
 */
// Schnittstelle zwischen der Anwendungslogik und der Datenbank
public class DBService {

    private static ResultSet rs;
    private static PreparedStatement ps;

    public static ResultSet getLastResult() {
        return rs;
    }
    
    public static ArrayList<Object[]> execPrepStmtRetAL(PreparedStatement ps) throws DatabaseException, SqlQueryException {
        ArrayList<Object[]> aL;
        try {
            rs = ps.executeQuery();
            aL = convertRsToAl(rs);
        } catch (SQLException ex) {
            throw new SqlQueryException("Fehler im SQL Code: " + ex.getMessage());
        }
        return aL;
    }
    public static ResultSet execPrepStmt(PreparedStatement ps) throws DatabaseException, SqlQueryException {
        try {
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            throw new SqlQueryException("Fehler im SQL Code: " + ex.getMessage());
        }
        return rs;
    }
    
    // Für abfragen mit Rückgabe als ResultSet
    public static ArrayList<Object[]> executeQueryReturnAL(String sql) throws DatabaseException, SqlQueryException {
        Statement stmt = JDBCConnector.getStatement();
        ArrayList<Object[]> aL;
        try {
            rs = stmt.executeQuery(sql);
            aL = convertRsToAl(rs);
        } catch (SQLException ex) {
            throw new SqlQueryException("Fehler im SQL Code: " + ex.getMessage());
        }
        return aL;
    }
    

    // Für Abfragen ohne Rückgabe, zum eintragen, löschen und ändern von Werten
    public static void executeUpdate(String sql) throws DatabaseException, SqlQueryException {
        Statement stmt = JDBCConnector.getStatement();
        try {
            stmt.execute(sql);
        } catch (SQLException ex) {
            throw new SqlQueryException("Fehler im SQL Code: " + ex.getMessage());
        }
    }

    public static ArrayList<Object[]> convertRsToAl(ResultSet rs) throws SQLException {
        ArrayList<Object[]> aL = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int colSize = rsmd.getColumnCount();

        while (rs.next()) {
            /* Wichtig: bei i=1 anfangen! */
            String[] s = new String[colSize];
            for (int j = 1; j <= colSize; j++) {
                if (rs.getString(j) == null) {
                    s[j - 1] = "0";
                } else {
                    s[j - 1] = rs.getString(j);
                }
            }
            aL.add(s);
        }
        return aL;
    }

    // Liefert anzahl Zeilen die das ResultSet besitzt zurück
    public static int getSizeOfRs(ResultSet rs) throws SQLException {
        rs.last();
        int i = rs.getRow();
        rs.beforeFirst();
        return i;
    }

    public static PreparedStatement getPreparedStatement(String sql) {
        try {
            ps = JDBCConnector.getPreparedStatement(sql);
        } catch (DatabaseException ex) {
            Logger.getLogger(DBService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ps;
    }

    public static ArrayList<String> getColumns() throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        ArrayList<String> aL = new ArrayList<>();

        for (int i = 1; i <= columnCount; i++) {
            aL.add(rsmd.getColumnName(i));
        }
        return aL;
    }

    public static void setString(PreparedStatement ps, int i, String value) {
        try {
            ps.setString(i, value);
        } catch (SQLException ex) {
            Logger.getLogger(DBService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setInt(PreparedStatement ps, int i, int value) {
        try {
            ps.setInt(i, value);
        } catch (SQLException ex) {
            Logger.getLogger(DBService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
