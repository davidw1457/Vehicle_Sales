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

public class User {
    private final String username;    // username
    private final boolean admin;      // true if user is admin

    /***************************************************************************
     * User
     * Constructor for User object
     * @param username  username
     * @param admin     true if user is admin
     **************************************************************************/
    public User(String username, boolean admin) {
        this.username = username;
        this.admin = admin;
    }

    /***************************************************************************
     * isAdmin
     * Check if user is admin
     * @return  boolean true if user is admin, false otherwise
     **************************************************************************/
    public boolean isAdmin() {
        return admin;
    }

    /***************************************************************************
     * getUsername
     * Get username for User
     * @return String username
     **************************************************************************/
    public String getUsername() {
        return username;
    }
}
