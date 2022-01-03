import Database_HY359.src.database.tables.EditBloodTestTable;
import Database_HY359.src.database.tables.EditTreatmentTable;
import Database_HY359.src.mainClasses.BloodTest;
import Database_HY359.src.mainClasses.Treatment;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
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
        ArrayList<Treatment> treatments = new ArrayList<>();
        JSONObject jsonreply = new JSONObject();

        int user_id = Integer.parseInt(request.getParameter("user_id"));

        try {
            treatments = treatable.databaseToTreatments(user_id);
        } catch (SQLException e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }

        for(int i=0;i<treatments.size();i++){
            jsonreply.append("treatments",treatable.treatmentToJSON(treatments.get(i)));
        }

        createResponse(response,200,jsonreply.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
