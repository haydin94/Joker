package de.clientIF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.control.UserControl;
import de.control.JokeControl;
import de.control.ViewFactory;
import de.db.JDBCConnector;
import de.haydin.model.entities.DataUser;
import de.haydin.model.entities.DataJoke;
import de.haydin.model.entities.DataLUser;
import de.haydin.model.unions.DtoJokeView;
import de.haydin.model.unions.DtoCardJokeTC;
import de.haydin.model.unions.DtoUserView;
import de.haydin.service.utils.Requests;
import de.haydin.service.utils.ResponseCodes;
import de.services.exceptions.DatabaseException;
import de.services.exceptions.DatabaseInconsistenceException;
import de.services.exceptions.EmptyBodyException;
import de.services.exceptions.EmptyResultException;
import de.services.exceptions.NoSuchUserException;
import de.services.exceptions.SqlQueryException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/Servlet")
public class ServerEntry extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = new PrintWriter(response.getOutputStream());
        out.println("Willkommen beim Jokee-Server!");
        out.println("Bitte benutzen Sie die Post Methode um eine Anfrage zu stellen");
        out.println("\n --> Anfrage von " + request.getLocalAddr());
        out.println("RequestMethod: " + request.getMethod());
        out.println("RequestURL: " + request.getRequestURL());
        out.println("RequestLength: " + request.getContentLength());
        out.println("RequestType: " + request.getContentType());
        try {
            out.println("Conneting to Database with URL: " + JDBCConnector.getDB_URL());
            out.println("User: " + JDBCConnector.getDB_USER() + " Pwd: " + JDBCConnector.getDB_PWD());
            JDBCConnector.initConnection();
            out.println("Connection to Database etablished!");
            out.println("JokeView: ID = 1, justComments = false, start = 0, count = 20");
            DtoJokeView jv = ViewFactory.getInstance().createJokeView(1, false, 0, 20);
            out.println(jv);
            writeResponse(response, jv);
            JDBCConnector.closeConnect();
        } catch (DatabaseException ex) {
            out.println("Connection to Database could not be etablished!");
            out.println("MESSAGE: " + ex.getMessage());
            out.println("LOCALIZEDMESSAGE: " + ex.getLocalizedMessage());
        }
        out.flush();
        out.close();
    }

    /**
     * @param request
     * @param response
     * @throws java.io.IOException
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Beginne DoPost");
        try {
            System.out.println("Stelle Datenbankverbindung her...");
            JDBCConnector.initConnection();
            System.out.println("Datenbankverbindung hergestellt");
        } catch (DatabaseException ex) {
            PrintWriter out = new PrintWriter(response.getOutputStream());
            System.out.println("Connection to Database could not be etablished!");
            System.out.println("MESSAGE: " + ex.getMessage());
            System.out.println("LOCALIZEDMESSAGE: " + ex.getLocalizedMessage());
        }

        // Lese Anfrage (Body)
        // Muss unbedingt VOR getParameter gelesen werden!!
        String[] reqBody = null;
        try {
            System.out.println("Lese Anfrage");
            reqBody = readRequest(request.getReader(), request.getContentLength());
        } catch (EmptyBodyException e) {
            response.sendError(ResponseCodes.EMPTYBODYERROR, "Error! RequestBody is empty!");
        }
        String type = request.getParameter(Requests.ParamType.TYPE);
        String req = request.getParameter(Requests.ParamType.REQUEST);
        printRequestInfo(request, reqBody);
        System.out.println(Requests.ParamType.TYPE + "=" + type);
        System.out.println(Requests.ParamType.REQUEST + "=" + req);
        if (type != null && type.equals(Requests.ParamValue.UPDATE)) {
        } else {
            try {
                handleSelectRequest(req, reqBody, response);
            } catch (DatabaseException e) {
                response.sendError(ResponseCodes.DATABSEERROR, "Error while connecting to the Database!");
                e.printStackTrace();
            } catch (SqlQueryException e) {
                response.sendError(ResponseCodes.SQLERROR, "Error in SQL code, please contact the programmer!");
                e.printStackTrace();
            } catch (EmptyResultException e) {
                response.sendError(ResponseCodes.SQLERROR, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void handleSelectRequest(String req, String[] strRequest, HttpServletResponse response) throws IOException, DatabaseException, SqlQueryException, EmptyResultException {
        switch (req) {
            case Requests.ParamValue.ParamSelect.CARD_USER:
                reqUser(strRequest[0], response);
                break;
            case Requests.ParamValue.ParamSelect.CARD_JOKE:
                reqJoke(strRequest[0], response);
                break;
            case Requests.ParamValue.ParamSelect.LOGIN:
                reqLogin(strRequest, response);
                break;
            case Requests.ParamValue.ParamSelect.VIEW_ALLJOKES:
                reqViewAllJokes(strRequest, response);
                break;
            case Requests.ParamValue.ParamSelect.VIEW_JOKEVIEW:
                reqJokeView(strRequest, response);
                break;
            case Requests.ParamValue.ParamSelect.VIEW_USERVIEW:
                reqUserView(strRequest, response);
                break;
            case Requests.ParamValue.ParamSelect.WITZ_BETWEEN:
                break;

            default:
        }
    }

    private void reqLogin(String[] request, HttpServletResponse response) throws DatabaseException, SqlQueryException, EmptyResultException, IOException {
        if (request != null && request.length < 2) {
            return; // THROW ERROR !!
        }
        try {
            DataLUser user = UserControl.getInstance().checkAuthentication(request[0], request[1]);
            writeResponse(response, user);
        } catch (NoSuchUserException | DatabaseInconsistenceException ex) {
            Logger.getLogger(ServerEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reqViewAllJokes(String[] request, HttpServletResponse response) throws IOException, EmptyResultException, DatabaseException, SqlQueryException {
        if (request != null && request.length < 3) {
            return; // THROW ERROR !!
        }
        String category = request[0];
        int start = Integer.parseInt(request[1]);
        int length = Integer.parseInt(request[2]);
        System.out.println("reqViewAllJokes: category=" + category + ", s=" + start + ", l=" + length);
        ArrayList<DtoCardJokeTC> result = null;
        result = JokeControl.getInstance().getAllJokesView(category, start, length);
        writeResponse(response, result);
    }

    private void reqJokeView(String[] request, HttpServletResponse response) throws IOException, DatabaseException, SqlQueryException, EmptyResultException {
        if (request != null && request.length < 4) {
            System.out.println("requestLength == 0 | < 4");
            return; // THROW ERROR !!
        }
        int id = Integer.parseInt(request[0]);
        boolean justCom = Boolean.parseBoolean(request[1]);
        int start = Integer.parseInt(request[2]);
        int count = Integer.parseInt(request[3]);

        System.out.println("reqJokeView id=" + id + ", justCom=" + justCom + ", start=" + start + ", count=" + count);
        DtoJokeView result = JokeControl.getInstance().getJokeView(id, justCom, start, count);
        writeResponse(response, result);
    }

    private void reqUserView(String[] request, HttpServletResponse response) throws EmptyResultException, IOException {
        if (request != null && request.length < 4) {
            return; // THROW ERROR !!
        }
        boolean logged = request[0].equals("l");
        int id = Integer.parseInt(request[1]);
        boolean justCom = Boolean.parseBoolean(request[2]);
        int start = Integer.parseInt(request[3]);
        int count = Integer.parseInt(request[4]);

        System.out.println("reqUserView logged=" + logged + ", id=" + id + ", justCom=" + justCom + ", start=" + start + ", count=" + count);
        DtoUserView result = UserControl.getInstance().getUserView(id, logged, start, count);
        System.out.println("de.clientIF.ServerEntry.reqUserView(): RESULT = \n" + result);
        writeResponse(response, result);
    }

    private void reqUser(String request, HttpServletResponse response) throws IOException, DatabaseException, SqlQueryException, EmptyResultException {
        int id = -1;
        id = Integer.parseInt(request);
        System.out.println("reqProfil id = " + id);
        DataUser result = null;
//        result = UserControl.getInstance().getUserById(id);
        writeResponse(response, result);
    }

    private void reqJoke(String request, HttpServletResponse response) throws IOException, DatabaseException, SqlQueryException, EmptyResultException {
        int id = -1;
        id = Integer.parseInt(request);
        System.out.println("reqWitz id = " + id);
        DataJoke result = null;
//        result = JokeControl.getInstance().getWitzEntityById(id);
        writeResponse(response, result);
    }

    private String[] readRequest(BufferedReader reader, int size) throws IOException, EmptyBodyException {
        ArrayList<String> list = new ArrayList<>();

        int i = 0;
        String tmp = null;
        while ((tmp = reader.readLine()) != null) {
            list.add(tmp);
            i++;
            System.out.println("Body: " + tmp);
        }
        try {
            reader.reset();
            reader.close();
        } catch (IOException ex) {
            System.out.println("reader fehler " + ex.getMessage());
            ex.printStackTrace();
        }
        if (list.get(0) == null) {
            System.out.println("Verdammt warum EmptyBody??");
            throw new EmptyBodyException();
        }

        String[] result = (String[]) list.toArray(new String[list.size()]);
        System.out.println("ReadRequest ende!");
        return result;
    }

    private void writeResponse(HttpServletResponse response, Object responseObj) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
        try {
            out.writeObject(responseObj);
        } catch (IOException ex) {
            response.sendError(ResponseCodes.IOERROR, ex.getMessage());
        }
        out.close();
    }

    private void printRequestInfo(HttpServletRequest request, String[] body) {
        System.out.println("\n --> Anfrage von " + request.getLocalAddr());
        System.out.println("RequestMethod: " + request.getMethod());
        System.out.println("RequestURL: " + request.getRequestURL());
        System.out.println("RequestLength: " + request.getContentLength());
        System.out.println("RequestType: " + request.getContentType());
        for (int i = 0; i < body.length; i++) {
            System.out.println("ReqBody " + i + ": " + body[i]);
        }
    }

}
