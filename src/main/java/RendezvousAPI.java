import Database_HY359.src.database.tables.EditDoctorTable;
import Database_HY359.src.database.tables.EditRandevouzTable;
import Database_HY359.src.mainClasses.Doctor;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String doctor_id = (String) request.getParameter("doctor_id");
        String user_id = (String) request.getParameter("user_id");
        EditRandevouzTable rendtable = new EditRandevouzTable();
        JSONObject jsonreply = new JSONObject();

        if(doctor_id!=null){
            try {
                jsonreply = rendtable.getRendezvousByDocID(Integer.parseInt(doctor_id),1);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                createResponse(response,403,e.getMessage());
                return;
            }
        }else if(user_id!=null){
            try {
                jsonreply = rendtable.getAllRendezvousOfUser(Integer.parseInt(user_id));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                createResponse(response,403,e.getMessage());
                return;
            }
        }else{
            createResponse(response,403,"Please specify docor_id or user_id in the querry part");
            return;
        }
        createResponse(response,200,jsonreply.toString());
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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonIn = new JSONObject(tokener);

        EditDoctorTable doctorTable = new EditDoctorTable();
        EditRandevouzTable randevouzTable = new EditRandevouzTable();
        ArrayList<Randevouz> rendezvous = null;
        JSONObject jsonOut = new JSONObject();

        if((int) jsonIn.get("certified") == 0) {
            jsonOut.put("reply", -1);
            createResponse(response, 403, jsonOut.toString());
            return;
        }

        try {
            Doctor doctor = doctorTable.databaseToDoctor((String) jsonIn.get("username"), (String) jsonIn.get("password"));
            System.out.println("doctor id: "+doctor.getDoctor_id());
            jsonIn.put("doctor_id", doctor.getDoctor_id());
            jsonIn.put("status", "free");
            jsonIn.remove("username");
            jsonIn.remove("password");
            jsonIn.remove("certified");
            rendezvous = randevouzTable.getRendezvousByDocIDtoArrayList(doctor.getDoctor_id(), 0);
        }
        catch(ClassNotFoundException ex){
            System.out.println(ex.toString());
        }
        catch(SQLException ex){
            System.out.println(ex.toString());
        }

        if(checkValidity((String) jsonIn.get("date_time"), rendezvous, response)) {
            jsonOut = new JSONObject();
            jsonOut.put("reply", 0); //0 means new rendezvous is good to make

            Randevouz randevouz = randevouzTable.jsonToRandevouz(jsonIn.toString());
            try {
                randevouzTable.createNewRandevouz(randevouz);
            }
            catch(ClassNotFoundException ex){
                System.out.println(ex.toString());
            }
            createResponse(response, 200, "");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonIn = new JSONObject(tokener);

        EditRandevouzTable rendtable = new EditRandevouzTable();
        try {
            rendtable.cancelrendezvous(Integer.parseInt((String) jsonIn.get("rend_id")));
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response,403,e.getMessage());
            return;
        }
        createResponse(response,200,"");
    }

    private boolean checkValidity(String datetime, ArrayList<Randevouz> rendezvous, HttpServletResponse response) {
        JSONObject jsonOut = new JSONObject();
        String[] date_;
        String[] time_;
        String[] split = datetime.split("T");

        date_ = split[0].split("-");
        time_ = split[1].split(":");

        String date = date_[0] + date_[1] + date_[2];
        String time = time_[0] + time_[1];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        String currentDatetime = formatter.format(now);
        String[] date_time = currentDatetime.split(" ");
        String currDate = date_time[0].split("/")[0] + date_time[0].split("/")[1] + date_time[0].split("/")[2];

        // If new rendezvous date is before current date
        if(Integer.parseInt(date) <= Integer.parseInt(currDate)) {
            System.out.println("problem with date");
            jsonOut.put("reply", 0); // 0 means new rendezvous date id before current date
            createResponse(response, 403, jsonOut.toString());
            return false;
        }
        //Check if time is between 8:00 and 20:30
        if(Integer.parseInt(time) < 800 || Integer.parseInt(time) > 2030) {
            System.out.println("Time is before 8:00 or after 20:30");
            jsonOut.put("reply", 2); //2 means time is before 8:00 or after 20:30
            createResponse(response, 403, jsonOut.toString());
            return false;
        }


        String[] rendezvousdatetime;
        String rendezvousDate;
        String rendezvousTime;
        String renDateFinal;
        String renTimeFinal;
        //Check for times. Rendezvous last 30 minutes
        for(int i = 0; i < rendezvous.size(); i++) {
            rendezvousdatetime = rendezvous.get(i).getDate_time().split(" ");
            rendezvousDate = rendezvousdatetime[0];
            rendezvousTime = rendezvousdatetime[1];
            renDateFinal = rendezvousDate.split("-")[0] + rendezvousDate.split("-")[1] + rendezvousDate.split("-")[2];
            renTimeFinal = rendezvousTime.split(":")[0] + rendezvousTime.split(":")[1];

            //If an existing rendezvous date is the same date as the new rendezvous
            if(Integer.parseInt(renDateFinal) == Integer.parseInt(date)) {
                //Check if the two rendezvous's have a gap of 30 minutes
                int difference = Integer.parseInt(time) - Integer.parseInt(renTimeFinal);
                if( difference < 30 && difference > -30) {
                    System.out.println("problem with time");
                    jsonOut.put("reply", 1); // 1 means two rendezvous on same date dont have 30 minutes time difference
                    createResponse(response, 403, jsonOut.toString());
                    return false;
                }
            }
        }

        return true;
    }
}
