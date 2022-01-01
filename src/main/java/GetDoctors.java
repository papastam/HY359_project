import Database_HY359.src.database.tables.EditDoctorTable;
import Database_HY359.src.database.tables.EditSimpleUserTable;
import Database_HY359.src.mainClasses.Doctor;
import Database_HY359.src.mainClasses.SimpleUser;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "GetDoctors", value = "/GetDoctors")
public class GetDoctors extends HttpServlet {
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
}
