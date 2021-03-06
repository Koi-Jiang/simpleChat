// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
    Object ID = client.getInfo("ID");
    String mes = msg.toString().trim();
    if (mes.startsWith("#login"))
    {
      if (ID == null) //first line is #login+ID
      {
        int loginID = Integer.parseInt(mes.replace("#login", "").trim());
        client.setInfo("ID", loginID);
      } 
      else //other line is #login+ID
      {
        try 
        {
          client.sendToClient("You are not allowed to send ID again.");
          client.close();
        } catch (Exception e) {}
      }
    }
    else 
    {
      if (ID == null) //first line is not #login+ID
      {
        try {
          client.sendToClient("Your need your loginID to get connection.");
          client.close();
        } catch (Exception e) {}
      }
      else  //everything going well
      {
        System.out.println("Message received: " + msg + " from " + client + "whose loginID is " + ID);
        this.sendToAllClients("loginID: " + ID + " > " + msg);
      }
    }    
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try {
      port = Integer.parseInt(args[0]); //Get port from command line
    } catch (Throwable t) {
      port = DEFAULT_PORT; //Set port to 5555
    }

    EchoServer sv = new EchoServer(port);
    ServerConsole serverConsole = new ServerConsole(sv);
    try {
      sv.listen(); //Start listening for connections
    } catch (Exception ex) {
      System.out.println("The loginID is incorrect.");
    }
    serverConsole.accept();
  }

  public void clientConnected(ConnectionToClient client) 
  {
    System.out.println("A client has connected.");
  }

  public void clientException(ConnectionToClient client, Throwable exception)
  {
    System.out.println("A client has disconnectted.");
  }

}
//End of EchoServer class
