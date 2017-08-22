package de.control;

import de.db.DBService;
import de.haydin.model.entities.DataLUser;
import de.haydin.model.entities.DataUser;
import de.haydin.model.unions.DtoCardComment;
import de.haydin.model.unions.DtoCardJokeTC;
import de.haydin.model.unions.DtoJokeView;
import de.haydin.model.unions.DtoUserView;
import de.haydin.model.utils.ViewFactoryIF;
import de.model.dao.CommentDAO;
import de.model.dao.JokeDAO;
import de.model.dao.UserDAO;
import de.model.entity.DataImage;
import de.services.exceptions.DatabaseException;
import de.services.exceptions.SqlQueryException;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aydins
 */
public class ViewFactory implements ViewFactoryIF {

    private static ViewFactory factory;

    private ViewFactory() {
    }

    public static ViewFactory getInstance() {
        if (factory == null) {
            factory = new ViewFactory();
        }
        return factory;
    }

    @Override
    public DtoJokeView createJokeView(int jokeId, boolean justComments, int start, int count) {
        DtoJokeView result = new DtoJokeView();
        try {
            if (!justComments) {        // if not just Comments -> means also the JokeCard
                PreparedStatement ps = JokeDAO.getInstance().getJokeCard(true);
                DBService.setInt(ps, 1, jokeId);
                ResultSet rs = DBService.execPrepStmt(ps);
                result.setJoke(ORM.mapDataJoke(rs, "j"));
                result.setUserJoke(ORM.mapDataUser(rs, "u"));
                createUserImage(result.getUserJoke(), false);
            }
            // Always get Comments
            result.setComments(createCommentCards(jokeId, start, count));
        } catch (DatabaseException | SqlQueryException | SQLException ex) {
            Logger.getLogger(ViewFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        result.setComments(createCommentCards(jokeId, start, count));
        return result;
    }

    @Override
    public DtoUserView createUserView(int userId, boolean logged, int start, int count) {
        DtoUserView result = new DtoUserView();
        if (logged) {
            result.setUser(createLUser(userId));
        } else {
            result.setUser(createUser(userId));
        }
        System.out.println("de.control.ViewFactory.createUserView(): RESULT= \n " + result.getUser());
        result.setJokes(createUserJokes(userId, start, count));
        return result;

    }

    @Override
    public ArrayList<DtoCardComment> createCommentCards(int jokeId, int start, int count) {
        ArrayList<DtoCardComment> result = new ArrayList<>();
        PreparedStatement ps = CommentDAO.getInstance().getCommentCardBetween(true);
        DBService.setInt(ps, 1, jokeId);
        DBService.setInt(ps, 2, start);
        DBService.setInt(ps, 3, count);
        try {
            ResultSet rs = DBService.execPrepStmt(ps);
            while (rs.next()) {
                DtoCardComment comment = new DtoCardComment();
                comment.setComment(ORM.mapDataComment(rs, "c"));
                comment.setUser(ORM.mapDataUser(rs, "u"));
                createUserImage(comment.getUser(), true);
                result.add(comment);
            }
        } catch (DatabaseException | SqlQueryException | SQLException ex) {
            Logger.getLogger(ViewFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<DtoCardJokeTC> createAllJokesView(String category, int start, int count) {
        category = category == null || category.equals("") ? "%" : category;
        PreparedStatement ps = JokeDAO.getInstance().getAllJokesViewBetween(true, true);
        DBService.setString(ps, 1, category);
        DBService.setInt(ps, 2, start);
        DBService.setInt(ps, 3, count);
        ArrayList<DtoCardJokeTC> result = new ArrayList<>();
        try {
            ResultSet rs = DBService.execPrepStmt(ps);
            result = mapCardJokeTC(rs);
        } catch (DatabaseException | SqlQueryException | SQLException ex) {
            Logger.getLogger(ViewFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private ArrayList<DtoCardJokeTC> createUserJokes(int userId, int start, int count) {
        PreparedStatement ps = JokeDAO.getInstance().getUserJokesBetween(true, true);
        DBService.setInt(ps, 1, userId);
        DBService.setInt(ps, 2, start);
        DBService.setInt(ps, 3, count);
        ArrayList<DtoCardJokeTC> result = new ArrayList<>();
        try {
            ResultSet rs = DBService.execPrepStmt(ps);
            result = mapCardJokeTC(rs);
        } catch (DatabaseException | SqlQueryException | SQLException ex) {
            Logger.getLogger(ViewFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private ArrayList<DtoCardJokeTC> mapCardJokeTC(ResultSet rs) throws SQLException {
        ArrayList<DtoCardJokeTC> result = new ArrayList<>();

        while (rs.next()) {
            DtoCardJokeTC card = new DtoCardJokeTC();
            // map user
            DataUser userJoke = ORM.mapDataUser(rs, "uj");
            DataUser userComment = ORM.mapDataUser(rs, "uc");
            // set User images
            createUserImage(userJoke, true);
            createUserImage(userComment, true);

            // Map ResultSet to DataStructures and add to Result
            card.setJoke(ORM.mapDataJoke(rs, "j"));
            card.setComment(ORM.mapDataComment(rs, "c"));
            card.setUserJoke(userJoke);
            card.setUserComment(userComment);
            result.add(card);
        }
        return result;
    }

    private DataUser createUser(int userId) {
        DataUser user = null;
        PreparedStatement ps = UserDAO.getInstance().getUser(true);
        DBService.setInt(ps, 1, userId);
        ResultSet rs;
        try {
            rs = DBService.execPrepStmt(ps);
            user = ORM.mapDataUser(rs, "u");

        } catch (DatabaseException | SqlQueryException | SQLException ex) {
            Logger.getLogger(ViewFactory.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    private DataLUser createLUser(int userId) {
        DataLUser user = null;
        PreparedStatement ps = UserDAO.getInstance().getLuser(true);
        DBService.setInt(ps, 1, userId);
        ResultSet rs;
        try {
            rs = DBService.execPrepStmt(ps);
            user = ORM.mapDataLuser(rs, "u");
        } catch (DatabaseException | SqlQueryException | SQLException ex) {
            Logger.getLogger(ViewFactory.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("de.control.ViewFactory.createLUser(): USERID=" + userId);
        return user;
    }

    private void createUserImage(DataUser user, boolean thumb) {
        PreparedStatement ps = UserDAO.getInstance().getImgByUserId();
        DBService.setInt(ps, 1, user.getUser_id());
        try {
            ResultSet rs = DBService.execPrepStmt(ps);
            DataImage img = ORM.mapDataImage(rs);
            String path = "";
            if (thumb) {
                path = img.getPathThumb() + img.getUser_id() + img.getImage_id();
            } else {
                path = img.getPathNorm() + img.getUser_id() + img.getImage_id();
            }
            user.setImg(new File(path));
        } catch (DatabaseException | SqlQueryException | SQLException ex) {
            System.out.println("ViewFactory.createUserImage(): Img for User " + user.getUser_id() + " cannot be load!");
//            Logger.getLogger(ViewFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
