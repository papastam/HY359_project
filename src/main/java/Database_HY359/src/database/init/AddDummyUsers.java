package Database_HY359.src.database.init;

import Database_HY359.src.database.tables.EditSimpleUserTable;
import org.json.JSONObject;

import java.sql.SQLException;

public class AddDummyUsers {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        EditSimpleUserTable userTable = new EditSimpleUserTable();

        JSONObject jsonuser = new JSONObject("{" +
                "  \"username\":\"DummyUser\"," +
                "  \"email\":\"dummyuser@gmail.com\"," +
                "  \"password\":\"Dummy1234\"," +
                "  \"cpassword\":\"Dummy1234\"," +
                "  \"usertype\":\"user\"," +
                "  \"specialty\":\"internist\"," +
                "  \"doctor_info\":\"\"," +
                "  \"firstname\":\"Dummy\"," +
                "  \"lastname\":\"User\"," +
                "  \"birthdate\":\"1980-01-01\"," +
                "  \"gender\":\"Male\"," +
                "  \"amka\":\"01018010101\"," +
                "  \"country\":\"Greece\"," +
                "  \"city\":\"Chania\"," +
                "  \"address\":\"Agora\"," +
                "  \"addrnumber\":\"\"," +
                "  \"telephone\":\"6969696969\"," +
                "  \"height\":\"180\"," +
                "  \"weight\":\"180\"," +
                "  \"blooddonor\":\"1\"," +
                "  \"lat\":\"10\"," +
                "  \"lon\":\"10\"," +
                "  \"bloodtype\":\"A-\"," +
                "  \"eulaagree\":\"Agree\"" +
                "}");
        userTable.addSimpleUserFromJSON(jsonuser.toString());
    }
}
