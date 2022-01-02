import Database_HY359.src.database.tables.EditBloodTestTable;
import Database_HY359.src.mainClasses.BloodTest;
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

@WebServlet(name = "BloodTestsAPI", value = "/BloodTestsAPI")
public class BloodTestsAPI extends HttpServlet {
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
        EditBloodTestTable bloodtests = new EditBloodTestTable();
        ArrayList<BloodTest> tests = new ArrayList<>();
        JSONObject jsonreply = new JSONObject();

        String amka = request.getParameter("amka");

        try {
            tests = bloodtests.databaseToBloodTests(Long.parseLong(amka));
        } catch (SQLException e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }

        for(int i=0;i<tests.size();i++){
            jsonreply.append("tests",bloodtests.bloodTestToJSON(tests.get(i)));
        }

        createResponse(response,200,jsonreply.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);
        EditBloodTestTable bloodtest = new EditBloodTestTable();

        try {
            bloodtest.addBloodTestFromJSON(String.valueOf(jsonin));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }

        createResponse(response,200,"");
    }
}
