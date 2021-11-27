import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.LinkedList;



public class Inventory {
    private static Inventory inventory; // singleton Inventory object
    String url;                         // address to MySQL server
    Connection conn;                    // SQL connection
    Statement stmt;                     // SQL statement
    ResultSet results;                  // SQL result set

    public static void main(String[] args){
        Inventory inv = Inventory.getInventory();
        LinkedList<Vehicle> searchResults = inv.getAllVehicles();
        for (Vehicle v : searchResults) {
            System.out.println(v);
        }


        inv.updateCar("1234567890123", "type", "Sedan");
        inv.updateCar("1234567890124", "price", 30000);

        inv.addUser("defaultUser", "p@$$w0rd1", false);
        inv.addUser("defaultAdmin", "p@$$w0rd2", true);


        String searchField = "Model";
        String searchCriteria = "Ford";
        searchResults = inv.getFilteredVehicles(searchField, searchCriteria);
        for (Vehicle v : searchResults) {
            System.out.println(v);
        }
    }

    /***************************************************************************
     * getInventory
     * Create new Inventory object or return existing Inventory
     * @return Inventory
     **************************************************************************/
    public static Inventory getInventory() {
        if (inventory == null) {  // inventory has not been instantiated
            // create new Inventory
            inventory = new Inventory();
        }
        // return Inventory object
        return inventory;
    }

    /***************************************************************************
     * Inventory
     * Inventory constructor
     **************************************************************************/
    private Inventory() {
        // initialize objects
        url = "jdbc:mysql://localhost:3306/vehicle_inventory";
        conn = null;
        stmt = null;
        results = null;
    }

    /***************************************************************************
     * getAllVehicles
     * Get a LinkedList of all Vehicles available for sale
     * @return  LinkedList<Vehicle> of all available vehicles if successful,
     *              null if unsuccessful
     **************************************************************************/
    public LinkedList<Vehicle> getAllVehicles() {
        // SQL Query
        String sql = "SELECT * FROM vehicle_inventory.vehicle WHERE sold IS " +
                "NULL ORDER BY price ASC;";

        // Execute query and return results
        return vehicleQuery(sql);
    }

    /***************************************************************************
     * getFilteredVehicles
     * Get a LinkedList of all Vehicles available for sale that match the given
     *      search criteria
     * @param criteria  Criteria to search for
     * @param field     Field to be searched
     * @return  LinkedList<Vehicle></Vehicle> of available vehicle matching
     *              criteria if successful, null if unsuccessful
     **************************************************************************/
    public LinkedList<Vehicle> getFilteredVehicles(String criteria
            , String field) {
        // SQL Query
        String sql = "SELECT * FROM vehicle_inventory.vehicle WHERE "
                + criteria + "=\"" + field +"\";";

        // Execute query and return results
        return vehicleQuery(sql);
    }

    /***************************************************************************
     * checkUser
     * Check is username/password combination is valid and user is active and
     *      return User if so. Return null if username or password are invalid
     *      or user is inactive
     * @param username  username to test
     * @param password  password for user
     * @return  User if username/password is valid and user is active, null
     *              otherwise
     **************************************************************************/
    public User checkUser(String username, String password) {
        // SQL Query
        String sql = "SELECT user_id, admin FROM vehicle_inventory.users " +
                "WHERE user_name = \"" + username + "\" and password = \""
                + password + "\" and active = 1;";

        // execute query
        executeQuery(sql);

        // Create & initialize user
        User user = null;

        try {
            if (results.next()) { // username/password correct
                // check if user is admin and create User
                boolean admin = results.getInt(2) > 1;
                user = new User(username, admin);
            }
            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }

        // return User
        return user;
    }

