import java.net.*;
import java.io.*;
import javax.swing.*;

public class EvilClient extends Client {

    // constructor
    public EvilClient(int portNumber, String hostName) {
        super(portNumber, hostName);
    }

    // communicate with the server
    public void communicate() throws UnknownHostException, IOException, ClassNotFoundException {
        try (Socket clientSocket = new Socket(hostName, portNumber);
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());) {

            // If no data is transferred, close the client socket after 30 seconds
            clientSocket.setSoTimeout(30 * 1000);

            // authenticate with the server
            JOptionPane.showMessageDialog(null, "Client communicating through port:" + portNumber);
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
                    sendPacket = new RequestPacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "NO");
                    System.out.println("Sending message to server: " + sendPacket.getMessage());
                }
                if (message.equals("DONE")) {
                    JOptionPane.showMessageDialog(null, "Client authenticated");
                    System.out.println();
                    break;
                }
                if (sendPacket != null) {
                    sendMessage(sendPacket, out);
                }
            }
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Unknown host: " + hostName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Client timed out. Closing communications with server...");
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        String hostName = JOptionPane.showInputDialog(
                "Enter the IP address of the machine on which the server is running. If the server is running on the same machine, then type in 'localhost'.");
        boolean flag = false;
        int portNumber = 8080;
        while (!flag) {
            try {
                portNumber = Integer.parseInt(JOptionPane
                        .showInputDialog("Enter the port number on which the server is listening for connections: "));
                flag = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid port number.");
            }
        }

        EvilClient client = new EvilClient(portNumber, hostName);
        client.communicate();
    }
}