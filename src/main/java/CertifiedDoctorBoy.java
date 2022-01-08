import Database_HY359.src.database.tables.EditDoctorTable;
import Database_HY359.src.database.tables.EditSimpleUserTable;
import Database_HY359.src.mainClasses.Doctor;
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

@WebServlet(name = "CertifiedDoctorBoy", value = "/CertifiedDoctorBoy")
public class CertifiedDoctorBoy extends HttpServlet {
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

    //Get a list of all the doctors
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditDoctorTable doctable = new EditDoctorTable();
        ArrayList<Doctor> docs=null;
        JSONObject jsonreply = new JSONObject();
        String certified = request.getParameter("certified");

        if(certified!=null) {
            try {
                docs = doctable.databaseToDoctors(1);
            } catch (Exception e) {
                e.printStackTrace();
                createResponse(response, 403, e.getMessage());
                return;
            }
        }else {
            try {
                docs = doctable.databaseToDoctors(0);
            } catch (Exception e) {
                e.printStackTrace();
                createResponse(response, 403, e.getMessage());
                return;
            }
        }

        for(Doctor doc : docs){
            jsonreply.append("doctors",doctable.doctorToJSON(doc));
        }

        createResponse(response,200,jsonreply.toString());
    }

    //Update a doctors certification
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);
        EditDoctorTable doctable = new EditDoctorTable();

        try {
            doctable.setDoctorCertification((String) jsonin.get("username"),Integer.parseInt((String) jsonin.get("certification")));
        } catch (SQLException e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }

        createResponse(response,200,"");
    }

    //This method is used to update a user's info from a json input in its request
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] jsonfields = { "blooddonor",
                "country",
                "specialty",
                "firstname",
                "birthdate",
                "address",
                "gender",
                "city",
                "specialty",
                "doctor_info",
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

        EditDoctorTable doctable = new EditDoctorTable();

        for(int i=0;i< jsonfields.length;i++){
            try{
                jsonin.put(jsonfields[i], ServletUtilities.filter((String)jsonin.get(jsonfields[i])));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        try {
            doctable.updateWholeDoctor(doctable.jsonToDoctor(String.valueOf(jsonin)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            createResponse(response, 403, e.getMessage());
            return;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            createResponse(response, 403, e.getMessage());
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            createResponse(response, 403, e.getMessage());
            return;
        }

        createResponse(response,200,"");
    }
}
