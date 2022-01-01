import Database_HY359.src.database.tables.EditDoctorTable;
import Database_HY359.src.database.tables.EditSimpleUserTable;
import Database_HY359.src.mainClasses.SimpleUser;
import Database_HY359.src.mainClasses.User;
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

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {
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
    
    private void login(HttpServletRequest request, HttpServletResponse response, User logged_in,int usertype){
        if(usertype==0){return;}
        JSONObject jsonreply = new JSONObject();
        HttpSession session = null;
        session = request.getSession(true);
        jsonreply.put("logged_in", true);
        jsonreply.put("usertype", usertype);
        try {
            session.setAttribute("logged_in", logged_in.getUsername());
            session.setAttribute("password", logged_in.getPassword());
        }catch (NullPointerException e){
            returnfailedlogin(response);
            return;
        }
        createResponse(response,200,jsonreply.toString());
        return;

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int usertype = 0;//0 for unknown,1 for user,2 for doctor,3 for admin
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);

        EditSimpleUserTable usrtable = new EditSimpleUserTable();
        EditDoctorTable doctable = new EditDoctorTable();

        User logged_in = null;
        try {
            logged_in = usrtable.databaseToSimpleUser((String) jsonin.get("username"), (String) jsonin.get("password"));
        } catch (SQLException  e) {
            e.printStackTrace();
//            returnfailedlogin(response);
//            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
//            returnfailedlogin(response);
//            return;
        }

        if(logged_in!=null) {
            usertype = 1;
            if (logged_in.getUsername().equals("admin")) {
                usertype = 3;
            }
            login(request,response,logged_in,usertype);
            return;
        }

        try {
            logged_in = doctable.databaseToDoctor((String) jsonin.get("username"), (String) jsonin.get("password"));
        } catch (SQLException  e) {
            e.printStackTrace();
//            returnfailedlogin(response);
//            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
//            returnfailedlogin(response);
//            return;
        }

        if(logged_in!=null){
            usertype=2;
            login(request,response,logged_in,usertype);
            return;
        }

        returnfailedlogin(response);
    }
}
