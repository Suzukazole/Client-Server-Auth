import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private int portNumber;

    public Server(int portNumber) {
        this.portNumber = portNumber;
    }

    public void start() throws IOException, ClassNotFoundException {
        boolean listening = true;
        while (listening) {
            System.out.println("Server listening for client connections on port " + portNumber);
            try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
                while (true) {
                    new ServerThread(serverSocket.accept()).start();
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected.");
                System.out.println();
                continue;
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber
                        + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Enter the port number on which the server should listen for connections:");
        Scanner scanner = new Scanner(System.in);
        int portNumber = scanner.nextInt();
        new Server(portNumber).start();
        scanner.close();
    }
}
