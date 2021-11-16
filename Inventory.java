import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.LinkedList;
//import Vehicle;

// usernames = inventoryview & inventoryadd
// I need to convert this class to a Singleton (make constructor private and add a "getInvetory()" method which will
// create the single Inventory object (or return it if already created)

public class Inventory {
    private static Inventory inventory;
    String url;
    Connection conn;
    Statement stmt;
    ResultSet results;

    public static void main(String[] args){
        Inventory inv = Inventory.getInventory();
        LinkedList<Vehicle> searchResults = inv.getAllVehicles();
        for (Vehicle v : searchResults) {
            System.out.println(v);
        }
        Vehicle insertVehicle = searchResults.get(1);
        insertVehicle.vin = "1234567890125";
        inv.addCar(insertVehicle);

        inv.updateCar("1234567890123", "type", "Sedan");
        inv.updateCar("1234567890124", "price", 30000);

        String searchField = "Model";
        String searchCriteria = "Ford";
        searchResults = inv.getFilteredVehicles(searchField, searchCriteria);
        for (Vehicle v : searchResults) {
            System.out.println(v);
        }
    }

    public static Inventory getInventory() {
        if (inventory == null) {
            inventory = new Inventory();
        }
        return inventory;
    }

    private Inventory() {
        url = "jdbc:mysql://localhost:3306/vehicle_inventory";
        conn = null;
        stmt = null;
        results = null;
    }

    public LinkedList<Vehicle> getAllVehicles() {
        String sql = "SELECT * FROM vehicle_inventory.vehicle;";
        return vehicleQuery(sql);
    }

    public LinkedList<Vehicle> getFilteredVehicles(String criteria, String field) {
        String sql = "SELECT * FROM vehicle_inventory.vehicle WHERE " + criteria + "=\"" + field +"\";";
        return vehicleQuery(sql);
    }

