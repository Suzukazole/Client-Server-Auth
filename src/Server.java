import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private int portNumber; // port number to connect to
    private ArrayList<ServerThread> clients; // list of clients connected to the server

    // constructor
    public Server(int portNumber) {
        this.portNumber = portNumber;
        this.clients = new ArrayList<ServerThread>();
    }

    // start the server
    public void start(Scanner scanner) throws IOException, ClassNotFoundException {
        boolean listening = true;
        while (listening) {
            System.out.println("Server listening for client connections on port " + portNumber);
            try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
                while (true) {
                    ServerThread client = new ServerThread(serverSocket.accept(), scanner);
                    client.start(); // start the thread
                    clients.add(client); // add the client to the list of clients
                    // remove closed clients from the list of clients
                    clients.removeIf(c -> !c.isAlive());
                    // wait for all clients to disconnect
                    for (ServerThread c : clients) {
                        c.join();
                    }
                    System.out.println("Server listening for client connections on port " + portNumber);
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected.");
                System.out.println();
                continue;
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber
                        + " or listening for a connection");
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("Server interrupted unexpectedly. Please try again later.");
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Enter the port number on which the server should listen for connections:");
        Scanner scanner = new Scanner(System.in);
        int portNumber = scanner.nextInt();
        scanner.nextLine();
        new Server(portNumber).start(scanner);
        scanner.close();
    }
}
