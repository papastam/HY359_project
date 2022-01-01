import Database_HY359.src.database.tables.EditSimpleUserTable;
import Database_HY359.src.mainClasses.SimpleUser;
import org.json.JSONObject;
import org.json.JSONTokener;
//import sun.java2d.pipe.SpanShapeRenderer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "UserLogin", value = "/UserLogin")
public class UserLogin extends HttpServlet {
    private void returnfailedlogin(HttpServletResponse response) {
        JSONObject jsonreply = new JSONObject();
        jsonreply.put("loged_in", false);
        jsonreply.put("message", "Username or Password is incorrect!");
        createResponse(response,403, jsonreply.toString());

    }


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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);

        JSONObject jsonreply = new JSONObject();
        EditSimpleUserTable usrtable = new EditSimpleUserTable();
        HttpSession session = null;

        SimpleUser logged_in = null;
        try {
            logged_in = usrtable.databaseToSimpleUser((String) jsonin.get("username"), (String) jsonin.get("password"));
        } catch (SQLException  e) {
            e.printStackTrace();
            returnfailedlogin(response);
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            returnfailedlogin(response);
            return;
        }

        session = request.getSession(true);
        jsonreply.put("logged_in", true);
        try {
            session.setAttribute("logged_in", logged_in.getUsername());
            session.setAttribute("password", logged_in.getPassword());
        }catch (NullPointerException e){
            returnfailedlogin(response);
            return;
        }
        createResponse(response,200,jsonreply.toString());
    }
}
