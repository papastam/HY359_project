/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database_HY359.src.database.tables;

import Database_HY359.src.mainClasses.JSON_Converter;
import Database_HY359.src.mainClasses.SimpleUser;
import com.google.gson.Gson;
import Database_HY359.src.mainClasses.Randevouz;
import Database_HY359.src.database.DB_Connection;
import org.json.JSONObject;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mike
 */
public class EditRandevouzTable {

   
    public void addRandevouzFromJSON(String json) throws ClassNotFoundException{
         Randevouz r=jsonToRandevouz(json);
         createNewRandevouz(r);
    }
    
    
     public Randevouz databaseToRandevouz(int id) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz WHERE randevouz_id= '" + id + "'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Randevouz bt = gson.fromJson(json, Randevouz.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

//    public ArrayList<Randevouz> getfreeRendezvousByDocID(int doctor_id) throws SQLException, ClassNotFoundException {
//        Connection con = DB_Connection.getConnection();
//        Statement stmt = con.createStatement();
//        ArrayList<Randevouz> users=new ArrayList<Randevouz>();
//        ResultSet rs;
//        try {
//            rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status=free AND doctor_id="+doctor_id );
//            while (rs.next()) {
//                String json = DB_Connection.getResultsToJSON(rs);
//                Gson gson = new Gson();
//                Randevouz rendezvous = gson.fromJson(json, Randevouz.class);
//                users.add(rendezvous);
//            }
//            return users;
//
//        } catch (Exception e) {
//            System.err.println("Got an exception! ");
//            System.err.println(e.getMessage());
//        }
//        return null;
//    }

    public JSONObject getRendezvousByDocID(int doctor_id, int status) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JSONObject jsonret = new JSONObject();
        ResultSet rs = null;
        try {
            switch (status) {
                //0 for all, 1 for free, 2 for selected, 3 for cancelled, 4 for done
                case 0:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE doctor_id = '" +doctor_id +"'" );
                    break;
                case 1:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'free' AND doctor_id = '"+doctor_id +"'");
                    break;
                case 2:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'selected' AND doctor_id = '"+doctor_id +"'");
                    break;
                case 3:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'cancelled' AND doctor_id = '"+doctor_id +"'");
                    break;
                case 4:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'done' AND doctor_id = '"+doctor_id +"'");
                    break;
            }

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                jsonret.append("rendezvous",json);
            }
            return jsonret;

        } catch (Exception e) {
            System.err.println("Got an exception in getRendezvousByDocID()");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Randevouz> getRendezvousByDocIDtoArrayList(int doctor_id, int status) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Randevouz> rend = new ArrayList<Randevouz>();
        ResultSet rs = null;
        try {
            switch (status) {
                //0 for all, 1 for free, 2 for selected, 3 for cancelled, 4 for done
                case 0:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE doctor_id = '" +doctor_id +"'" );
                    break;
                case 1:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'free' AND doctor_id = '"+doctor_id +"'");
                    break;
                case 2:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'selected' AND doctor_id = '"+doctor_id +"'");
                    break;
                case 3:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'cancelled' AND doctor_id = '"+doctor_id +"'");
                    break;
                case 4:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'done' AND doctor_id = '"+doctor_id +"'");
                    break;
            }

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Randevouz rendezvous = gson.fromJson(json, Randevouz.class);
                rend.add(rendezvous);
            }
            return rend;

        } catch (Exception e) {
            System.err.println("Got an exception in getRendezvousByDocID()");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Randevouz> getRendezvousByUserID(int user_id,int status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Randevouz> users=new ArrayList<Randevouz>();
        ResultSet rs = null;
        try {
            switch (status) {
                //0 for all, 1 for free, 2 for selected, 3 for cancelled, 4 for done
                case 0:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE user_id = '" +user_id +"'" );
                    break;
                case 1:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'free' AND user_id = '"+user_id +"'");
                    break;
                case 2:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'selected' AND user_id = '"+user_id +"'");
                    break;
                case 3:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'cancelled' AND user_id = '"+user_id +"'");
                    break;
                case 4:
                    rs = stmt.executeQuery("SELECT * FROM randevouz WHERE status = 'done' AND user_id = '"+user_id +"'");
                    break;
            }
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Randevouz rendezvous = gson.fromJson(json, Randevouz.class);
                users.add(rendezvous);
            }
            return users;

        } catch (Exception e) {
            System.err.println("Got an exception in getfreeRendezvousByUserID()");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Randevouz> getAllRendezvousOfDoc(int doctor_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Randevouz> allrendezvous= new ArrayList<Randevouz>();
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz WHERE doctor_id = '" + doctor_id +"'");

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Randevouz rendezvous = gson.fromJson(json, Randevouz.class);
                allrendezvous.add(rendezvous);
            }
            return allrendezvous;
        }
        catch(Exception ex) {
            System.err.println("Got an exception in getAllRendezvousOfDoc()");
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public JSONObject getAllRendezvousOfUser(int user_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JSONObject jsonret = new JSONObject();
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz WHERE user_id = '" + user_id +"'");

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                jsonret.append("rendezvous",json);
            }
            return jsonret;
        }
        catch(Exception ex) {
            System.err.println("Got an exception in getAllRendezvousOfDoc()");
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public void cancelrendezvous(int randevouzID) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE randevouz SET status='canceled' WHERE randevouz_id = '" + randevouzID + "'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

     public Randevouz jsonToRandevouz(String json) {
        Gson gson = new Gson();
        Randevouz r = gson.fromJson(json, Randevouz.class);
        return r;
    }
     
         
      public String randevouzToJSON(Randevouz r) {
        Gson gson = new Gson();

        String json = gson.toJson(r, Randevouz.class);
        return json;
    }


    public void updateRandevouz(int randevouzID, int userID, String info, String status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE randevouz SET user_id='" + userID + "',status='" + status +"',user_info='" + info + "' WHERE randevouz_id = '" + randevouzID + "'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void deleteRandevouz(int randevouzID) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String deleteQuery = "DELETE FROM randevouz WHERE randevouz_id='" + randevouzID + "'";
        stmt.executeUpdate(deleteQuery);
        stmt.close();
        con.close();
    }



    public void createRandevouzTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE randevouz "
                + "(randevouz_id INTEGER not NULL AUTO_INCREMENT, "
                + " doctor_id INTEGER not NULL, "
                + " user_id INTEGER not NULL, "
                + " date_time TIMESTAMP not NULL, "
                + " price INTEGER  not NULL, "
                + " doctor_info VARCHAR(500),"
                + " user_info VARCHAR(500),"
                + " status VARCHAR(15) not null,"
                + "FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id), "
                + " PRIMARY KEY ( randevouz_id  ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewRandevouz(Randevouz rand) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " randevouz (doctor_id,user_id,date_time,price,doctor_info,user_info,status)"
                    + " VALUES ("
                    + "'" + rand.getDoctor_id() + "',"
                    + "'" + rand.getUser_id() + "',"
                    + "'" + rand.getDate_time() + "',"
                    + "'" + rand.getPrice() + "',"
                    + "'" + rand.getDoctor_info() + "',"
                    + "'" + rand.getUser_info() + "',"
                    + "'" + rand.getStatus() + "'"
                    + ")";
            //stmt.execute(table);

            stmt.executeUpdate(insertQuery);
            System.out.println("# The randevouz was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditRandevouzTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
