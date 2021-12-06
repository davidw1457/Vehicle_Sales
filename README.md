# Vehicle_Sales

The program is written and tested in Java 13 and MySQL 8.0.26 on Windows 10 and 11, using IntelliJ IDEA IDE.

The backup of the sample sql database is included in vehicle_inventory.sql. Restore to vehicle_inventory using
mysqladmin -u root -p create vehicle_inventory
mysql -u root -p vehilce_inventory < vehilce_inventory.sql

The program expects the sql database to be running on the localhost at port 3306. You will also need to create
anonymous users with select, update, and insert permissions with the following sql commnads:
CREATE USER ''@'localhost';
GRANT SELECT on vehicle_inventory.* to ''@'localhost';
GRANT INSERT on vehicle_inventory.* to ''@'localhost';
GRANT UPDATE on vehicle_inventory.* to ''@'localhost';

When run, the user will be requried to login. Two default users are created for testing:
 username    | password
-------------+----------
defaultuser  | p@$$w0rd1
defaultadmin | p@$$w0rd2

The default user will have access to browse vehicles (and purchase if desired), access title history, and sell a vehicle
into the system.
The admin usesr will have the same access and also be able to add users, view users, activate or deactivate users, edit 
existing vehicles, and update the title history for vehicles.

There is also a test.txt file included which can be used for automated testing. It will simulate an attempt to login
with an invalid username and password, then login as a standard user, browse vehicles, purchase a vehicle, view a title
history, sell a vehicle into the system, and then logout. Then it will login as an admin, edit a vehicle, update a title
history, list all users, add a user, deactivate and activate a user, logout, and quit the program.
