import Database_HY359.src.database.tables.EditDoctorTable;
import Database_HY359.src.database.tables.EditSimpleUserTable;
import Database_HY359.src.mainClasses.Doctor;
import Database_HY359.src.mainClasses.SimpleUser;
import Database_HY359.src.mainClasses.User;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "GetInfo", value = "/GetInfo")
public class GetInfo extends HttpServlet {

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
        HttpSession session = request.getSession();
        if(session.getAttribute("logged_in")==null){
            returnfailedlogin(response);
            return;
        }
        String logedin_id = session.getAttribute("logged_in").toString();
        String password = (String) session.getAttribute("password");
        JSONObject jsonreply = null;

        EditSimpleUserTable usrtable = new EditSimpleUserTable();
        User loggedin = null;
        try {
            loggedin = usrtable.databaseToSimpleUser(logedin_id, password);
            if(loggedin!=null) {
                jsonreply = new JSONObject(usrtable.simpleUserToJSON((SimpleUser) loggedin));
                if(loggedin.getUsername().equals("admin")){
                    jsonreply.put("usertype","admin");
                }else{
                    jsonreply.put("usertype","user");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        EditDoctorTable doctable = new EditDoctorTable();
        try {
            loggedin = doctable.databaseToDoctor(logedin_id, password);
            if(loggedin!=null) {
                jsonreply = new JSONObject(doctable.doctorToJSON((Doctor) loggedin));
                jsonreply.put("usertype", "doctor");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(jsonreply==null){returnfailedlogin(response);}
        createResponse(response, 200, jsonreply.toString());
    }
}
