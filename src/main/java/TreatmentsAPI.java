import Database_HY359.src.database.tables.EditBloodTestTable;
import Database_HY359.src.database.tables.EditTreatmentTable;
import Database_HY359.src.mainClasses.BloodTest;
import Database_HY359.src.mainClasses.Treatment;
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

@WebServlet(name = "TreatmentsAPI", value = "/TreatmentsAPI")
public class TreatmentsAPI extends HttpServlet {
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
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditTreatmentTable treatable = new EditTreatmentTable();
        JSONObject jsonreply = new JSONObject();

        String user_id = request.getParameter("user_id");
        String bloodtest_id = request.getParameter("bloodtest_id");

        try {
            if(user_id!=null){
                jsonreply = treatable.databaseToJSON(Integer.parseInt(user_id));
            }else if(bloodtest_id!=null){
                jsonreply = treatable.databaseToJSONfromTestID(Integer.parseInt(bloodtest_id));
            }
            createResponse(response,200,jsonreply.toString());
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);
        EditTreatmentTable treatmenttable = new EditTreatmentTable();

        String start_date = (String) jsonin.get("start_date");
        String end_date = (String) jsonin.get("end_date");
        int end = Integer.parseInt(start_date.split("-")[0]+start_date.split("-")[1]+start_date.split("-")[2]);
        int start = Integer.parseInt(end_date.split("-")[0]+end_date.split("-")[1]+end_date.split("-")[2]);
        if(start > end) {
            createResponse(response, 403, "End date can't be after the starting date");
        }

        try {
            treatmenttable.addTreatmentFromJSON(jsonin.toString());
            createResponse(response,200,"");
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
        }

    }
}
