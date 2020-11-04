// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
	ChatIF clientUI;
	int loginID;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(int loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
		this.clientUI = clientUI;
		this.loginID = loginID;
		openConnection();
		sendToServer("#login " + loginID);
  }
  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if (message.trim().charAt(0) == '#')
    {
      try
      {
        String[] mes = message.split(" ");
        switch (mes[0]) 
        {
          case "#quit":
            quit();
            clientUI.display("Has quit.");
            break;
          case "#logoff":
            closeConnection();
            clientUI.display("Has logged off.");
            break;
          case "#sethost":
            if(isConnected())
            {
              clientUI.display("Client should log off first.");
              break;
            }
            setHost(mes[1]);
            clientUI.display("Host has been set to " + getHost() + ".");
            break;
          case "#setport":
            if(isConnected())
            {
              clientUI.display("Client should log off first.");
              break;
            }
            setPort(Integer.parseInt(mes[1]));
            clientUI.display("Port has been set to " + getPort() + ".");
            break;
					case "#login":
            if(isConnected())
            {
              clientUI.display("Client should disconnect first.");
              break;
            }
            openConnection();
            clientUI.display("Has logged in.");
            break;
          case "#gethost":
            clientUI.display(getHost() + ".");
            break;
          case "#getport":
            clientUI.display(getPort() + ".");
						break;
					default:
						if (mes[0].startsWith("#login")) //diffent with command "#login"
						{
							try {
								sendToServer(message);
							} catch (IOException e) {
								clientUI.display("Could not send message to server.  Terminating client.");
								quit();
							}
						}
						else
            	clientUI.display("Command is incorrect.");
            break;
        }
      }catch (Exception e) 
      {
        clientUI.display("An exception has occurred. Terminating client.");
        quit();
      }    
    }
    else
    {
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display
          ("Could not send message to server.  Terminating client.");
        quit();
      }
    }    
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try {
      closeConnection();
    } catch (IOException e) {
    }
    System.exit(0);
  }
  
  public void connectionException(Exception exception) 
	{
		System.out.println("The server has shut down.");
		quit();
	}
	
	public void connectionClosed()
	{
		System.out.println("The server has shut down.");
		quit();
	}
}
//End of ChatClient class
