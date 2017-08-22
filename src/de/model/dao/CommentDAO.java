package de.model.dao;

import de.db.DBService;
import java.sql.PreparedStatement;

/**
 *
 * @author aydins
 */
public class CommentDAO {

    private static CommentDAO dao;
    private final static String TAB_COMMENT = "tab_comment";
    private final static String VIEW_CARDCOMMENT = "view_cardComment";

    private CommentDAO() {
    }

    public static CommentDAO getInstance() {
        if (dao == null) {
            dao = new CommentDAO();
        }
        return dao;
    }

    public PreparedStatement getCommentCardBetween(boolean justActive){
        String active = justActive ? "True" : "%";
        String sql = "SELECT *"
                + " FROM " + VIEW_CARDCOMMENT 
                + " WHERE c_jokeid = ?"
                + " AND c_active LIKE " + active
                + " AND u_active LIKE " + active
                + " ORDER BY c_rating DESC"
                + " LIMIT ?, ?";
        return DBService.getPreparedStatement(sql);
    }
    public PreparedStatement getCommentById(){
        String sql = "SELECT * "
                + " FROM " + TAB_COMMENT
                + " WHERE com_id = ?"
                + " AND active = True";
        return DBService.getPreparedStatement(sql);
    }
     public PreparedStatement getCommentsByUser(){
        String sql = "SELECT * "
                + " FROM " + TAB_COMMENT
                + " WHERE user_id = ?"
                + " AND active = True";
        return DBService.getPreparedStatement(sql);
    }
}
