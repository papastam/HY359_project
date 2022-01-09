import Database_HY359.src.database.tables.EditRandevouzTable;
import Database_HY359.src.database.tables.EditSimpleUserTable;
import Database_HY359.src.mainClasses.Randevouz;
import Database_HY359.src.mainClasses.SimpleUser;
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

@WebServlet(name = "ShowDeleteRendezvous", value = "/ShowDeleteRendezvous")
public class ShowDeleteRendezvous extends HttpServlet {

    private void createResponse(HttpServletResponse response, int statuscode, String data) {
        response.setStatus(statuscode);

        PrintWriter respwr = null;
        try {
            respwr = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(data);
        respwr.write(data);
        response.setContentType("application/text");
        response.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonIn = new JSONObject(tokener);
        JSONObject jsonOut = new JSONObject();

        EditRandevouzTable randevouzTable = new EditRandevouzTable();
        ArrayList<Randevouz> allDocRandezvous = null;
        try {
            System.out.println("doc_id = " + (int) jsonIn.get("doctor_id"));
            allDocRandezvous = randevouzTable.databaseToArrayList((int) jsonIn.get("doctor_id"));
            System.out.println("size of list = " +allDocRandezvous.size());
        }
        catch(ClassNotFoundException | SQLException ex) {
            System.err.println(ex.toString());
        }

        int j = 0;
        SimpleUser user = null;

        EditSimpleUserTable userTable = new EditSimpleUserTable();

        for(int i = 0; i < allDocRandezvous.size(); i++) {
            try {
                user = userTable.getSimpleUserFromID(allDocRandezvous.get(i).getUser_id());
            }
            catch(SQLException | ClassNotFoundException ex) {
                System.err.println(ex.toString());
            }
            JSONObject  randezvous = new JSONObject();
            String name = user.getFirstname() + " " +user.getLastname();
            randezvous.put("date_time", allDocRandezvous.get(i).getDate_time());
            randezvous.put("doctor_info", allDocRandezvous.get(i).getDoctor_info());
            randezvous.put("price", allDocRandezvous.get(i).getPrice());
            randezvous.put("status", allDocRandezvous.get(i).getStatus());
            randezvous.put("name", name);
            jsonOut.put(String.valueOf(j), randezvous);
            jsonOut.put("size", j);
            j++;
        }

        createResponse(response, 200, jsonOut.toString());
    }
}