    /***************************************************************************
     * addUser
     * Add a new user to the database
     * @param username  user's username
     * @param password  user's password
     * @param admin     admin status (true = admin)
     * @return  boolean true if add successful, false otherwise
     **************************************************************************/
    public boolean addUser(String username, String password, boolean admin) {
        boolean inserted = false;   // Track whether insert is successful

        // SQL Query
        String sql = "SELECT user_id, user_name, admin, password, active FROM" +
                " vehicle_inventory.users where user_name = \"" + username +
                "\";";

        // execute query
        executeQuery(sql);

        try {
            if (results.next()) {  // User already exists
                try { results.close(); } catch (SQLException ex) {}
                try { stmt.close(); } catch (SQLException ex) {}
                try { conn.close(); } catch (SQLException ex) {}
            } else { // User is new user
                // Insert new user
                results.moveToInsertRow();
                results.updateString(2, username);
                results.updateInt(3, admin ? 1 : 0);
                results.updateString(4, password);
                results.updateInt(5, 1);
                results.insertRow();
                conn.commit();

                // Inserted successfully
                inserted = true;
            }
            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) { System.out.println(ex.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        // Return results of insert
        return inserted;
    }

    /***************************************************************************
     * deactivateUser
     * Mark the given user as deactivated
     * @param username username of user to deactivate
     * @return  boolean true if user marked inactive, false otherwise
     **************************************************************************/
    public boolean deactivateUser(String username) {
        boolean updated = false;    // Track whether update is successful
        // SQL query
        String sql = "SELECT user_id, user_name, active FROM " +
                "vehicle_inventory.users where user_name = \"" + username +
                "\";";

        // execute query
        executeQuery(sql);

        try {
            if (results.next()) {    // User found
                // Update user to inactive
                results.updateInt(2, 0);
                results.updateRow();
                conn.commit();

                // Update successful
                updated = true;
            }
            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        // Return result of update
        return updated;
    }

    /***************************************************************************
     * activateUser
     * Mark the given user as active
     * @param username username of user to activate
     * @return  boolean true if user marked active, false otherwise
     **************************************************************************/
    public boolean activateUser(String username) {
        boolean updated = false;    // Track whether update is successful

        // SQL query
        String sql = "SELECT user_id, user_name, active FROM " +
                "vehicle_inventory.users;";

        // execute query
        executeQuery(sql);

        try {
            if (results.next()) { // user found
                // Update user to active
                results.updateInt(2, 1);
                results.updateRow();
                conn.commit();

                // update successful
                updated = true;
            }
            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }

        // return results of update
        return updated;
    }

    /***************************************************************************
     * listUser
     * Provide a LinkedList of all usernames
     * @return  LinkedList<String> of usernames if successful, null if
     *              unsuccessful
     **************************************************************************/
    public LinkedList<String> listUser() {
        LinkedList<String> users = null; // Linked list of usernames

        // SQL query
        String sql = "SELECT user_name FROM vehicle_inventory.users;";

        // execute query
        executeQuery(sql);

        try {
            if (results.next()) { // Loop through results and add usernames to users
                users = new LinkedList<>();
                users.add(results.getString(1));
                while (results.next()) {
                    users.add(results.getString(1));
                }
            }
            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
            users = null; // Set results to null if error encountered
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        // Return results
        return users;
    }

    /***************************************************************************
     * updateCar
     * Update specified field to specified value in record with matching vin
     * @param vin   VIN of vehicle to update
     * @param field field to update
     * @param value new value
     * @return  boolean true if update successful, false otherwise
     **************************************************************************/
    public boolean updateCar(String vin, String field, String value) {
        boolean updated = false;    // Track update status

        // SQL query
        String sql = "SELECT vin, " + field + " FROM " +
                "vehicle_inventory.vehicle WHERE vin = \"" + vin + "\"";

        // execute query
        executeQuery(sql);

        try {
            if (results.next()) { // matching vin found in database
                // Update field with new value
                results.updateString(2, value);
                results.updateRow();
                conn.commit();

                // Update successful
                updated = true;
            }
            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) { System.out.println(ex.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        // Return update results
        return updated;
    }

    /***************************************************************************
     * updateCar
     * Update specified field to specified value in record with matching vin
     * @param vin   VIN of vehicle to update
     * @param field field to update
     * @param value new value
     * @return  boolean true if update successful, false otherwise
     **************************************************************************/
    public boolean updateCar(String vin, String field, int value) {
        boolean updated = false;    // Track update status

        // SQL query
        String sql = "SELECT vin, " + field + " FROM " +
                "vehicle_inventory.vehicle WHERE vin = \"" + vin + "\"";

        // execute query
        executeQuery(sql);
        try {
            if (results.next()) { // matching vin found in database
                // Update field with new value
                results.updateInt(2, value);
                results.updateRow();
                conn.commit();

                // Update successful
                updated = true;
            }
            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) { System.out.println(ex.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }
        // Return update results
        return updated;
    }

    /***************************************************************************
     * addCar
     * Add the provided Vehicle to the database, if not in database
     * @param vehicle   Vehicle to add
     * @return  boolean true if added, false if not added
     **************************************************************************/
    public boolean addCar(Vehicle vehicle) {
        boolean inserted = false;   // track insert status

        // SQL query
        String sql = "SELECT vin, price, model, make, country, type, year, " +
                "mileage, features, size, color, engine, fuel_economy, " +
                "fuel_type, location FROM vehicle_inventory.vehicle WHERE vin" +
                " = \"" + vehicle.getVin() + "\";";

        // execute query
        executeQuery(sql);

        try {
            if (!results.next()) { // vehicle does not exist in database
                // Add vehicle to database
                results.moveToInsertRow();
                results.updateString(1, vehicle.getVin());
                results.updateInt(2, vehicle.getPrice());
                results.updateString(3, vehicle.getModel());
                results.updateString(4, vehicle.getMake());
                results.updateString(5, vehicle.getCountry());
                results.updateString(6, vehicle.getType());
                results.updateInt(7, vehicle.getYear());
                results.updateInt(8, vehicle.getMileage());
                results.updateString(9, vehicle.getFeatures());
                results.updateString(10, vehicle.getSize());
                results.updateString(11, vehicle.getColor());
                results.updateString(12, vehicle.getEngine());
                results.updateInt(13, vehicle.getFuel_economy());
                results.updateString(14, vehicle.getFuel_type());
                results.updateInt(15, vehicle.getLocation());
                results.insertRow();
                conn.commit();

                // Insert successful
                inserted = true;
            }
            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) { System.out.println(ex.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }

        // return insert results
        return inserted;
    }

    /***************************************************************************
     * addTitleHistory
     * Add or update title history for vehicle to database
     * @param vin           vin of vehicle to add history for
     * @param titleHistory  title history of vehicle
     * @return  boolean true if title history added/updated successfully, false
     *              if not
     **************************************************************************/
    public boolean addTitleHistory(String vin, String titleHistory) {
        boolean updated = false;    // track insert/update status

        // SQL query
        String sql = "SELECT title_history, vin FROM " +
                "vehicle_inventory.title_history WHERE vin = \"" + vin + "\";";

        // execute query
        executeQuery(sql);

        try {
            if (results.next()) { // title history exists
                // update title history
                results.updateString(1, titleHistory);
                results.updateRow();
            } else {
                // insert title history
                results.moveToInsertRow();
                results.updateString(1, titleHistory);
                results.updateString(2, vin);
                results.insertRow();
            }
            // commit changes to database
            conn.commit();

            // insert/update successful
            updated = true;

            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }

        // return results
        return updated;
    }

    /***************************************************************************
     * getTitleHistory
     * Get title history for vehicle matching vin
     * @param vin   vin of vehicle to retrieve title history for
     * @return  String of title history, null if not found
     **************************************************************************/
    public String getTitleHistory(String vin) {
        String titleHistory = null; // title history

        // SQL query
        String sql = "SELECT title_history FROM vehicle_inventory.title_history WHERE vin = \"" + vin + "\";";

        // execute query
        executeQuery(sql);

        try {
            if (results.next()) { // history found
                // get history
                titleHistory = results.getString(1);
            }
            // Close ResultSet, Statement, and Connection
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) {}
        }

        // return history
        return titleHistory;
    }

    /***************************************************************************
     * createVehicle
     * Create Vehicle object based on query results
     * @return  Vehicle object from results, or null if error encountered
     **************************************************************************/
    private Vehicle createVehicle() {
        try {
            // extract results from query results
            String vin = results.getString("vin");
            int price = results.getInt("price");
            String model = results.getString("model");
            String make = results.getString("make");
            String country = results.getString("country");
            String type = results.getString("type");
            int year = results.getInt("year");
            int mileage = results.getInt("mileage");
            String features = results.getString("features");
            String size = results.getString("size");
            String color = results.getString("color");
            String engine = results.getString("engine");
            int fuel_economy = results.getInt("fuel_economy");
            String fuel_type = results.getString("fuel_type");
            int location = results.getInt("location");

            // create and return new Vehicle
            return new Vehicle(vin, price, model, make, country, type, year,
                    mileage, features, size, color, engine, fuel_economy,
                    fuel_type, location);
        } catch (SQLException ex) {
            // return null if error encountered
            return null;
        }
    }

    /***************************************************************************
     * executeQuery
     * Run SQL query and store ResultSet in results
     * @param sql   Query to run
     **************************************************************************/
    private void executeQuery(String sql) {
        try {
            // Connect to sql server and execute query
            conn = DriverManager.getConnection(url, "", "");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            conn.setAutoCommit(false);
            results = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            // SQL query failed, set results == null
            results = null;
        }
    }

    /***************************************************************************
     * vechicleQuery
     * Create and return a LinkedList of Vehicles based on results from SQL
     *      query
     * @param sql   SQL query to execute
     * @return  LinkedList<Vehicle> if query successful or null if unsuccessful
     **************************************************************************/
    private LinkedList<Vehicle> vehicleQuery(String sql) {
        LinkedList<Vehicle> vehicleList = null; // list of vehicles to return

        try {
            // execute query
            executeQuery(sql);

            // Loop through results and add them to LinkedList
            if (results.next()) {
                vehicleList = new LinkedList<>();
                Vehicle v = createVehicle();
                vehicleList.add(v);
                while (results.next()) {
                    v = createVehicle();
                    vehicleList.add(v);
                }
            }
            // Close ResultSet and Statement
            try { results.close(); } catch (SQLException ex) {}
            try { stmt.close(); } catch (SQLException ex) {}
        } catch (SQLException ex) {
            vehicleList = null; // Set list to null if error encountered
        } finally {
            // Close Connection
            try { conn.close(); } catch (SQLException ex) {}
        }
        // return results
        return vehicleList;
    }
}
