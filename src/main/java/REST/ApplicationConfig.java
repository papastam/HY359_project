package REST;

import Database_HY359.src.database.tables.EditBloodTestTable;
import Database_HY359.src.mainClasses.BloodTest;

import javax.ws.rs.core.Application;
import java.sql.SQLException;
import java.util.*;

@javax.ws.rs.ApplicationPath("BloodTestAPI")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(REST.BloodTestAPI.class);
    }
//    @Override
//    public Set<Object> getSingletons() {
//        EditBloodTestTable bloodtests = new EditBloodTestTable();
//        try {
//            List tests = bloodtests.databaseToDoctors();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        Set set = new HashSet((Collection) bloodtests);
//        set.add(new BloodTestAPI());
//        return set;
//    }
}
