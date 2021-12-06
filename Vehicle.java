/*******************************************************************************
 * Arielle Gilmore
 * Courtney Geer Ahaus
 * Carlos Del Valle
 * David Williams
 * Mandelson Acabado
 * CPSC 4360
 * Professor: Dr. Stefan Andrei
 * Group Project
 * Due: December 5, 2021
 * This is a simple automobile sales application for a car dealership. It will
 *      allow a user to browse, search, and purchase or sell a car. Admin users
 *      can also activate, deactivate, list, and add users and edit existing
 *      vehicles. The program is written in Java and uses a MySQL back-end
 *      database to store the vehicle inventory and user credentials.
 ******************************************************************************/

public class Vehicle {

    private final String vin;         // Vehicle Identification Number
    private final int price;          // price of vehicle
    private final String model;       // model of vehicle
    private final String make;        // make of vehicle
    private final String country;     // country of origin
    private final String type;        // type of vehicle (e.g. coupe, truck, etc)
    private final int year;           // year of manufacture
    private final int mileage;        // vehicle mileage
    private final String features;    // optional features
    private final String size;        // size of vehicle (standard, mid-size, etc)
    private final String color;       // color of vehicle
    private final String engine;      // engine type
    private final int fuel_economy;   // average miles per gallon
    private final String fuel_type;   // fuel type (standard, diesel, etc)
    private final int location;       // zip code where vehicle is located

    /***************************************************************************
     * Vehicle
     * Vehicle constructor
     * @param vin// Vehicle Identification Number
     * @param price         // price of vehicle
     * @param model         // model of vehicle
     * @param make          // make of vehicle
     * @param country       // country of origin
     * @param type          // type of vehicle (e.g. coupe, truck, etc.)
     * @param year          // year of manufacture
     * @param mileage       // vehicle mileage
     * @param features      // optional features
     * @param size          // size of vehicle (standard, mid-size, etc.)
     * @param color         // color of vehicle
     * @param engine        // engine type
     * @param fuel_economy  // average miles per gallon
     * @param fuel_type     // fuel type (standard, diesel, etc)
     * @param location      // zip code where vehicle is located
     **************************************************************************/
    public Vehicle(String vin, int price, String model, String make,
                   String country, String type, int year, int mileage,
                   String features, String size, String color, String engine,
                   int fuel_economy, String fuel_type, int location) {
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

    /***************************************************************************
     * getVin
     * Get vehicle identification number
     * @return  String vin
     **************************************************************************/
    public String getVin() {
        return vin;
    }

    /***************************************************************************
     * getPrice
     * Get price of vehicle
     * @return  int price
     **************************************************************************/
    public int getPrice() {
        return price;
    }

    /***************************************************************************
     * getModel
     * Get model of vehicle
     * @return  String model
     **************************************************************************/
    public String getModel() {
        return model;
    }

    /***************************************************************************
     * getMake
     * Get make of vehicle
     * @return  String make
     **************************************************************************/
    public String getMake() {
        return make;
    }

    /***************************************************************************
     * getCountry
     * Get country of origin of vehicle
     * @return  String country
     **************************************************************************/
    public String getCountry() {
        return country;
    }

    /***************************************************************************
     * getType
     * Get type (sedan, coupe, truck, etc.) of vehicle
     * @return  String type
     **************************************************************************/
    public String getType() {
        return type;
    }

    /***************************************************************************
     * getYear
     * Get year of manufacture of vehicle
     * @return  int year
     **************************************************************************/
    public int getYear() {
        return year;
    }

    /***************************************************************************
     * getMileage
     * Get mileage of vehicle
     * @return  int mileage
     **************************************************************************/
    public int getMileage() {
        return mileage;
    }

    /***************************************************************************
     * getFeatures
     * Get optional features of vehicle
     * @return  String features
     **************************************************************************/
    public String getFeatures() {
        return features;
    }

    /***************************************************************************
     * getSize
     * Get size (mid-size, full-size, etc.) of vehicle
     * @return  String size
     **************************************************************************/
    public String getSize() {
        return size;
    }

    /***************************************************************************
     * getColor
     * Get color of vehicle
     * @return  String color
     **************************************************************************/
    public String getColor() {
        return color;
    }

    /***************************************************************************
     * getEngine
     * Get engine type of vehicle
     * @return  String engine
     **************************************************************************/
    public String getEngine() {
        return engine;
    }

    /***************************************************************************
     * getFuel_economy
     * Get average fuel economy of vehicle
     * @return  int fuel_economy
     **************************************************************************/
    public int getFuel_economy() {
        return fuel_economy;
    }

    /***************************************************************************
     * getFuel_type
     * Get fuel type of car (diesel, premium, etc)
     * @return  String fuel_type
     **************************************************************************/
    public String getFuel_type() {
        return fuel_type;
    }

    /***************************************************************************
     * getLocation
     * Get current location (ZIP code) of vehicle
     * @return  int location
     **************************************************************************/
    public int getLocation() {
        return location;
    }

    /***************************************************************************
     * sell
     * Update database to reflect sale of vehicle to User
     * @param user  User who is purchasing car
     **************************************************************************/
    public boolean sell(User user) {
        // update car in Inventory
        return Inventory.getInventory().updateCar(vin, "sold", user.getUsername());
    }

    /***************************************************************************
     * toString
     * Output contents of Vehicle as a string
     * @return  String of Vehicle fields
     **************************************************************************/
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
