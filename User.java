public class User {
    private String username;    // username
    private boolean admin;      // true if user is admin

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
