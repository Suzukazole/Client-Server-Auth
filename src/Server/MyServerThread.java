package Server;
import java.io.*;
import java.net.*;
import javax.swing.*;

import Packet.Packet;
import Packet.RequestPacket;
import Packet.ResponsePacket;

public class MyServerThread extends Thread {

    private Socket socket = null; // socket to connect to

    // constructor
    public MyServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
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
            JOptionPane.showMessageDialog(null, "Client connected from " + clientIP);
            System.out.println("Client connected from " + clientIP);
            Packet rcvdPacket;

            // Initiate conversation with client
            while ((rcvdPacket = receiveMessage(in)) != null) {
                // decide action based on message from client
                Packet sendPacket = null;
                String message = rcvdPacket.getMessage();
                if (!(message.equals("PING") || message.equals("YES") || message.equals("NO"))){
                    JOptionPane.showMessageDialog(null, "Message received from client " + clientIP + ": " + message);
                } else{
                    System.out.println("Message received from client " + clientIP + ": " + message);
                }
                switch (message) {
                // authenticate client
                case "PING":
                    sendPacket = new RequestPacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "AUTH");
                    break;
                case "YES":
                    sendPacket = new ResponsePacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "DONE");
                    break;
                case "NO":
                    sendPacket = new ResponsePacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "FAIL");
                    sendMessage(sendPacket, out);
                    JOptionPane.showMessageDialog(null, "Client not authenticated. Closing connection...");
                    // close connection
                    socket.close();
                    break;
                // messaging after client has been authenticated
                default:
                    String response = JOptionPane.showInputDialog("Enter response: ");
                    sendPacket = new ResponsePacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), response);
                    break;
                }
                // send message to client
                if (sendPacket != null && !(message.equals("PING") || message.equals("YES") || message.equals("NO"))) {
                    JOptionPane.showMessageDialog(null, "Sending message to client " + clientIP + " :" + sendPacket.getMessage());
                    sendMessage(sendPacket, out);
                } else{
                    System.out.println("Sending message to client: " + sendPacket.getMessage());
                    sendMessage(sendPacket, out);
                }
            }
            socket.close();
        } catch (SocketException e){
            JOptionPane.showMessageDialog(null, "Client disconnected.");
        }
        catch (SocketTimeoutException e) {
            JOptionPane.showMessageDialog(null, 
                    "Connection timed out because the client did not communicate any data. Please try connecting again.");
        } catch (EOFException e) {
            JOptionPane.showMessageDialog(null, "Client disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "The data is corrupted. Please try connecting again.");
        }
    }

}
