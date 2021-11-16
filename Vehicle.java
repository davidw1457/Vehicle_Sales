public class Vehicle {

    private String vin;
    private int price;
    private String model;
    private String make;
    private String country;
    private String type;
    private int year;
    private int mileage;
    private String features;
    private String size;
    private String color;
    private String engine;
    private int fuel_economy;
    private String fuel_type;
    private int location;

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

    public String getVin() {
        return vin;
    }

    public int getPrice() {
        return price;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public String getCountry() {
        return country;
    }

    public String getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public int getMileage() {
        return mileage;
    }

    public String getFeatures() {
        return features;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getEngine() {
        return engine;
    }

    public int getFuel_economy() {
        return fuel_economy;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public int getLocation() {
        return location;
    }

    public void sell(User user) {
        Inventory.getInventory().updateCar(vin, "sold", user.getUsername());
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
