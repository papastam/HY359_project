package REST;

import Database_HY359.src.database.tables.EditBloodTestTable;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

//@Path("BT")
public class BloodTestAPI {
    @GET
        @Path("/BloodTestMeasure")
        @Produces({MediaType.APPLICATION_JSON})
        public Response getBloodTests(){
            Response.Status status = Response.Status.OK;

            EditBloodTestTable bdtable = new EditBloodTestTable();
            JSONObject jsonFromDB = null;
            try {
                jsonFromDB = bdtable.databaseToJSON();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return Response.status(status).type("application/json").entity(jsonFromDB).build();
        }



}