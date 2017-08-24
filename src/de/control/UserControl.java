package de.control;

import de.db.DBService;
import de.haydin.model.entities.DataLUser;
import de.haydin.model.unions.DtoUserView;
import de.model.dao.UserDAO;
import de.services.exceptions.DatabaseException;
import de.services.exceptions.DatabaseInconsistenceException;
import de.services.exceptions.EmptyResultException;
import de.services.exceptions.NoSuchUserException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aydins
 */
public class UserControl {

    private static UserControl control;

    private UserControl() {
    }

    public static UserControl getInstance() {
        if (control == null) {
            control = new UserControl();
        }
        return control;
    }

//    public DataUser getUserById(int id) throws DatabaseException, SqlQueryException, EmptyResultException {
//        // Get ProfilTab and BildTab
//        // Merge to One Result and create DataProfil
//        if (listProf == null || listProf.isEmpty()) {
//            throw new de.services.exceptions.EmptyResultException("Die Abfrage lieferte kein Ergebnis!");
//        }
//        System.out.println("de.control.ProfilControl.getProfilById():");
//        for (int i = 0; i < listProf.size(); i++) {
//            for (int j = 0; j < listProf.get(0).length; j++) {
//                System.out.println(listProf.get(i)[j]);
//            }
//        }
//        DataUser profil = new DataUser();
//
//        return profil;
//    }
//    public DataUser getLuserById(int id) throws DatabaseException, SqlQueryException, EmptyResultException {
//        // Get ProfilTab and BildTab
//        PreparedStatement ps = UserDAO.getInstance().getLuser(true);
//        DBService.setInt(ps, 1, id);
//        ArrayList<Object[]> listProf = DBService.execPrepStmtRetAL(ps);
//        // Merge to One Result and create DataProfil
//        if (listProf == null || listProf.isEmpty()) {
//            throw new de.services.exceptions.EmptyResultException("Die Abfrage lieferte kein Ergebnis!");
//        }
//        System.out.println("de.control.ProfilControl.getProfilById():");
//        for (int i = 0; i < listProf.size(); i++) {
//            for (int j = 0; j < listProf.get(0).length; j++) {
//                System.out.println(listProf.get(i)[j]);
//            }
//        }
//        DataLUser user = new DataLUser();
//
//        return user;
//    }
    public DtoUserView getUserView(int userId, boolean logged, int start, int count) throws EmptyResultException, DatabaseException {
        ViewFactory factory = ViewFactory.getInstance();

        DtoUserView result = factory.createUserView(userId, logged, start, count);
        if (result == null || result.isEmpty()) {
            throw new de.services.exceptions.EmptyResultException("The Query didn´t delivered a result!");
        }
        return result;
    }

    public DataLUser checkAuthentication(String email, String passwort) throws DatabaseException, NoSuchUserException, DatabaseInconsistenceException, EmptyResultException {
        PreparedStatement psLogin = UserDAO.getInstance().checkLogin();
        DBService.setString(psLogin, 1, email);
        DBService.setString(psLogin, 2, email);
        DBService.setString(psLogin, 3, passwort);

        ResultSet rs = DBService.execPrepStmt(psLogin);
        if (rs == null) {
            throw new NoSuchUserException("User not found!");
        }
//        if (rs.relative(2) > 1) {
//            throw new DatabaseInconsistenceException("FATAL ERROR! More than one User was found, Database is inconsistent");
//        }
        DataLUser result = null;
        try {
            result = ORM.mapDataLuser(rs, "u");
        } catch (SQLException ex) {
            Logger.getLogger(UserControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
