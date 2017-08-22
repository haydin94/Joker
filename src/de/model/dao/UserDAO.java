package de.model.dao;

import java.sql.PreparedStatement;
import de.db.DBService;

public class UserDAO {

    public static UserDAO dao;
    private static final String VIEW_ELEM_USER = "view_elem_user";
    private static final String VIEW_ELEM_LUSER = "view_elem_luser";
    private static final String TAB_USER = "tab_user";
    private static final String TAB_IMAGE = "tab_image";

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        if (dao == null) {
            dao = new UserDAO();
        }
        return dao;
    }

    public PreparedStatement getUser(boolean isActive) {       // User karte wo alle nötigen Informationen draufstehen
        String active = isActive ? "True" : "%";
        String sql = "SELECT * "
                + " FROM " + VIEW_ELEM_USER
                + " WHERE u_id = ?"
                + " AND u_active LIKE " + active;
        return DBService.getPreparedStatement(sql);
    }
    
    public PreparedStatement getLuser(boolean isActive) {       // User karte wo alle nötigen Informationen draufstehen
        String active = isActive ? "True" : "%";
        String sql = "SELECT * "
                + " FROM " + VIEW_ELEM_LUSER
                + " WHERE u_id = ?"
                + " AND u_active LIKE " + active;
        return DBService.getPreparedStatement(sql);
    }

    public PreparedStatement checkLogin() {
        String sql = "SELECT u_id"
                + " FROM " + VIEW_ELEM_LUSER
                + " WHERE (u_email = ?"
                + " OR u_name = ?)"
                + " AND u_password = ?"
                + " AND u_active = True";
        return DBService.getPreparedStatement(sql);
    }
    
    public PreparedStatement getImgByUserId() {
        String sql = "SELECT * "
                + " FROM " + TAB_IMAGE
                + " WHERE user_id = ?"
                + " AND active = True";
        return DBService.getPreparedStatement(sql);
    }

}
