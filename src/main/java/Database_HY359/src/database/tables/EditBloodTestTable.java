/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database_HY359.src.database.tables;

import Database_HY359.src.mainClasses.BloodTest;
import Database_HY359.src.mainClasses.Doctor;
import Database_HY359.src.mainClasses.SimpleUser;
import com.google.gson.Gson;
import Database_HY359.src.database.DB_Connection;
import org.json.JSONObject;

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
public class EditBloodTestTable {

   
    
    public void addBloodTestFromJSON(String json) throws ClassNotFoundException{
         BloodTest bt=jsonToBloodTest(json);
         bt.setValues();
         createNewBloodTest(bt);
    }
    
     public BloodTest jsonToBloodTest(String json){
        Gson gson = new Gson();
        BloodTest btest = gson.fromJson(json, BloodTest.class);
        return btest;
    }
    
     public String bloodTestToJSON(BloodTest bt){
         Gson gson = new Gson();

        String json = gson.toJson(bt, BloodTest.class);
        return json;
    }

    public ArrayList<BloodTest> databaseToArrayList() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<BloodTest> tests=new ArrayList<BloodTest>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bloodtest");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BloodTest test = gson.fromJson(json, BloodTest.class);
                tests.add(test);
            }
            return tests;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public JSONObject databaseToJSON(String amka) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JSONObject jsonret = new JSONObject();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bloodtest WHERE amka='"+amka+"'");
            while (rs.next()) {
                JSONObject json = new JSONObject(DB_Connection.getResultsToJSON(rs));

                EditSimpleUserTable usertable = new EditSimpleUserTable();
                SimpleUser user = usertable.getSimpleUserFromAMKA(amka);
                json.put("name",user.getFirstname()+" "+user.getLastname());
                json.put("user_id",user.getUser_id());

                jsonret.append("tests",json.toString());
            }
            return jsonret;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public JSONObject databaseToJSON() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        int i=0;
        JSONObject jsonBloodTests = new JSONObject();
        ArrayList<BloodTest> bloodtests=new ArrayList<BloodTest>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bloodtest");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BloodTest bt = gson.fromJson(json, BloodTest.class);
                jsonBloodTests.put(String.valueOf(i),bt);
            }
            return jsonBloodTests;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
     public BloodTest databaseToBloodTest(String amka,String date) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bloodtest WHERE amka= '" + amka + "' AND test_date='"+date+"'");
            if(rs.next()==false){return null;}
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            BloodTest bt = gson.fromJson(json, BloodTest.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
       public void updateBloodTest(int id,double chol) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        BloodTest bt=new BloodTest();
        bt.setCholesterol(chol);
        bt.setCholesterol_level();
        String update="UPDATE bloodtest SET cholesterol='"+chol+"', cholesterol_level='"+bt.getCholesterol_level()+"' WHERE bloodtest_id = '"+id+"'";
        stmt.executeUpdate(update);
    }
  
       
       public void deleteBloodTest(int bloodtestid) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String deleteQuery = "DELETE FROM bloodtest WHERE bloodtest_id='" + bloodtestid + "'";
        stmt.executeUpdate(deleteQuery);
        stmt.close();
        con.close();
    }


    public void createBloodTestTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE bloodtest "
                + "(bloodtest_id INTEGER not NULL AUTO_INCREMENT, "
                + "amka VARCHAR (11) not null,"
                + "test_date DATE not NULL, "
                + "medical_center VARCHAR(100) not NULL, "
                + "blood_sugar DOUBLE, "
                + "blood_sugar_level VARCHAR(10),"
                + "cholesterol DOUBLE, "
                + "cholesterol_level VARCHAR(10)," 
                + "iron DOUBLE, "
                + "iron_level VARCHAR(10)," 
                + "vitamin_d3 DOUBLE, "
                + "vitamin_d3_level VARCHAR(10)," 
                + "vitamin_b12 DOUBLE, "
                + "vitamin_b12_level VARCHAR(10)," 
                + "PRIMARY KEY ( bloodtest_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewBloodTest(BloodTest bt) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " bloodtest (amka,test_date,medical_center,blood_sugar,blood_sugar_level,cholesterol,"
                    + "cholesterol_level,iron,iron_level,vitamin_d3,vitamin_d3_level,vitamin_b12,vitamin_b12_level) "
                    + " VALUES ("
                    + "'" + bt.getAmka()+ "',"
                    + "'" + bt.getTest_date() + "',"
                    + "'" + bt.getMedical_center() + "',"
                    + "'" + bt.getBlood_sugar() + "',"
                    + "'" + bt.getBlood_sugar_level() + "',"
                    + "'" + bt.getCholesterol() + "',"
                    + "'" + bt.getCholesterol_level() + "',"
                    + "'" + bt.getIron() + "',"
                    + "'" + bt.getIron_level() + "',"
                    + "'" + bt.getVitamin_d3() + "',"
                    + "'" + bt.getVitamin_d3_level() + "',"
                    + "'" + bt.getVitamin_b12()+ "',"
                    + "'" + bt.getVitamin_b12_level()+ "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The bloodtest was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditBloodTestTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
