import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.LinkedList;

// usernames = inventoryview & inventoryadd
// I need to convert this class to a Singleton (make constructor private and add a "getInvetory()" method which will
// create the single Inventory object (or return it if already created)

public class Inventory {
    String url;
    String user;
    String password;
    Connection conn;
    Statement stmt;
    ResultSet results;

    public static void main(String[] args){
        Inventory inv = new Inventory();
        LinkedList<Vehicle> searchResults = inv.getAllVehicles();
        for (Vehicle v : searchResults) {
            System.out.println(v);
        }
        String[] searchField = {"Model"};
        String[] searchCriteria = {"Ford"};
        searchResults = inv.getFilteredVehicles(new SearchNode(searchField, searchCriteria, null));
        for (Vehicle v : searchResults) {
            System.out.println(v);
        }
    }

    Inventory() {
        url = "jdbc:mysql://localhost:3306/vehicle_inventory";
        user = "inventoryview";
        password = "";
        conn = null;
        stmt = null;
        results = null;
    }

    public LinkedList<Vehicle> getAllVehicles() {
        String sql = "SELECT * FROM vehicle_inventory.vehicle;";
        return vehicleQuery(sql);
    }

    public LinkedList<Vehicle> getFilteredVehicles(SearchNode search) {
        // TODO: Update this with new SearchNode object; Update sql query creation to support multiple criteria
        // Once this is written, it can become the bases of most of the remaining methods
        String sql = "SELECT * FROM vehicle_inventory.vehicle WHERE " + search.field[0] + "=\"" + search.criteria[0] +"\";";
        return vehicleQuery(sql);
    }

    public boolean checkUser() {
        //TODO: write check user
        return false;
    }

    public boolean addUser() {
        //TODO: write check user
        return false;
    }

    public boolean deactivateUser() {
        //TODO: write deactivate user
        return false;
    }

    // We should create a User object. It will be a simple object with a username & password. We can pass that to and
    //  from the *User methods
    public LinkedList<User> listUser() {
        //TODO: write list user
        return null;
    }

    public boolean updateCar() {
        //Todo: Update attributes of existing car
        return false;
    }

    public boolean addCar() {
        //Todo: Add car to database
        return false;
    }

    public boolean addTitleHistory() {
        //Todo: Add Title History to database
        return false;
    }

    public String getTitleHistory() {
        return null;
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
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();

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

    // I don't really like the way this works and am trying to think of a better data structure
    // The way it works now, the field array holds the fields to search, the criteria array holds the criteria and
    // the searchType array holds the type (>, <, =, >=, <=)
    // so, if the first thing to filter on was country is japan, field[0] = country, criteria[0] = japan, searchType[0] = =
    // this feels clunky and it would probably be better design to have a three-element array that holds all of these items
    // rather than spreading them across three separate arrays.
    // I think I may change the input to a LinkedList<String[]> where each array in the list has three parameters that form
    // a search
    static class SearchNode {
        String[] field;
        String[] criteria;
        String[] searchType;

        SearchNode(String[] field, String[] criteria, String[] searchType){
            this.field = field;
            this.criteria = criteria;
            this.searchType = searchType;
        }
    }

    // Shell Vehicle class for testing creating objects from the database
    static class Vehicle {

        String vin;
        int price;
        String model;
        String make;
        String country;
        String type;
        int year;
        int mileage;
        String features;
        String size;
        String color;
        String engine;
        int fuel_economy;
        String fuel_type;
        int location;

        public Vehicle(String vin, int price, String model, String make, String country, String type, int year
                , int mileage, String features, String size, String color, String engine, int fuel_economy
                , String fuel_type, int location) {
            this.vin = vin;
            this.price = price;
            this.model = model;
            this.make = make;
            this.country = country;
            this.type = type;
            this.year = year;
            this.mileage = mileage;
            this.features = features;
            this.size = size;
            this.color = color;
            this.engine = engine;
            this.fuel_economy = fuel_economy;
            this.fuel_type = fuel_type;
            this.location = location;
        }

        @Override
        public String toString() {
            return "Vehicle{" +
                    "vin='" + vin + '\'' +
                    ", price=" + price +
                    ", model='" + model + '\'' +
                    ", make='" + make + '\'' +
                    ", country='" + country + '\'' +
                    ", type='" + type + '\'' +
                    ", year=" + year +
                    ", mileage=" + mileage +
                    ", features='" + features + '\'' +
                    ", size='" + size + '\'' +
                    ", color='" + color + '\'' +
                    ", engine='" + engine + '\'' +
                    ", fuel_economy=" + fuel_economy +
                    ", fuel_type='" + fuel_type + '\'' +
                    ", location=" + location +
                    '}';
        }
    }

    static class User{
        String username;
        String password;

        User(String username, String password, boolean hash){
            this.username = username;
            if (!hash) {
                this.password = password;
            } else {
                hashPassword(password);
            }
        }

        private void hashPassword(String password) {
            // write some algorithm for converting a text password to a hashed password for storage
            this.password = password;
        }
    }
}
