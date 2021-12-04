import java.util.LinkedList;
import java.util.Scanner;

public class Menu{

    private User user;
    private final Inventory inventory;
    private final Scanner input;

    public static void main(String[] args) {
        Menu menu = new Menu();
        char choice;

        choice = menu.loginMenu();
        while (choice != 'Q') {
            menu.login();
            while (menu.isLoggedIn()) {
                menu.displayMenu();
            }
            choice = menu.loginMenu();
        }
    }

    public Menu () {
        user = null;
        inventory = Inventory.getInventory();
        input = new Scanner(System.in);
    }

    private boolean isUserAdmin() {
        if (user == null) {
            return false;
        }
        return user.isAdmin();
    }

    public boolean isLoggedIn() {
        return !(user == null);
    }

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

    public char loginMenu() {
        char choice = '\0'; // Option

        // Display menu until user chooses to login or quit
        while (choice == '\0') {
            System.out.print("Enter L to login or Q to quit: ");
            choice = input.nextLine().toUpperCase().charAt(0);

            if (choice != 'Q' && choice != 'L') {   // Invalid choice
                System.out.println("Invalid input\n");
                choice = '\0';
            }
        }

        // Return selected option
        return choice;
    }

    public void displayMenu() {
        int choice; // Option

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

        choice = input.nextInt();

        // Move pointer to next line
        if (input.hasNextLine()) {input.nextLine();}

        switch (choice) {
            case 1 :
                viewVehicles();
                break;
            case 2 :
                viewTitleHistory();
                break;
            case 3 :
                sellVehicle();
                break;
            case 4 :
                if (isUserAdmin()) {
                    editVehicle();
                    break;
                }
            case 5 :
                if (isUserAdmin()) {
                    updateTitleHistory();
                    break;
                }
            case 6 :
                if (isUserAdmin()) {
                    listUsers();
                    break;
                }
            case 7 :
                if (isUserAdmin()) {
                    addUser();
                    break;
                }
            case 8 :
                if (isUserAdmin()) {
                    changeUserStatus();
                    break;
                }
            default :
                System.out.println("Invalid choice\n");
                break;
            case 9 :
                logout();
                break;
        }
    }

    private void logout() {
        System.out.println("\nLogging out...\n");
        user = null;
    }

