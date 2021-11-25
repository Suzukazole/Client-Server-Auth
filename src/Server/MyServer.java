package Server;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class MyServer {
    private int portNumber; // port number to connect to
    private ArrayList<MyServerThread> clients; // list of clients connected to the server

    // constructor
    public MyServer(int portNumber) {
        this.portNumber = portNumber;
        this.clients = new ArrayList<MyServerThread>();
    }

    // start the server
    public void start() throws IOException, ClassNotFoundException {
        boolean listening = true;
        while (listening) {
            System.out.println("Server listening for client connections on port " + portNumber);
            JOptionPane.showMessageDialog(null, "Server listening for client connections on port " + portNumber);
            try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
                while (true) {
                    MyServerThread client = new MyServerThread(serverSocket.accept());
                    client.start(); // start the thread
                    clients.add(client); // add the client to the list of clients
                    // remove closed clients from the list of clients
                    clients.removeIf(c -> !c.isAlive());
                    // wait for all clients to disconnect
                    for (MyServerThread c : clients) {
                        c.join();
                    }
                    System.out.println("Server listening for client connections on port " + portNumber);
                    JOptionPane.showMessageDialog(null, "Server listening for client connections on port " + portNumber);
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected.");
                JOptionPane.showMessageDialog(null, "Client disconnected.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Exception caught when trying to listen on port " + portNumber
                        + " or listening for a connection");
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Server interrupted unexpectedly. Please try again later.");
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        boolean flag = false;
        int portNumber = 8080;
        while (!flag) {
            try{
                portNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter the port number on which the server is listening for connections: "));
                flag = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid port number.");
            }
        }
        new MyServer(portNumber).start();
    }
}
