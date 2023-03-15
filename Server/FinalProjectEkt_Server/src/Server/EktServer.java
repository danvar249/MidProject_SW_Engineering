package Server;

import java.io.IOException;

import common.SCCP;
import common.ServerClientRequestTypes;
import database.DatabaseController;
import database.DatabaseSimpleOperation;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EktServer extends AbstractServer 
{


  public EktServer(int port) 
  {
	  // create the server
    super(port);
    // create a database connection
    initDBConnector();
    
  }
  
  private void initDBConnector() {
		DatabaseController.getInstance();
  }

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   * @param 
   */
  public void handleMessageFromClient  (Object msg, ConnectionToClient client)
  {
	  // TODO: this
	  if(msg instanceof SCCP) {
		  
		  System.out.println("Server got message from client (" +client+"): "+(SCCP)msg);
		  // now, the magic:
		  SCCP response = ServerMessageHandler.getMap().get(((SCCP)msg).getRequestType()).handleMessage((SCCP)msg);
		  try {
			  // send to client
			client.sendToClient(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  else {
		  // error
		  System.out.println("ERROR! Server got message from " + client +
				  " not of type SCCP! message: "+ msg +" type of message: "+ (msg.getClass()));
		  // Rotem @ 11.1 -> added crash response in this case:
			try {
				client.sendToClient(new SCCP(ServerClientRequestTypes.CRASH, "Invalid communications protocol"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
  }
   
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	  System.out.println ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()  {
	  DatabaseController.closeDBController();
	  System.out.println ("Server has stopped listening for connections.");
  }  
  
  protected void handleForcedShutdown() {
	  System.out.println("Logging off all connected users!");
	  String query = "DELETE FROM "+DatabaseController.getSchemaName()+".logged_users WHERE username!=\"\";";
	  System.out.println("Executing query="+query);
	  
	  	// remove logged users from the logged_users table
	  if(DatabaseSimpleOperation.executeQuery(query)) {
		  System.out.println("Executed query successfully!");
	  }
	  else {
		  System.out.println("Could not find users to remove - verify database contents manually!");
	  }
		// handle open connections
		for(Thread c : ServerUI.getEktServerObject().getClientConnections()) {
			ConnectionToClient cConn = (ConnectionToClient)c;
			// do stuff with the connection (send message? forced shutdown? TODO: as of now it's "CRASH")
			try {
				cConn.sendToClient(new SCCP(ServerClientRequestTypes.CRASH, "Server has been forcefully shut down."));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		// close the server (closing the DB is done inside serverStopped() )
		try {
			this.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
}
