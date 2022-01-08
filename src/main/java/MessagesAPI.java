import Database_HY359.src.database.tables.EditMessageTable;
import Database_HY359.src.database.tables.EditRandevouzTable;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "MessagesAPI", value = "/MessagesAPI")
public class MessagesAPI extends HttpServlet {
    private void createResponse(HttpServletResponse response, int statuscode, String data) {
        response.setStatus(statuscode);

        PrintWriter respwr = null;
        try {
            respwr = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        respwr.write(data);
        response.setContentType("application/text");
        response.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_id = (String) request.getParameter("user_id");
        String doctor_id = (String) request.getParameter("doctor_id");
        EditMessageTable messageTable = new EditMessageTable();
        JSONObject jsonmessages = new JSONObject();

        if(user_id==null || doctor_id==null){
            createResponse(response,403,"Please specify user_id and doctor_id");
        }

        try {
            jsonmessages = messageTable.chatToJSON(Integer.parseInt(user_id), Integer.parseInt(doctor_id));
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }
        createResponse(response,200, jsonmessages.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);
        EditMessageTable messageTable = new EditMessageTable();

        try {
            messageTable.addMessageFromJSON(jsonin.toString());
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }
        createResponse(response,200,"");

    }
}
