import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {

    private Socket socket = null; // socket to connect to
    private Scanner scanner; // scanner to read user input

    // constructor
    public ServerThread(Socket socket, Scanner scanner) {
        super("ServerThread");
        this.socket = socket;
        this.scanner = scanner;
        try {
            // close the socket after 60 seconds of inactivity
            socket.setSoTimeout(60 * 1000);
        } catch (SocketException e) {
            System.out.println("Connection failed due to a corrupted socket. Please try again.");
        }
    }

    // getters
    public Socket getSocket() {
        return socket;
    }

    // send message to the client
    public void sendMessage(Packet packet, ObjectOutputStream out) throws IOException {
        out.writeObject(packet);
    }

    // receive message from the client
    public Packet receiveMessage(ObjectInputStream in) throws IOException, ClassNotFoundException {
        return (Packet) in.readObject();
    }

    // run method
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {
            // get client's IP address
            String clientIP = socket.getInetAddress().getHostAddress();
            System.out.println("Client connected from " + clientIP);
            Packet rcvdPacket;

            // Initiate conversation with client
            while ((rcvdPacket = receiveMessage(in)) != null) {
                // decide action based on message from client
                Packet sendPacket = null;
                String message = rcvdPacket.getMessage();
                System.out.println("Message received from client " + clientIP + ": " + message);
                switch (message) {
                // authenticate client
                case "PING":
                    sendPacket = new RequestPacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "AUTH");
                    break;
                case "YES":
                    sendPacket = new ResponsePacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "DONE");
                    break;
                case "NO":
                    System.out.println("Client not authenticated. Closing connection...");
                    // close connection
                    socket.close();
                    break;
                // messaging after client has been authenticated
                default:
                    System.out.println("Enter response: ");
                    String response = scanner.nextLine();
                    sendPacket = new ResponsePacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), response);
                    break;
                }
                // send message to client
                if (sendPacket != null) {
                    System.out.println("Sending message to client: " + sendPacket.getMessage());
                    sendMessage(sendPacket, out);
                }
            }
            socket.close();
        } catch (SocketTimeoutException e) {
            System.out.println(
                    "Connection timed out because the client did not communicate any data. Please try connecting again.");
            System.out.println();
        } catch (EOFException e) {
            System.out.println("Client disconnected.");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("The data is corrupted. Please try connecting again.");
        }
    }

}
