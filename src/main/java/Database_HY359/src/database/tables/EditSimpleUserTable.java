/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database_HY359.src.database.tables;

import Database_HY359.src.mainClasses.Doctor;
import Database_HY359.src.mainClasses.SimpleUser;
import Database_HY359.src.mainClasses.User;
import com.google.gson.Gson;
import Database_HY359.src.database.DB_Connection;
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
public class EditSimpleUserTable {

    public void addSimpleUserFromJSON(String json) throws ClassNotFoundException, SQLException {
         SimpleUser user=jsonToSimpleUser(json);
         addNewSimpleUser(user);
    }

    public void updateSimpleUserFromJSON(String json) throws ClassNotFoundException, SQLException {
        SimpleUser user=jsonToSimpleUser(json);
        updateWholeSimpleUser(user);
    }

     public SimpleUser jsonToSimpleUser(String json){
         Gson gson = new Gson();

        SimpleUser user = gson.fromJson(json, SimpleUser.class);
        return user;
    }
    
    public String simpleUserToJSON(SimpleUser user){
        Gson gson = new Gson();

        String json = gson.toJson(user, SimpleUser.class);
        return json;
    }
    
    public void updateSimpleUser(String username,double weight) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update="UPDATE users SET weight='"+weight+"' WHERE username = '"+username+"'";
        stmt.executeUpdate(update);
    }
    
    public void printSimpleUserDetails(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND password='"+password+"'");
            while (rs.next()) {
                System.out.println("===Result===");
                DB_Connection.printResults(rs);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    public SimpleUser databaseToSimpleUser(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            SimpleUser user = gson.fromJson(json, SimpleUser.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<SimpleUser> databaseToSimpleUsers() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<SimpleUser> users=new ArrayList<SimpleUser>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                SimpleUser user = gson.fromJson(json, SimpleUser.class);
                users.add(user);
            }
            return users;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String databaseUserToJSON(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


     public void createSimpleUserTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE users "
                + "(user_id INTEGER not NULL AUTO_INCREMENT, "
                + "    username VARCHAR(30) not null unique,"
                + "    email VARCHAR(40) not null unique,	"
                + "    password VARCHAR(32) not null,"
                + "    firstname VARCHAR(20) not null,"
                + "    lastname VARCHAR(30) not null,"
                + "    birthdate DATE not null,"
                + "    gender  VARCHAR (7) not null,"
                + "    amka VARCHAR (11) not null,"
                + "    country VARCHAR(30) not null,"
                + "    city VARCHAR(50) not null,"
                + "    address VARCHAR(50) not null,"
                + "    lat DOUBLE,"
                + "    lon DOUBLE,"
                + "    telephone VARCHAR(14) not null,"
                + "    height INTEGER,"
                + "    weight DOUBLE,"
                + "   blooddonor BOOLEAN,"
                + "   bloodtype VARCHAR(7) not null,"
                + " PRIMARY KEY ( user_id))";
        stmt.execute(query);
        stmt.close();
    }
    
    
    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewSimpleUser(SimpleUser user) throws ClassNotFoundException, SQLException {
        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();

        String insertQuery = "INSERT INTO "
                + " users (username,email,password,firstname,lastname,birthdate,gender,amka,country,city,address,"
                + "lat,lon,telephone,height,weight,blooddonor,bloodtype)"
                + " VALUES ("
                + "'" + user.getUsername() + "',"
                + "'" + user.getEmail() + "',"
                + "'" + user.getPassword() + "',"
                + "'" + user.getFirstname() + "',"
                + "'" + user.getLastname() + "',"
                + "'" + user.getBirthdate() + "',"
                + "'" + user.getGender() + "',"
                + "'" + user.getAmka() + "',"
                + "'" + user.getCountry() + "',"
                + "'" + user.getCity() + "',"
                + "'" + user.getAddress() + "',"
                + "'" + user.getLat() + "',"
                + "'" + user.getLon() + "',"
                + "'" + user.getTelephone() + "',"
                + "'" + user.getHeight() + "',"
                + "'" + user.getWeight() + "',"
                + "'" + user.isBloodDonor() + "',"
                + "'" + user.getBloodtype() + "'"
                + ")";
        //stmt.execute(table);
        System.out.println(insertQuery);
        stmt.executeUpdate(insertQuery);
        System.out.println("# The user was successfully added in the database.");

        /* Get the member id from the database and set it to the member */
        stmt.close();

    }

    /**
     * Establish a database connection and edit a user from the database.
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void updateWholeSimpleUser(SimpleUser user) throws ClassNotFoundException, SQLException {
        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();
//            + " users (username,email,password,firstname,lastname,birthdate,gender,amka,country,city,address,"
//                    + "lat,lon,telephone,height,weight,blooddonor,bloodtype)"
//                    + " VALUES ("
//            String update="UPDATE users SET weight='"+weight+"' WHERE username = '"+username+"'";
        String updateQuery = "UPDATE users SET "
//                    + "'" + user.getUsername() + "',"
                + "email='" + user.getEmail() + "',"
                + "password='" + user.getPassword() + "',"
                + "firstname='" + user.getFirstname() + "',"
                + "lastname='" + user.getLastname() + "',"
                + "birthdate='" + user.getBirthdate() + "',"
                + "gender='" + user.getGender() + "',"
//                    + "'" + user.getAmka() + "',"
                + "country='" + user.getCountry() + "',"
                + "city='" + user.getCity() + "',"
                + "address='" + user.getAddress() + "',"
                + "lat='" + user.getLat() + "',"
                + "lon='" + user.getLon() + "',"
                + "telephone='" + user.getTelephone() + "',"
                + "height='" + user.getHeight() + "',"
                + "weight='" + user.getWeight() + "',"
                + "blooddonor='" + user.isBloodDonor() + "',"
                + "bloodtype='" + user.getBloodtype() + "' "
                + "WHERE username = '"+user.getUsername()+"'";
        //stmt.execute(table);
        System.out.println(updateQuery);
        stmt.executeUpdate(updateQuery);
        System.out.println("# The user has succesfully updated his data.");

        /* Get the member id from the database and set it to the member */
        stmt.close();
    }

    public void deleteSimpleUser(String username) throws ClassNotFoundException, SQLException {
        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();
        String updateQuery = "DELETE FROM users WHERE username = '"+username+"'";
        //stmt.execute(table);
        System.out.println(updateQuery);
        stmt.executeUpdate(updateQuery);
        System.out.println("# The user has been sucesfully deleted.");

        /* Get the member id from the database and set it to the member */
        stmt.close();
    }
}
