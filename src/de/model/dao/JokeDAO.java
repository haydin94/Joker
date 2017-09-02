package de.model.dao;

import java.sql.PreparedStatement;
import de.db.DBService;

public class JokeDAO {

    private static JokeDAO dao;
    private static final String VIEW_CARDJOKETC = "view_cardJokeTC";
    private static final String VIEW_CARDJOKEFAV = "view_cardJokeFav";
    private static final String VIEW_CARDJOKE = "view_cardJoke"; // nur Witz mit jeweiligem Profil
    private static final String VIEW_JOKEVIEW = "view_jokeView";// -> + Kommentare, Witz mit Profil redundant!! 

    private JokeDAO() {
    }

    public static JokeDAO getInstance() {
        if (dao == null) {
            dao = new JokeDAO();
        }
        return dao;
    }

    // Alle Witze View mit filter nach Category + aktive User und Witz, default geordnet nach DESC
    public PreparedStatement getAllJokesViewBetween(boolean asc, boolean justActive) {
        String order = asc ? "ASC" : "DESC";
        String active = justActive ? "True" : "%";
        String sql = "SELECT * "
                + " FROM " + VIEW_CARDJOKETC
                + " WHERE j_category LIKE ?"
                + " AND j_active LIKE " + active
                + " AND uj_active LIKE " + active
                + " ORDER BY j_rating " + order
                + " LIMIT ?, ?";
        return DBService.getPreparedStatement(sql);
    }

    public PreparedStatement getUserJokesBetween(boolean asc, boolean justActiveJokes) {
        String order = asc ? "ASC" : "DESC";
        String sql = "SELECT * "
                + " FROM " + VIEW_CARDJOKETC
                + " WHERE j_userId = ?"
                + " AND j_active = " + justActiveJokes
                + " AND uj_active = True"
                + " ORDER BY j_date " + order
                + " LIMIT ?, ?";
        return DBService.getPreparedStatement(sql);
    }


    public PreparedStatement getUserFavouritesBetween(boolean asc, boolean justActiveJokes) {
        String order = asc ? "ASC" : "DESC";
        String sql = "SELECT * "
                + " FROM " + VIEW_CARDJOKEFAV
                + " WHERE f_userId = ?"
                + " AND j_active = " + justActiveJokes
                + " AND uj_active = True"
                + " ORDER BY j_date " + order
                + " LIMIT ?, ?";
        return DBService.getPreparedStatement(sql);
    }

    public PreparedStatement getJokeCard(boolean justActive) {
        String active = justActive ? "True" : "%";
        String sql = "SELECT * "
                + " FROM " + VIEW_CARDJOKE
                + " WHERE j_id = ?"
                + " AND j_active LIKE " + active
                + " AND u_active LIKE " + active;
        return DBService.getPreparedStatement(sql);
    }

    // Witz View mit allen zugehoerigen Kommentaren, 
    //Erstmal nicht benutzen da Joke immer wieder fuer jeden Kommentar geliefert wird
    public PreparedStatement getJokeView() {
        String sql = "SELECT * "
                + " FROM " + VIEW_JOKEVIEW
                + " WHERE j_id = ?";
        return DBService.getPreparedStatement(sql);
    }

    public PreparedStatement getWitzEntityById() {
        String sql = "SELECT * "
                + " FROM tab_witz"
                + " WHERE witz_id = ?";
        return DBService.getPreparedStatement(sql);
    }

    public PreparedStatement getWitzEntityBetween() {
        String sql = "SELECT * "
                + " FROM tab_witz"
                + " WHERE aktiv = True"
                + " LIMIT ?, ?";
        return DBService.getPreparedStatement(sql);
    }

    public PreparedStatement getCountKommentare() {
        String sql = "SELECT COUNT(k.kom_id)"
                + "FROM tab_kommentar k, tab_witz w "
                + "WHERE k.witz_id = w.witz_id"
                + "AND w.witz_id = ?";
        return DBService.getPreparedStatement(sql);
    }

    public PreparedStatement getWitzBewertetVon() {
        String sql = "SELECT pbw.bewertung"
                + "FROM tab_witz w, tab_profil p, tab_pBewertetW pbw"
                + "WHERE w.witz_id = pbw.witz_id"
                + "AND p.profil_id = pbw.profil_id"
                + "AND w.witz_id = ?"
                + "AND p.profil_id = ?";
        return DBService.getPreparedStatement(sql);
    }
}
