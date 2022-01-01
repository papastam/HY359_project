import Database_HY359.src.database.tables.EditSimpleUserTable;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "DeleteUser", value = "/DeleteUser")
public class DeleteUser extends HttpServlet {
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);
        EditSimpleUserTable userTable = new EditSimpleUserTable();

        try {
            userTable.deleteSimpleUser((String) jsonin.get("username"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            createResponse(response, 403,e.getMessage());
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            createResponse(response, 403,e.getMessage());
            return;
        }

        createResponse(response,200,"");
    }
}
