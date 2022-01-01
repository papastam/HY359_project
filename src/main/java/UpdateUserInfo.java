import Database_HY359.src.database.tables.EditDoctorTable;
import Database_HY359.src.database.tables.EditSimpleUserTable;
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

@WebServlet(name = "UpdateUserInfo", value = "/UpdateUserInfo")
public class UpdateUserInfo extends HttpServlet {
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
        String[] jsonfields = { "blooddonor",
                                "country",
                                "specialty",
                                "firstname",
                                "birthdate",
                                "address",
                                "gender",
                                "city",
                                "usertype",
                                "weight",
                                "telephone",
                                "lastname",
                                "amka",
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
}