import java.net.*;
import java.util.*;
import java.io.*;

public class Client {

    protected int portNumber; // port number to connect to
    protected String hostName; // host name to connect to
    private Scanner scanner; // scanner to read user input

    public Client(int portNumber, String hostName, Scanner scanner) {
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.scanner = scanner;
    }

    // get the IP address of the client
    protected InetAddress getLocalIP() {
        try (Socket socket = new Socket();) {
            socket.connect(new InetSocketAddress("google.com", 80));
            return socket.getLocalAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // send a packet to the server
    public void sendMessage(Packet packet, ObjectOutputStream out) {
        try {
            out.writeObject(packet);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // receive a packet from the server
    public Packet receiveMessage(ObjectInputStream in) {
        try {
            return (Packet) in.readObject();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    // communicate with the server
    public void communicate() throws UnknownHostException, IOException, ClassNotFoundException {
        try (Socket clientSocket = new Socket(hostName, portNumber);
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());) {

            // If no data is transferred, close the client socket after 30 seconds
            clientSocket.setSoTimeout(30 * 1000);

            // authenticate with the server
            System.out.println("Client communicating through port:" + portNumber);
            Packet rcvdPacket = new ResponsePacket(null, null, "");
            Packet sendPacket = null;

            // initiate conversation with server
            if (rcvdPacket.getMessage().equals("")) {
                System.out.println("Client: sending packet to server " + InetAddress.getLocalHost().getHostAddress());
                sendPacket = new RequestPacket(getLocalIP(), InetAddress.getLocalHost(), "PING");
                System.out.println("Sending message to server: " + sendPacket.getMessage());
                // send packet to server
                sendMessage(sendPacket, out);
            }

            // continue conversation with server
            while ((rcvdPacket = receiveMessage(in)) != null) {
                // decide action based on message from client
                String message = rcvdPacket.getMessage();
                System.out.println("Message received from server: " + message);
                if (message.equals("AUTH")) {
                    sendPacket = new RequestPacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "YES");
                    System.out.println("Sending message to server: " + sendPacket.getMessage());
                }
                if (message.equals("DONE")) {
                    System.out.println("Client authenticated");
                    System.out.println();
                    break;
                }
                if (sendPacket != null) {
                    sendMessage(sendPacket, out);
                }
            }
            /* communication with server */
            boolean comms = true; // flag to indicate if client is still communicating with server
            while (comms) {
                // check if user still wants to communicate with server
                System.out.println(
                        "Would you like to continue communicating with the server? If yes, please type 1. If no, enter any number to exit.");
                boolean flag = false;
                int choice = -1;
                while (!flag) {
                    try {
                        choice = scanner.nextInt();
                        scanner.nextLine();
                        flag = true;
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid choice: ");
                        scanner.nextLine();
                    }
                }
                if (choice == 1) {
                    // send message to server
                    System.out.println("Enter the message you wish to send to the server: ");
                    String message = scanner.nextLine();
                    sendPacket = new RequestPacket(getLocalIP(), InetAddress.getLocalHost(), message);
                    System.out.println("Sending message to server: " + sendPacket.getMessage());
                    sendMessage(sendPacket, out);
                    rcvdPacket = receiveMessage(in);
                    System.out.println("Message received from server: " + rcvdPacket.getMessage());
                } else {
                    System.out.println("Closing communications with server...");
                    comms = false;
                }

            }
            System.out.println();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + hostName);
        } catch (IOException e) {
            System.out.println(
                    "Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Client timed out. Closing communications with server...");
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "Enter the IP address of the machine on which the server is running. If the server is running on the same machine, then type in 'localhost'.");
        String hostName = sc.nextLine();
        System.out.println("Enter the port number on which the server is listening for connections: ");
        int portNumber = sc.nextInt();
        Client client = new Client(portNumber, hostName, sc);
        client.communicate();
        sc.close();
    }
}