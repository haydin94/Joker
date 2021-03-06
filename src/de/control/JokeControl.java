package de.control;

import de.haydin.model.entities.DataComment;
import java.util.ArrayList;
import de.haydin.model.unions.DtoJokeView;
import de.haydin.model.unions.DtoCardJokeTC;
import de.services.exceptions.DatabaseException;
import de.services.exceptions.EmptyResultException;
import java.sql.SQLException;

public class JokeControl {

    private static JokeControl control;

    private JokeControl() {
    }

    public static JokeControl getInstance() {
        if (control == null) {
            control = new JokeControl();
        }
        return control;
    }

    public ArrayList<DtoCardJokeTC> getAllJokesView(String category, int start, int count) throws EmptyResultException, DatabaseException {
        ViewFactory factory = ViewFactory.getInstance();

        ArrayList<DtoCardJokeTC> result = factory.createAllJokesView(category, start, count);
        if (result == null || result.isEmpty()) {
            throw new de.services.exceptions.EmptyResultException("The Query did not deliver any result!");
        }
        return result;
    }

    public ArrayList<DtoCardJokeTC> getUserFavourites(int userId, int start, int count) throws EmptyResultException, DatabaseException {
        ViewFactory factory = ViewFactory.getInstance();

        ArrayList<DtoCardJokeTC> result = factory.createUserFavourites(userId, start, count);
        if (result == null || result.isEmpty()) {
            throw new de.services.exceptions.EmptyResultException("The Query did not deliver any result!");
        }
        return result;
    }
    /*
    * Vorläufig hier, später evtl in CommentControl
    */
    public ArrayList<DataComment> getUserComments(int userId, int start, int count) throws EmptyResultException, DatabaseException {
        ViewFactory factory = ViewFactory.getInstance();

        ArrayList<DataComment> result = factory.createUserComments(userId, start, count);
        if (result == null || result.isEmpty()) {
            throw new de.services.exceptions.EmptyResultException("The Query did not deliver any result!");
        }
        return result;
    }
    
    public ArrayList<DtoCardJokeTC> getUserJokes(int userId, int start, int count) throws EmptyResultException, DatabaseException {
        ViewFactory factory = ViewFactory.getInstance();

        ArrayList<DtoCardJokeTC> result = factory.createUserJokes(userId, start, count);
        if (result == null || result.isEmpty()) {
            throw new de.services.exceptions.EmptyResultException("The Query did not deliver any result!");
        }
        return result;
    }

    public DtoJokeView getJokeView(int jokeId, boolean justComments, int start, int count) throws DatabaseException, EmptyResultException, SQLException {
        ViewFactory factory = ViewFactory.getInstance();
        DtoJokeView result = factory.createJokeView(jokeId, justComments, start, count);

        if (result == null) {
            throw new de.services.exceptions.EmptyResultException("The Query did not delivered a result!");
        }
        return result;
    }

}
