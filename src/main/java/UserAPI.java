import Database_HY359.src.database.tables.EditRandevouzTable;
import Database_HY359.src.database.tables.EditSimpleUserTable;
import Database_HY359.src.mainClasses.Randevouz;
import Database_HY359.src.mainClasses.SimpleUser;
import ServletUtilities.ServletUtilities;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@WebServlet(name = "UserAPI", value = "/UserAPI")
public class UserAPI extends HttpServlet {
    private void createResponse(HttpServletResponse response,int statuscode,String data){
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

    //This method is used to get a users info
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditSimpleUserTable usertable = new EditSimpleUserTable();
        JSONObject jsonreply = new JSONObject();
        String type = request.getParameter("type");

        try {
            if (type == null) {
                createResponse(response, 200, usertable.databaseToJSONnotadmin().toString());
            }else if(type.equals("patient")){
                String doctor_id = request.getParameter("doctor_id");
                if(doctor_id==null){createResponse(response,403,"Please specify doctor_id to get patients back");}

                JSONObject jsonpatients = new JSONObject();
                ArrayList<Randevouz> doctorsrends = new ArrayList<>();
                EditRandevouzTable randtable = new EditRandevouzTable();
                EditSimpleUserTable usrtable = new EditSimpleUserTable();
                doctorsrends = randtable.databaseToArrayList(Integer.parseInt(doctor_id),4);

                HashSet<Integer> patientsSet = new HashSet<>();
                for(Randevouz rand: doctorsrends){
                    patientsSet.add(rand.getUser_id());
                }

                for(int user_id: patientsSet){
                    jsonpatients.append("patients",usertable.simpleUserToJSON(usertable.getSimpleUserFromID(user_id)));
                }

                createResponse(response,200,jsonpatients.toString());
            }else if(type.equals("user")){
                jsonreply = usertable.databaseToJSON();
                createResponse(response, 200, jsonreply.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }
    }

    //This method is used to update a user's info from a json input in its request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] jsonfields = { "blooddonor",
                "country",
                "specialty",
                "firstname",
                "birthdate",
                "address",
                "gender",
                "city",
                "weight",
                "telephone",
                "lastname",
                "password",
                "bloodtype",
                "doctor_info",
                "email",
                "username",
                "height"};

        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);

        JSONObject jsonreply = new JSONObject();

        EditSimpleUserTable userTable = new EditSimpleUserTable();

        for(int i=0;i< jsonfields.length;i++){
            try{
                jsonin.put(jsonfields[i], ServletUtilities.filter((String)jsonin.get(jsonfields[i])));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        try {
            userTable.updateSimpleUserFromJSON(String.valueOf(jsonin));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            jsonreply.put("debug","ClassNotFoundException");
            createResponse(response, 403, e.getMessage());
            return;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            jsonreply.put("debug", "NumberFormatException");
            createResponse(response, 403, e.getMessage());
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            jsonreply.put("debug", "SQLException");
            createResponse(response, 403, e.getMessage());
            return;
        }
        if(!(jsonreply.has("debug"))){jsonreply.put("debug","success");}

        createResponse(response,200,String.valueOf(jsonreply));
    }

    //This method is used to delete a specific user
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);
        EditSimpleUserTable userTable = new EditSimpleUserTable();

        try {
            userTable.deleteSimpleUser((String) jsonin.get("username"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            createResponse(response, 403,e.getMessage());
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            createResponse(response, 403,e.getMessage());
            return;
        }

        createResponse(response,200,"");
    }
}
