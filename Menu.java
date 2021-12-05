import java.util.LinkedList;
import java.util.Scanner;

/*******************************************************************************
 * David Williams
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
public class Menu{

    private User user;                  // Currently logged in user
    private final Inventory inventory;  // Vehicle invontory
    private final Scanner input;        // Read input from user

    public static void main(String[] args) {
        // Create menu
        Menu menu = new Menu();

        // Let user login or quit
        char choice = menu.loginMenu();

        // Loop until user quits
        while (choice != 'Q') {
            // Login to system
            menu.login();

            // Loop back to the starting menu until user logs out
            while (menu.isLoggedIn()) {
                menu.displayMenu();
            }

            // Let user login or quit
            choice = menu.loginMenu();
        }
    }

    /***************************************************************************
     * Menu
     * Menu constructor
     **************************************************************************/
    public Menu () {
        // Set user to null until logged in
        user = null;

        // Get inventory
        inventory = Inventory.getInventory();

        // Initialize input Scanner
        input = new Scanner(System.in);
    }

    /***************************************************************************
     * isUserAdmin
     * Check if currently logged in user is admin
     * @return  true if user is admin, false otherwise
     **************************************************************************/
    private boolean isUserAdmin() {
        // If user not logged in, not admin
        if (user == null) {
            return false;
        }

        // Return admins status of user
        return user.isAdmin();
    }

    /***************************************************************************
     * isLoggedIn
     * Check whether a user is logged in to the system
     * @return  true if user is logged in, false otherwise
     **************************************************************************/
    public boolean isLoggedIn() {
        // If user == null, user is not logged in
        return !(user == null);
    }

    /***************************************************************************
     * login
     * User login to the system
     **************************************************************************/
    public void login() {
        String username;    // username
        String password;    // user password

        // get username and password
        System.out.print("Enter username: ");
        username = input.nextLine();
        System.out.print("Enter password: ");
        password = input.nextLine();

        // check for validity
        user = inventory.checkUser(username, password);
        if (user == null) { // no match found in data
            System.out.println("Invalid username or password\n");
        }
    }

    /***************************************************************************
     * loginMenu
     * Prompt the user to login or quit
     * @return  Q if user choses to quit, L if user chooses to login
     **************************************************************************/
    public char loginMenu() {
        char choice = '\0'; // Option

        // Display menu until user chooses to login or quit
        while (choice == '\0') {
            System.out.print("Enter L to login or Q to quit: ");

            // Get first letter of input
            choice = input.nextLine().toUpperCase().charAt(0);

            // Verify the choice is valid, set choice to null if not
            if (choice != 'Q' && choice != 'L') {   // Invalid choice
                System.out.println("Invalid input\n");
                choice = '\0';
            }
        }

        // Return selected option
        return choice;
    }

    /***************************************************************************
     * displayMenu
     * Show main menu and process user choice
     **************************************************************************/
    public void displayMenu() {
        // Display menu
        System.out.println("Main Menu:");
        System.out.println("1.\tBrowse Vehicles");
        System.out.println("2.\tView Title History");
        System.out.println("3.\tSell Vehicle");
        if (isUserAdmin()) {    // Admin options
            System.out.println("4.\tEdit Vehicle");
            System.out.println("5.\tUpdate Title History");
            System.out.println("6.\tList Users");
            System.out.println("7.\tAdd User");
            System.out.println("8.\tActivate/Deactivate User");
        }
        System.out.println("9.\tLogout");
        System.out.print("Enter choice: ");

        // accept input from user
        int choice = input.nextInt();

        // Move pointer to next line
        if (input.hasNextLine()) {input.nextLine();}

        // process choice
        switch (choice) {
            case 1 :    // Browse vehicles
                viewVehicles();
                break;
            case 2 :    // View title history
                viewTitleHistory();
                break;
            case 3 :    // Sell vehicle
                sellVehicle();
                break;
            case 4 :    // Edit existing vehicle
                if (isUserAdmin()) {    // ensure user is admin
                    editVehicle();
                    break;
                }
            case 5 :    // Update title history
                if (isUserAdmin()) {    // ensure user is admin
                    updateTitleHistory();
                    break;
                }
            case 6 :    // List all users
                if (isUserAdmin()) {    // ensure user is admin
                    listUsers();
                    break;
                }
            case 7 :    // Add new user
                if (isUserAdmin()) {    // ensure user is admin
                    addUser();
                    break;
                }
            case 8 :    // Activate/Deactivate user
                if (isUserAdmin()) {    // ensure user is admin
                    changeUserStatus();
                    break;
                }
            default :   // invalid choice (will fall through from admin choices if not admin)
                System.out.println("Invalid choice\n");
                break;
            case 9 :    // logout of system
                logout();
                break;
        }
    }

    /***************************************************************************
     * logout
     * Log user out of system
     **************************************************************************/
    private void logout() {
        System.out.println("\nLogging out...\n");

        // Clear current user
        user = null;
    }

    /***************************************************************************
     * changeUserStatus
     * Activate or deactivate user
     **************************************************************************/
    private void changeUserStatus() {
        int choice = 0;     // Option
        String username;    // Username to change
        while (choice != 3) {   // Loop until user chooses to return to main menu
            System.out.println("\nActivate or Deactivate User");
            System.out.println("1. Activate User");
            System.out.println("2. Deactivate User");
            System.out.println("3. Return to main menu");
            System.out.print("Enter choice: ");

            // Accept user input
            choice = input.nextInt();

            // Move pointer to next line
            if (input.hasNextLine()) {input.nextLine();}

            // Process choice
            switch (choice) {
                case 1 :    // Activate user
                    System.out.print("Enter username: ");
                    username = input.nextLine();

                    // Attempt to activate user
                    if (inventory.activateUser(username)) { // successful
                        System.out.printf("%s activated\n\n", username);
                    } else {    // failure
                        System.out.printf("%s not found\n\n", username);
                    }
                    break;
                case 2 :    // Deactivate user
                    System.out.print("Enter username: ");
                    username = input.nextLine();

                    // Attempt to deactivate user
                    if (inventory.deactivateUser(username)) {   // successful
                        System.out.printf("%s deactivated\n\n", username);
                    } else {    // failure
                        System.out.printf("%s not found\n\n", username);
                    }
                    break;
                case 3 :    // Return to main menu
                    break;
                default :   // Invalid choice
                    System.out.println("Invalid choice");
            }
        }
    }

    /***************************************************************************
     * addUser
     * Add a new user to the system
     **************************************************************************/
    private void addUser() {

        System.out.println("Add User\n");

        // Get username
        System.out.print("Enter username: ");
        String username = input.nextLine();

        // Get password
        System.out.print("Enter password: ");
        String password = input.nextLine();

        // Get admin satus
        System.out.print("Is user admin (Y/N)? ");
        char admin = input.nextLine().toUpperCase().charAt(0);

        // Attempt to add user
        if (inventory.addUser(username, password, admin=='Y')) {    // Successful
            System.out.printf("%s added.\n\n", username);
        } else {    // failure
            System.out.printf("Unable to add %s\n\n", username);
        }
    }

    /***************************************************************************
     * lustUsers
     * List all users
     **************************************************************************/
    private void listUsers() {
        System.out.println("List Users\n");

        // get list of users from Inventory and print to screen
        for (String username : inventory.listUser()) {
            System.out.println(username);
        }
        System.out.println();
    }

    /***************************************************************************
     * updateTitleHistory
     * Upload a new title history for a vehicle
     **************************************************************************/
    private void updateTitleHistory() {
        System.out.println("Update Title History\n");

        // Get VIN and title history of vehicle tup update
        System.out.print("Enter vin: ");
        String vin = input.nextLine();
        System.out.print("Enter history: ");
        String titleHistory = input.nextLine();

        // Update inventory with new title history
        if (inventory.addTitleHistory(vin, titleHistory)) { // Successful
            System.out.println("Updated successfully\n");
        } else {    // Failure
            System.out.println("Unable to update title history");
        }
    }

    /***************************************************************************
     * editVehicle
     * Edit a property on a vehicle in the inventory
     **************************************************************************/
    private void editVehicle() {
        int fieldChoice = 0;    // Option
        String field = null;    // Field to update

        System.out.println("Edit vehicle\n");

        // Get VIN of vehicle to update
        System.out.print("Enter vin: ");
        String vin = input.nextLine();

        // Retrieve vehicle from inventory
        LinkedList<Vehicle> vehicle = inventory.getFilteredVehicles(vin, "vin");

        if (vehicle == null) {  // Vehicle not returned from inventory
            System.out.println("Vehicle not found\n");
        } else {    // Vehicle returned
            while (fieldChoice == 0 && vehicle != null) {
                // Display current vehicle properties
                displayVehicle(vehicle.getFirst());

                // Get field to update
                System.out.println("Select field to update");
                System.out.println("1.  Price          2. Year       3. Mileage");
                System.out.println("4.  Fuel Economy   5. Location   6. Model");
                System.out.println("7.  Make           8. Features   9. Size");
                System.out.println("10. Color         11. Engine    12. Country");
                System.out.println("13. Fuel Type     14. Type      15. Exit");
                System.out.print("Enter choice: ");
                fieldChoice = input.nextInt();

                // Move pointer to next line
                if (input.hasNextLine()) {input.nextLine();}

                // Update field based on choice
                switch (fieldChoice) {
                    case 1 :    // Price
                        field = "price";
                        break;
                    case 2 :    // Year
                        field = "year";
                        break;
                    case 3 :    // Mileage
                        field = "mileage";
                        break;
                    case 4 :    // Fuel Economy
                        field = "fuel_economy";
                        break;
                    case 5 :    // Location
                        field = "location";
                        break;
                    case 6 :    // Model
                        field = "model";
                        break;
                    case 7 :    // Make
                        field = "make";
                        break;
                    case 8 :    // Features
                        field = "features";
                        break;
                    case 9 :    // Size
                        field = "size";
                        break;
                    case 10 :   // Color
                        field = "color";
                        break;
                    case 11 :   // Engine
                        field = "engine";
                        break;
                    case 12 :   // Country
                        field = "country";
                        break;
                    case 13 :   // Fuel Type
                        field = "fuel_type";
                        break;
                    case 14 :   // Type
                        field = "type";
                        break;
                    case 15 :   // Exit
                        break;
                    default :   // Invalid option
                        System.out.println("Invalid choice\n");

                        // Reset field choice to 0
                        fieldChoice = 0;
                        break;
                }
                if (fieldChoice == 15) {    // Exit
                    System.out.println("Returning to main menu\n");
                } else if (fieldChoice > 0) {
                    System.out.printf("Enter new value for %s: ", field);
                    if (fieldChoice < 6) {  // Field is an integer
                        // Get new value
                        int intValue = input.nextInt();

                        // Move pointer to next line
                        if (input.hasNextLine()) {input.nextLine();}

                        // Update inventory with new value
                        inventory.updateCar(vin, field, intValue);
                    } else {    // Field is a string
                        // Get new value
                        String strValue = input.nextLine();

                        // Udpate inventory with new value
                        inventory.updateCar(vin, field, strValue);
                    }
                    // Referesh vehicle
                    vehicle = inventory.getFilteredVehicles(vin, "vin");
                    // reset choice
                    fieldChoice = 0;
                }
            }
        }
    }

    /***************************************************************************
     * displayVehicle
     * Display properties of vehicle in a simple table
     * @param vehicle Vehicle object to display
     **************************************************************************/
    private void displayVehicle(Vehicle vehicle) {
        System.out.printf("      Vin: %-20s        Price: $%,-20d\n", vehicle.getVin(), vehicle.getPrice());
        System.out.printf("    Model: %-20s         Make: %-20s\n", vehicle.getModel(), vehicle.getMake());
        System.out.printf("  Country: %-20s         Type: %-20s\n", vehicle.getCountry(), vehicle.getType());
        System.out.printf("     Year: %-20d      Mileage: %,-20d\n", vehicle.getYear(), vehicle.getMileage());
        System.out.printf("     Size: %-20s        Color: %-20s\n", vehicle.getSize(), vehicle.getColor());
        System.out.printf("   Engine: %-20s Fuel Economy: %-20d\n", vehicle.getEngine(), vehicle.getFuel_economy());
        System.out.printf("Fuel Type: %-20s     Location: %-20d\n", vehicle.getFuel_type(), vehicle.getLocation());
        System.out.printf("Additional Features: %s\n\n", vehicle.getFeatures());
    }

    /***************************************************************************
     * sellVehicle
     * Sell/add vehicle to system
     **************************************************************************/
    private void sellVehicle() {
        System.out.println("Sell Vehicle\n");

        // Get properites of vehicle
        System.out.print("Input vin: ");
        String vin = input.nextLine();
        System.out.print("Input price: ");
        int price = input.nextInt();

        // Move pointer to next line
        if (input.hasNextLine()) {input.nextLine();}

        System.out.print("Input model: ");
        String model = input.nextLine();
        System.out.print("Enter make: ");
        String make = input.nextLine();
        System.out.print("Enter country: ");
        String country = input.nextLine();
        System.out.print("Enter type: ");
        String type = input.nextLine();
        System.out.print("Enter year: ");
        int year = input.nextInt();
        System.out.print("Enter mileage: ");
        int mileage = input.nextInt();

        // Move pointer to next line
        if (input.hasNextLine()) {input.nextLine();}

        System.out.print("Enter features: ");
        String features = input.nextLine();
        System.out.print("Enter size: ");
        String size = input.nextLine();
        System.out.print("Enter color: ");
        String color = input.nextLine();
        System.out.print("Enter engine: ");
        String engine = input.nextLine();
        System.out.print("Enter fuel economy: ");
        int fuel_economy = input.nextInt();

        // Move pointer to next line
        if (input.hasNextLine()) {input.nextLine();}

        System.out.print("Enter fuel type: ");
        String fuel_type = input.nextLine();
        System.out.print("Enter location: ");
        int location = input.nextInt();

        // Move pointer to next line
        if (input.hasNextLine()) {input.nextLine();}

        // Create new vehicle object
        Vehicle vehicle = new Vehicle(vin, price, model, make, country, type,
                year, mileage, features, size, color, engine, fuel_economy,
                fuel_type, location);

        // Add vehicle to inventory
        if (inventory.addCar(vehicle)) {    // Successful
            System.out.println("Car sold. Returning to main menu.\n");
        } else {    // Failure
            System.out.println("Unable to sell car. Returning to main menu.\n");
        }
    }

    /***************************************************************************
     * viewTitleHistory
     * Display title history for vehicle based on VIN
     **************************************************************************/
    private void viewTitleHistory() {
        System.out.println("View Title History\n");

        // Get VIN
        System.out.print("Enter vin: ");
        String vin = input.nextLine();

        // Get title history from inventory
        String titleHistory = inventory.getTitleHistory(vin);

        if (titleHistory == null) { // Failure
            System.out.printf("Unable to find title history for %s\n\n", vin);
        } else {    // Success
            System.out.printf("%s: %s\n\n", vin, titleHistory);
        }
    }

    /***************************************************************************
     * viewVehicles
     * Display filtered or unfiltered list of vehicles, one vehicle at a time
     **************************************************************************/
    private void viewVehicles() {
        int choice = 0; // Option
        LinkedList<Vehicle> vehicles = null;    // List of vehicles
        while (choice != 3) {   // Loop until user chooses to exit
            System.out.println("1. View all vehicles");
            System.out.println("2. Search vehicles");
            System.out.println("3. Return to main menu");
            System.out.print("Enter choice: ");

            // Get user choice
            choice = input.nextInt();

            // Move pointer to next line
            if (input.hasNextLine()) {input.nextLine();}

            switch (choice) {
                case 1 :    // All vehicles
                    vehicles = inventory.getAllVehicles();
                    break;
                case 2 :    // Filtered list of vehicles
                    vehicles = filterVehicles();
                    break;
                case 3 :    // Exit
                    break;
                default :   // Invalid input
                    System.out.println("Invalid option\n");
                    break;
            }

            if ((choice == 1 || choice == 2) && vehicles == null) {
                // If vehicles == null, no vehicles were returned by inventory
                System.out.println("No vehicles found\n");
            } else if (choice == 1 || choice == 2) {
                choice = 0; // reset choice
                int current = 0;    // current vehicle in list
                int vehicleCount = vehicles.size(); // size of list

                // loop until user chooses to exit
                while (choice != 4) {
                    // Display current vehicle
                    displayVehicle(vehicles.get(current));
                    if (current > 0) { // if not first vehicle
                        System.out.println("\n1. Previous Vehicle");
                    } else {
                        System.out.println(("\n"));
                    }

                    if (current < vehicleCount - 1) { // if not last vehicle
                        System.out.println("2. Next Vehicle");
                    } else {
                        System.out.println();
                    }

                    System.out.println("3. Purchase vehicle");
                    System.out.println("4. Exit vehicle list");
                    System.out.print("Enter choice: ");

                    // Get choice
                    choice = input.nextInt();

                    // Move pointer to next line
                    if (input.hasNextLine()) {input.nextLine();}

                    switch (choice) {
                        case 1 :    // Previous vehicle
                            if (current > 0) {  // if there is a previous vehicle...
                                // Decrement current
                                --current;
                            } else {    // No previous vehicle
                                System.out.println("Invalid choice\n");
                            }
                            break;
                        case 2 :    // Next vehicle
                            if (current < vehicleCount - 1) {   // if there is a next vehicle
                                // increment current
                                ++current;
                            } else {    // No next vehicle
                                System.out.println("Invalid choice\n");
                            }
                            break;
                        case 3 :    // Purchase vehicle
                            // Attempt to purchase current vehicle
                            if (vehicles.get(current).sell(user)) { // Successful
                                System.out.printf("Vehicle purchased by %s\n", user.getUsername());

                                // Exit vehicle list after successful purchase
                                choice = 4;
                            } else {    // Failure
                                System.out.println("Unable to purchase vehicle. Please try again later");
                            }
                            break;
                        case 4 : // Exit
                            break;
                        default :   // Invalid input
                            System.out.println("Invalid choice\n");
                    }
                }
                // reset choice
                choice = 0;
            }
        }
    }

    /***************************************************************************
     * filteredVehicles
     * @return LinkedList of vehicles matching filter
     **************************************************************************/
    private LinkedList<Vehicle> filterVehicles() {
        int fieldChoice = 0;    // option
        String field = null;    // field to search
        while (fieldChoice == 0) {  // loop until valid input
            System.out.println("Select field to filter on");
            System.out.println("1.  Price          2. Year       3. Mileage");
            System.out.println("4.  Fuel Economy   5. Location   6. Model");
            System.out.println("7.  Make           8. Features   9. Size");
            System.out.println("10. Color         11. Engine    12. Country");
            System.out.println("13. Fuel Type     14. Type      15. VIN");
            System.out.print("Enter choice: ");

            // get choice
            fieldChoice = input.nextInt();

            // Move pointer to next line
            if (input.hasNextLine()) {input.nextLine();}

            switch (fieldChoice) {
                case 1 :    // Price
                    field = "price";
                    break;
                case 2 :    // Year
                    field = "year";
                    break;
                case 3 :    // Mileage
                    field = "mileage";
                    break;
                case 4 :    // Fuel Economy
                    field = "fuel_economy";
                    break;
                case 5 :    // Location
                    field = "location";
                    break;
                case 6 :    // Model
                    field = "model";
                    break;
                case 7 :    // Make
                    field = "make";
                    break;
                case 8 :    // Features
                    field = "features";
                    break;
                case 9 :    // Size
                    field = "size";
                    break;
                case 10 :   // Color
                    field = "color";
                    break;
                case 11 :   // Engine
                    field = "engine";
                    break;
                case 12 :   // Country
                    field = "country";
                    break;
                case 13 :   // Fuel Type
                    field = "fuel_type";
                    break;
                case 14 :   // Type
                    field = "type";
                    break;
                case 15 :   // VIN
                    field = "vin";
                    break;
                default :   // Invalid input
                    System.out.println("Invalid choice\n");
                    // Reset choice
                    fieldChoice = 0;
                    break;
            }
        }

        System.out.printf("Enter value to search %s for: ", field);
        if (fieldChoice < 6) {  // Field is an integer
            // Get criteria
            int intCriteria = input.nextInt();

            // Move pointer to next line
            if (input.hasNextLine()) {input.nextLine();}

            // Return filtered list from inventory
            return inventory.getFilteredVehicles(intCriteria, field);
        } else {    // Field is a string
            // Get criteria
            String strCriteria = input.nextLine();

            // Return filtered list from inventory
            return inventory.getFilteredVehicles(strCriteria, field);
        }
    }
}