    private void changeUserStatus() {
        int choice = 0;
        String username;
        while (choice != 3) {
            System.out.println("\nActivate or Deactivate User");
            System.out.println("1. Activate User");
            System.out.println("2. Deactivate User");
            System.out.println("3. Return to main menu");
            System.out.print("Enter choice: ");
            choice = input.nextInt();

            // Move pointer to next line
            if (input.hasNextLine()) {input.nextLine();}

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    username = input.nextLine();
                    if (inventory.activateUser(username)) {
                        System.out.printf("%s activated\n\n", username);
                    } else {
                        System.out.printf("%s not found\n\n", username);
                    }
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    username = input.nextLine();
                    if (inventory.deactivateUser(username)) {
                        System.out.printf("%s deactivated\n\n", username);
                    } else {
                        System.out.printf("%s not found\n\n", username);
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void addUser() {
        String username;
        String password;
        char admin;
        System.out.println("Add User\n");
        System.out.print("Enter username: ");
        username = input.nextLine();
        System.out.print("Enter password: ");
        password = input.nextLine();
        System.out.print("Is user admin (Y/N)? ");
        admin = input.nextLine().toUpperCase().charAt(0);

        if (inventory.addUser(username, password, admin=='Y')) {
            System.out.printf("%s added.\n\n", username);
        } else {
            System.out.printf("Unable to add %s\n\n", username);
        }
    }

    private void listUsers() {
        System.out.println("List Users\n");
        for (String username : inventory.listUser()) {
            System.out.println(username);
        }
        System.out.println();
    }

    private void updateTitleHistory() {
        String vin;
        String titleHistory;
        System.out.println("Update Title History\n");
        System.out.print("Enter vin: ");
        vin = input.nextLine();
        System.out.print("Enter history: ");
        titleHistory = input.nextLine();
        if (inventory.addTitleHistory(vin, titleHistory)) {
            System.out.println("Updated successfully\n");
        } else {
            System.out.println("Unable to update title history");
        }
    }

    private void editVehicle() {
        String vin;
        int fieldChoice = 0;
        String field = null;
        System.out.println("Edit vehicle\n");
        System.out.print("Enter vin: ");
        vin = input.nextLine();
        LinkedList<Vehicle> vehicle = inventory.getFilteredVehicles(vin, "vin");
        if (vehicle == null) {
            System.out.println("Vehicle not found\n");
        } else {
            while (fieldChoice == 0 && vehicle != null) {
                displayVehicle(vehicle.getFirst());
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

                switch (fieldChoice) {
                    case 1 :
                        field = "price";
                        break;
                    case 2 :
                        field = "year";
                        break;
                    case 3 :
                        field = "mileage";
                        break;
                    case 4 :
                        field = "fuel_economy";
                        break;
                    case 5 :
                        field = "location";
                        break;
                    case 6 :
                        field = "model";
                        break;
                    case 7 :
                        field = "make";
                        break;
                    case 8 :
                        field = "features";
                        break;
                    case 9 :
                        field = "size";
                        break;
                    case 10 :
                        field = "color";
                        break;
                    case 11 :
                        field = "engine";
                        break;
                    case 12 :
                        field = "country";
                        break;
                    case 13 :
                        field = "fuel_type";
                        break;
                    case 14 :
                        field = "type";
                        break;
                    case 15 :
                        break;
                    default :
                        System.out.println("Invalid choice\n");
                        fieldChoice = 0;
                        break;
                }
                if (fieldChoice == 15) {
                    System.out.println("Returning to main menu\n");
                } else if (fieldChoice > 0) {
                    System.out.printf("Enter new value for %s: ", field);
                    if (fieldChoice < 6) {
                        int intValue = input.nextInt();

                        // Move pointer to next line
                        if (input.hasNextLine()) {input.nextLine();}

                        inventory.updateCar(vin, field, intValue);
                    } else {
                        String strValue = input.nextLine();
                        inventory.updateCar(vin, field, strValue);
                    }
                    vehicle = inventory.getFilteredVehicles(vin, "vin");
                    fieldChoice = 0;
                }
            }
        }
    }

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

    private void sellVehicle() {
        System.out.println("Sell Vehicle\n");
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

        Vehicle vehicle = new Vehicle(vin, price, model, make, country, type,
                year, mileage, features, size, color, engine, fuel_economy,
                fuel_type, location);
        if (inventory.addCar(vehicle)) {
            System.out.println("Car sold. Returning to main menu.\n");
        } else {
            System.out.println("Unable to sell car. Returning to main menu.\n");
        }

    }

    private void viewTitleHistory() {
        String vin;
        String titleHistory;
        System.out.println("View Title History\n");
        System.out.print("Enter vin: ");
        vin = input.nextLine();
        titleHistory = inventory.getTitleHistory(vin);
        if (titleHistory == null) {
            System.out.printf("Unable to find title history for %s\n\n", vin);
        } else {
            System.out.printf("%s: %s\n\n", vin, titleHistory);
        }
    }

    private void viewVehicles() {
        int choice = 0;
        LinkedList<Vehicle> vehicles = null;
        while (choice != 3) {
            System.out.println("1. View all vehicles");
            System.out.println("2. Search vehicles");
            System.out.println("3. Return to main menu");
            System.out.print("Enter choice: ");
            choice = input.nextInt();

            // Move pointer to next line
            if (input.hasNextLine()) {input.nextLine();}

            switch (choice) {
                case 1:
                    vehicles = inventory.getAllVehicles();
                    break;
                case 2:
                    vehicles = filterVehicles();
                    break;
                case 3:
                    break;
                default :
                    System.out.println("Invalid option\n");
                    break;
            }
            if ((choice == 1 || choice == 2) && vehicles == null) {
                System.out.println("No vehicles found\n");
            } else if (choice == 1 || choice == 2) {
                choice = 0;
                int current = 0;
                int vehicleCount = vehicles.size();
                while (choice != 4) {
                    displayVehicle(vehicles.get(current));
                    if (current > 0) {
                        System.out.println("\n1. Previous Vehicle");
                    } else {
                        System.out.println(("\n"));
                    }

                    if (current < vehicleCount - 1) {
                        System.out.println("2. Next Vehicle");
                    } else {
                        System.out.println();
                    }

                    System.out.println("3. Purchase vehicle");
                    System.out.println("4. Exit vehicle list");
                    System.out.print("Enter choice: ");
                    choice = input.nextInt();

                    // Move pointer to next line
                    if (input.hasNextLine()) {input.nextLine();}

                    switch (choice) {
                        case 1 :
                            if (current > 0) {
                                --current;
                            } else {
                                System.out.println("Invalid choice\n");
                            }
                            break;
                        case 2 :
                            if (current < vehicleCount - 1) {
                                ++current;
                            } else {
                                System.out.println("Invalid choice\n");
                            }
                            break;
                        case 3 :
                            if (vehicles.get(current).sell(user)) {
                                System.out.printf("Vehicle purchased by %s\n", user.getUsername());
                                choice = 4;
                            } else {
                                System.out.println("Unable to purchase vehicle. Please try again later");
                            }
                            break;
                        default :
                            System.out.println("Invalid choice\n");
                    }
                }
                choice = 0;
            }
        }
    }

    private LinkedList<Vehicle> filterVehicles() {
        int fieldChoice = 0;
        String field = null;
        while (fieldChoice == 0) {
            System.out.println("Select field to filter on");
            System.out.println("1.  Price          2. Year       3. Mileage");
            System.out.println("4.  Fuel Economy   5. Location   6. Model");
            System.out.println("7.  Make           8. Features   9. Size");
            System.out.println("10. Color         11. Engine    12. Country");
            System.out.println("13. Fuel Type     14. Type      15. VIN");
            System.out.print("Enter choice: ");
            fieldChoice = input.nextInt();

            // Move pointer to next line
            if (input.hasNextLine()) {input.nextLine();}

            switch (fieldChoice) {
                case 1:
                    field = "price";
                    break;
                case 2:
                    field = "year";
                    break;
                case 3:
                    field = "mileage";
                    break;
                case 4:
                    field = "fuel_economy";
                    break;
                case 5:
                    field = "location";
                    break;
                case 6:
                    field = "model";
                    break;
                case 7:
                    field = "make";
                    break;
                case 8:
                    field = "features";
                    break;
                case 9:
                    field = "size";
                    break;
                case 10:
                    field = "color";
                    break;
                case 11:
                    field = "engine";
                    break;
                case 12:
                    field = "country";
                    break;
                case 13:
                    field = "fuel_type";
                    break;
                case 14:
                    field = "type";
                    break;
                case 15:
                    field = "vin";
                    break;
                default:
                    System.out.println("Invalid choice\n");
                    fieldChoice = 0;
            }
        }
        System.out.printf("Enter value to search %s for: ", field);
        if (fieldChoice < 6) {
            int intCriteria = input.nextInt();

            // Move pointer to next line
            if (input.hasNextLine()) {input.nextLine();}

            return inventory.getFilteredVehicles(intCriteria, field);
        } else {
            String strCriteria = input.nextLine();
            return inventory.getFilteredVehicles(strCriteria, field);
        }
    }
}