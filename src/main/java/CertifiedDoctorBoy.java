import Database_HY359.src.database.tables.EditDoctorTable;
import Database_HY359.src.mainClasses.Doctor;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditDoctorTable doctable = new EditDoctorTable();
        ArrayList<Doctor> docs=null;
        JSONObject jsonreply = new JSONObject();

        try {
            docs = doctable.databaseToDoctors();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for(Doctor doc : docs){
            jsonreply.append("doctors",doctable.doctorToJSON(doc));
        }

        createResponse(response,200,jsonreply.toString());
    }

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
}
