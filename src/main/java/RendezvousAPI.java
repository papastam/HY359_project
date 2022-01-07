import Database_HY359.src.database.tables.EditRandevouzTable;
import Database_HY359.src.mainClasses.Randevouz;
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

@WebServlet(name = "RendezvousAPI", value = "/RendezvousAPI")
public class RendezvousAPI extends HttpServlet {
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

    private JSONObject ArrayListToJSON(ArrayList<Randevouz> array){
        JSONObject jsonret = new JSONObject();
        EditRandevouzTable rendtable = new EditRandevouzTable();

        for(int i=0;i< array.size();i++){
            jsonret.append("rendezvous", rendtable.randevouzToJSON(array.get(i)));
        }
        return jsonret;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String doctor_id = (String) request.getParameter("doctor_id");
        String user_id = (String) request.getParameter("user_id");
        EditRandevouzTable rendtable = new EditRandevouzTable();
        ArrayList<Randevouz> rendezvous = new ArrayList<Randevouz>();

        if(doctor_id!=null){
            try {
                rendezvous = rendtable.getfreeRendezvousByDocID(Integer.parseInt(doctor_id));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                createResponse(response,403,e.getMessage());
                return;
            }
        }else if(user_id!=null){
            try {
                rendezvous = rendtable.getfreeRendezvousByUserID(Integer.parseInt(user_id));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                createResponse(response,403,e.getMessage());
                return;
            }
        }else{
            createResponse(response,403,"Please specify docor_id or user_id in the querry part");
            return;
        }
        createResponse(response,200,ArrayListToJSON(rendezvous).toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);
        EditRandevouzTable rendtable = new EditRandevouzTable();

        try {
            rendtable.updateRandevouz(Integer.parseInt((String) jsonin.get("randevouz_id")),(Integer) jsonin.get("user_id"), (String) jsonin.get("user_info"),"selected");
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }

        createResponse(response,200,"");
    }
}
