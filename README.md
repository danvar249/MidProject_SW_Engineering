# FinalProjectEkt
This time for real doe...

Contains bare-boned version of server and client.
Current capabilities:

- Server creates a ServerSocket on port 5555.
- Server connects to MySQL database (root username, locally stored)
- Server listens for clients
- Server can disconnect and reconnect "seamlessly"
- Server can be safely closed using the exit button or the X button (with an asterisk* -> open connections to client not yet handled properly)

- Client can connect to a server over port 5555 and an address (user inserted, defaults to localhost)
- Client can close the program using the "exit" button (X button is not properly mapped yet)
- Client loads an (empty) login page for the Ekt system after connecting to server

**Update - 25/12 Merry Christmas!**

Added (by me or others):
- Map[ServerClientRequestTypes -> IServerSideFunction]: a map from a request-type to an operation, used server-side to map client message to a function without a switch statement.
- Similar map from database-operation to the operation itself. [Types: DatabaseOperation, IDatabaseAction]
- Many UI pages for different stuff.

Current functionality of this system (my branch):
- Server can be started and connected to database. Server can be re-started in an orgranized fashion.
- Client can connect to server. (TODO: add a special login for "Touch simulation", ...)
- Client has a login page where data is compared, currently with columns in "systemuser" table which is too large.
- Client can connect as Customer, and view an example page.

TODO:
Plenty, TBD.
Seriously though, 
