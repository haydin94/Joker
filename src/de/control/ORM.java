package de.control;

import de.haydin.model.entities.DataComment;
import de.haydin.model.entities.DataJoke;
import de.haydin.model.entities.DataLUser;
import de.haydin.model.entities.DataUser;
import de.model.entity.DataImage;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author aydins
 */
public class ORM {

    public static DataJoke mapDataJoke(ResultSet rs, String name) throws SQLException {
        prepareResultSet(rs);
        if (name == null || name.equals("")) {
            name = "j";
        }
        DataJoke joke = new DataJoke();
        joke.setJoke_id(rs.getInt(name + "_id"));
        joke.setUser_id(rs.getInt(name + "_userId"));
        joke.setCategory(rs.getString(name + "_category"));
        joke.setDate(rs.getDate(name + "_date").toString());
        joke.setJoke(rs.getString(name + "_joke"));
        joke.setRating(rs.getShort(name + "_rating"));
        joke.setNumRating(rs.getInt(name + "_numRating"));
        joke.setNumComments(rs.getInt(name + "_numComm"));
        joke.setActive(rs.getBoolean(name + "_active"));

        return joke;
    }

    public static DataUser mapDataUser(ResultSet rs, String name) throws SQLException {
        prepareResultSet(rs);
        if (name == null || name.equals("")) {
            name = "u";
        }
        DataUser user = new DataLUser();
        user.setUser_id(rs.getInt(name + "_id"));
        user.setName(rs.getString(name + "_name"));
        user.setAge(rs.getInt(name + "_age"));
        user.setGender(rs.getString(name + "_gender"));
        user.setDate(rs.getDate(name + "_date"));
        user.setDescription(rs.getString(name + "_desc"));
        user.setPlace(rs.getString(name + "_place"));
        user.setFavourite(rs.getInt(name + "_fav"));
        user.setNumFollower(rs.getInt(name + "_numFollower"));
        user.setActive(rs.getBoolean(name + "_active"));
        return user;
    }

    public static DataLUser mapDataLuser(ResultSet rs, String name) throws SQLException {
        prepareResultSet(rs);
        if (name == null || name.equals("")) {
            name = "u";
        }
        DataLUser luser = new DataLUser();
        luser.setUser_id(rs.getInt(name + "_id"));
        luser.setName(rs.getString(name + "_name"));
        luser.setAge(rs.getInt(name + "_age"));
        luser.setGender(rs.getString(name + "_gender"));
        luser.setDate(rs.getDate(name + "_date"));
        luser.setDescription(rs.getString(name + "_desc"));
        luser.setPlace(rs.getString(name + "_place"));
        luser.setFavourite(rs.getInt(name + "_fav"));
        luser.setNumFollower(rs.getInt(name + "_numFollower"));
        luser.setActive(rs.getBoolean(name + "_active"));
        luser.setBirthday(rs.getDate(name + "_birthday"));
        luser.setNumFollows(rs.getShort(name + "_numFollows"));
        luser.setEmail(rs.getString(name + "_email"));
        luser.setPassword(rs.getString(name + "_password"));
        return luser;
    }

    public static DataComment mapDataComment(ResultSet rs, String name) throws SQLException {
        prepareResultSet(rs);
        if (name == null || name.equals("")) {
            name = "c";
        }
        DataComment comment = new DataComment();
        comment.setCom_id(rs.getInt(name + "_id"));
        comment.setUser_id(rs.getInt(name + "_userId"));
        comment.setJoke_id(rs.getInt(name + "_jokeId"));
        comment.setDate(rs.getDate(name + "_date"));
        comment.setComment(rs.getString(name + "_comment"));
        comment.setRating(rs.getShort(name + "_rating"));
        comment.setNumRating(rs.getInt(name + "_numRating"));
        comment.setActive(rs.getBoolean(name + "_active"));

        return comment;
    }

    public static DataImage mapDataImage(ResultSet rs) throws SQLException {
        prepareResultSet(rs);
        DataImage img = new DataImage();
        img.setImage_id(rs.getInt("image_id"));
        img.setUser_id(rs.getInt("user_id"));
        img.setDate(rs.getDate("date").toString());
        img.setPathNorm(rs.getString("pathNorm"));
        img.setPathThumb(rs.getString("pathThumb"));
        img.setActive(rs.getBoolean("active"));
        return img;
    }

    private static void prepareResultSet(ResultSet rs) throws SQLException {
        if (rs.isBeforeFirst()) {   // You have to execute "next()" min one time before you can start reading data
            rs.next();
        }
    }
}
