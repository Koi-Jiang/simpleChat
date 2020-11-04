import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;


public class ServerConsole implements ChatIF 
{
    /**
     * The instance of the server that created this ServerConsole.
     */
    EchoServer echoServer;

    /**
     * Scanner to read from the console
     */
    Scanner fromConsole;

    /**
     * Constructs an instance of the ServerConsole.
     *
     * @param host The host to connect to.
     * @param port The port to connect on.
     */
    public ServerConsole(EchoServer echoServer)
    {
        this.echoServer = echoServer;
        // Create scanner object to read from console
        fromConsole = new Scanner(System.in);
    }
    
    /**
     * This method waits for input from the console.  Once it is 
     * received, it sends it to the server's message handler.
     */
    public void accept() 
    {
        try 
        {
            String message;
            while (true) 
            {
                message = fromConsole.nextLine();
                handleMessageFromClientUI(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    private void quit()
    {
        try 
        {
            echoServer.close();
        } catch (Exception e) {}
        System.exit(0);
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
                        display("Has quit.");
                        break;
                    case "#stop":
                        echoServer.stopListening();
                        break;
                    case "#close":
                        echoServer.close();
                        break;
                    case "#setport":
                        if (echoServer.isListening())
                        {
                            display("The server should close first.");
                            break;
                        }
                        echoServer.setPort(Integer.parseInt(mes[1]));
                        display("Port has been set to " + echoServer.getPort() + ".");
                        break;
                    case "#start":
                        echoServer.listen();
                        break;
                    case "#getport":
                        display(echoServer.getPort() + ".");
                        break;       
                    default:
                        display("Command is incorrect.");
                        break;
                    }
            }catch (Exception e) 
                {
                    display("An exception has occurred. Terminating client.");
                    quit();
                }    
        }
        else
        {
            try
            {
                echoServer.sendToAllClients("SERVER MSG> " + message);
                display(message);
            }
            catch(Exception e)
            {
                display("Could not send message to server.  Terminating client.");
                quit();
            }
        }    
    }

    /**
    * This method overrides the method in the ChatIF interface.  It
    * displays a message onto the screen.
    *
    * @param message The string to be displayed.
    */
  	@Override
    public void display(String message) 
    {
		System.out.println("SERVER MSG> " + message);		
	}
    
}