    public boolean checkUser(String username, String password) {
        String sql = "SELECT user_id FROM vehicle_inventory.users WHERE user_name = \"" + username + "\" and password = \""
                + password + "\" and active = 1;";
        executeQuery(sql);
        boolean valid = false;
        try {
            valid = results.next();
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return valid;
    }

    public boolean addUser(String username, String password, boolean admin) {
        boolean inserted = false;
        String sql = "SELECT user_name, admin, password, active FROM vehicle_inventory.users where user_name = \"" + username + "\";";
        executeQuery(sql);
        try {
            if (results.next()) {
                try { results.close(); } catch (SQLException ex) {}
                try { stmt.close(); } catch (SQLException ex) {}
                try { conn.close(); } catch (SQLException ex) {}
                throw new InvalidParameterException("Username already exists.");
            }
            results.moveToInsertRow();
            results.updateString(1, username);
            results.updateInt(2, admin ? 1 : 0);
            results.updateString(3, password);
            results.updateInt(4, 1);
            results.insertRow();
            conn.commit();
            inserted = true;
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return inserted;
    }

    public boolean deactivateUser(String username) {
        boolean updated = false;
        String sql = "SELECT user_id, user_name, active FROM vehicle_inventory.users where user_name = \"" + username + "\";";
        executeQuery(sql);
        try {
            while (results.next()) {
                results.updateInt(2, 0);
                results.updateRow();
                conn.commit();
            }
            updated = true;
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return updated;
    }

    public boolean activateUser(String username) {
        boolean updated = false;
        String sql = "SELECT user_id, user_name, active FROM vehicle_inventory.users;";
        executeQuery(sql);
        try {
            while (results.next()) {
                results.updateInt(2, 1);
                results.updateRow();
                conn.commit();
            }
            updated = true;
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return updated;
    }

    public boolean isUserAdmin(String username) {
        boolean admin = false;
        String sql = "SELECT user_name, admin FROM vehicle_inventory.users;";
        executeQuery(sql);
        try {
            if (results.next()) {
                admin = (results.getInt(2) > 0);
            }
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return admin;
    }


    public LinkedList<String> listUser() {
        LinkedList<String> users = new LinkedList<>();
        String sql = "SELECT user_name FROM vehicle_inventory.users;";
        executeQuery(sql);
        try {
            if (results.next()) {
                users.add(results.getString(1));
            }
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return users;
    }

    public boolean updateCar(String vin, String field, String value) {
        boolean updated = false;
        String sql = "SELECT vin, " + field + " FROM vehicle_inventory.vehicle WHERE vin = \"" + vin + "\"";
        executeQuery(sql);
        try {
            if (results.next()) {
                results.updateString(2, value);
                results.updateRow();
                conn.commit();
                updated = true;
            }
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) { System.out.println(ex.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return updated;
    }

    public boolean updateCar(String vin, String field, int value) {
        boolean updated = false;
        String sql = "SELECT vin, " + field + " FROM vehicle_inventory.vehicle WHERE vin = \"" + vin + "\"";
        executeQuery(sql);
        try {
            if (results.next()) {
                results.updateInt(2, value);
                results.updateRow();
                conn.commit();
                updated = true;
            }
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) { System.out.println(ex.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return updated;
    }

    public boolean addCar(Vehicle vehicle) {
        boolean inserted = false;
        String sql = "SELECT vin, price, model, make, country, type, year, mileage, features, size, color, engine, " +
                "fuel_economy, fuel_type, location FROM vehicle_inventory.vehicle WHERE vin = \"" + vehicle.vin + "\";";
        executeQuery(sql);
        try {
            if (!results.next()) {
                results.moveToInsertRow();
                results.updateString(1, vehicle.vin);
                results.updateInt(2, vehicle.price);
                results.updateString(3, vehicle.model);
                results.updateString(4, vehicle.make);
                results.updateString(5, vehicle.country);
                results.updateString(6, vehicle.type);
                results.updateInt(7, vehicle.year);
                results.updateInt(8, vehicle.mileage);
                results.updateString(9, vehicle.features);
                results.updateString(10, vehicle.size);
                results.updateString(11, vehicle.color);
                results.updateString(12, vehicle.engine);
                results.updateInt(13, vehicle.fuel_economy);
                results.updateString(14, vehicle.fuel_type);
                results.updateInt(15, vehicle.location);
                results.insertRow();
                conn.commit();
                inserted = true;
            }
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) { System.out.println(ex.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return inserted;
    }

    public boolean addTitleHistory(String vin, String titleHistory) {
        boolean updated = false;
        String sql = "SELECT title_history, vin FROM vehicle_inventory.title_history WHERE vin = \"" + vin + "\";";
        executeQuery(sql);
        try {
            if (results.next()) {
                results.updateString(1, titleHistory);
                results.updateRow();
                conn.commit();
                updated = true;
            } else {
                results.moveToInsertRow();
                results.updateString(1, titleHistory);
                results.updateString(2, vin);
                results.insertRow();
                conn.commit();
                updated = true;
            }
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }

        return updated;
    }

    public String getTitleHistory(String vin) {
        String titleHistory = null;
        String sql = "SELECT title_history FROM vehicle_inventory.title_history WHERE vin = \"" + vin + "\";";
        executeQuery(sql);
        try {
            if (results.next()) {
                titleHistory = results.getString(1);
            }
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return titleHistory;
    }

    private Vehicle createVehicle(ResultSet results) {
        try {
            String vin = results.getString(1);
            int price = results.getInt(2);
            String model = results.getString(3);
            String make = results.getString(4);
            String country = results.getString(5);
            String type = results.getString(6);
            int year = results.getInt(7);
            int mileage = results.getInt(8);
            String features = results.getString(9);
            String size = results.getString(10);
            String color = results.getString(11);
            String engine = results.getString(12);
            int fuel_economy = results.getInt(13);
            String fuel_type = results.getString(14);
            int location = results.getInt(15);
            return new Vehicle(vin, price, model, make, country, type, year, mileage, features, size, color, engine
                    , fuel_economy, fuel_type, location);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void executeQuery(String sql) {
        try {
            conn = DriverManager.getConnection(url, "", "");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            conn.setAutoCommit(false);
            results = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            results = null;
        }
    }

    private LinkedList<Vehicle> vehicleQuery(String sql) {
        LinkedList<Vehicle> vehicleList = new LinkedList<Vehicle>();

        try {
            executeQuery(sql);

            while (results.next()) {
                Vehicle v = createVehicle(results);
                vehicleList.add(v);
            }

            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
            return null;
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        return vehicleList;
    }
}
