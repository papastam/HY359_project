/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package servlets;

//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import Database_HY359.src.mainClasses.*;
import Database_HY359.src.database.tables.*;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.JSONTokener;
import ServletUtilities.ServletUtilities;


/**
 *
 * @author mountant
 */
@WebServlet(name = "Register/*", value="/Register/*")



public class Register extends HttpServlet {
    int counter=0;

    private void createResponse(HttpServletResponse response,int statuscode,String data){
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] jsonfields = { "blooddonor",
                "country",
                "specialty",
                "firstname",
                "birthdate",
                "address",
                "gender",
                "city",
                "usertype",
                "weight",
                "telephone",
                "lastname",
                "amka",
                "password",
                "bloodtype",
                "doctor_info",
                "email",
                "username",
                "height"};

        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);

        JSONObject jsonreply = new JSONObject();

        String usrtype = (String) jsonin.get("usertype");
        jsonreply.put("usertype",usrtype);

        for(int i=0;i< jsonfields.length;i++){
            jsonin.put(jsonfields[i],ServletUtilities.filter((String)jsonin.get(jsonfields[i])));
        }

        if(usrtype.equals("doctor")){
            EditDoctorTable doctable = new EditDoctorTable();

            try {
                doctable.addDoctorFromJSON(String.valueOf(jsonin));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                jsonreply.put("debug","ClassNotFoundException");
                createResponse(response, 403, ServletUtilities.filter(e.getMessage()));
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                jsonreply.put("debug","NumberFormatException");
                createResponse(response,403,"Please make sure all the required fields are filled");
                return;
            } catch (SQLException e){
                e.printStackTrace();
                jsonreply.put("debug","SQLExeption");
                createResponse(response,403,ServletUtilities.filter(e.getMessage()));
                return;
            }
            if(!(jsonreply.has("debug"))){jsonreply.put("debug","success");}
        }else if(usrtype.equals("user")){
            EditSimpleUserTable userTable = new EditSimpleUserTable();

            try {
                userTable.addSimpleUserFromJSON(String.valueOf(jsonin));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                jsonreply.put("debug","ClassNotFoundException");
                createResponse(response, 403, ServletUtilities.filter(e.getMessage()));
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                jsonreply.put("debug", "NumberFormatException");
                createResponse(response, 403, e.getMessage());
                return;
            } catch (SQLException e){
                e.printStackTrace();
                jsonreply.put("debug","SQLExeption");
                createResponse(response,403,ServletUtilities.filter(e.getMessage()));
                return;
            }
            if(!(jsonreply.has("debug"))){jsonreply.put("debug","success");}
        }



        createResponse(response,200,String.valueOf(jsonreply));
    }
}
