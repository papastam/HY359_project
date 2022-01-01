import Database_HY359.src.database.tables.EditDoctorTable;
import Database_HY359.src.mainClasses.Doctor;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditDoctorTable doctable = new EditDoctorTable();
        JSONObject jsonreply = new JSONObject();
//        jsonreply.put("doctors","");

        ArrayList<Doctor> docs = null;
        try {
            docs = doctable.databaseToDoctors();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for(int i=0;i<docs.size();i++){
            if(docs.get(i).getCertified()==1){
                jsonreply.put(String.valueOf(i),doctable.doctorToJSON(docs.get(i)));
            }
        }

        createResponse(response,200,jsonreply.toString());
    }
}
