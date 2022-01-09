import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import Database_HY359.src.database.tables.*;
import Database_HY359.src.mainClasses.Doctor;
import Database_HY359.src.mainClasses.SimpleUser;
import org.json.JSONObject;
import org.json.JSONTokener;

@WebServlet(name = "TestForDuplicates", value = "/TestForDuplicates")
public class TestForDuplicates extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonrequest = new JSONObject(tokener);
        JSONObject jsonreply = new JSONObject();
//        JSONObject jsonreply = new JSONObject("{\"usernameexists\":0, \"amkaexists\":0, \"emailexists\":0}");

        EditDoctorTable doctable = new EditDoctorTable();
        ArrayList<Doctor> doctors = null;
        try {
            doctors = doctable.databaseToArrayList(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //databaseToSimpleUsersArrayList

        EditSimpleUserTable usrtable = new EditSimpleUserTable();
        ArrayList<SimpleUser> users = null;
        try {
            users = usrtable.databaseToSimpleUsersArrayList();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(jsonrequest.has("username")){
            jsonreply.put("usernameexists",0);
        }
        if(jsonrequest.has("email")){
            jsonreply.put("emailexists",0);
        }
        if(jsonrequest.has("amka")){
            jsonreply.put("amkaexists",0);
        }

        for(int i=0;i< doctors.size();i++){
            if(jsonrequest.has("username")){
                if(doctors.get(i).getUsername().equals(jsonrequest.get("username"))){
                    if(jsonrequest.has("excludeusername")){
                        continue;
                    }
                    jsonreply.put("usernameexists",1);
                }
            }
            if(jsonrequest.has("email")){
                if(doctors.get(i).getEmail().equals(jsonrequest.get("email"))){
                    jsonreply.put("emailexists",1);
                }
            }
            if(jsonrequest.has("amka")){
                if(doctors.get(i).getAmka().equals(jsonrequest.get("amka"))){
                    jsonreply.put("amkaexists",1);
                }
            }
        }

        for(int i=0;i < users.size();i++){
            if(jsonrequest.has("username")){
                if(users.get(i).getUsername().equals(jsonrequest.get("username"))){
                    if(jsonrequest.has("excludeusername")){
                        continue;
                    }
                    jsonreply.put("usernameexists",1);
                }
            }
            if(jsonrequest.has("email")){
                if(users.get(i).getEmail().equals(jsonrequest.get("email"))){
                    jsonreply.put("emailexists",1);
                }
            }
            if(jsonrequest.has("amka")){
                if(users.get(i).getAmka().equals(jsonrequest.get("amka"))){
                    jsonreply.put("amkaexists",1);
                }
            }
        }

        response.setStatus(200);
        PrintWriter respwr = response.getWriter();
        respwr.write(String.valueOf(jsonreply));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

    }
}
