import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerThread extends Thread {

    private Socket socket = null;

    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
        try{
            socket.setSoTimeout(10 * 1000);
        } catch (SocketException e) {
            System.out.println("Connection failed due to a corrupted socket. Please try again.");
        }
    }

    public void sendMessage(Packet packet, ObjectOutputStream out) throws IOException {
        out.writeObject(packet);
    }

    public Packet receiveMessage(ObjectInputStream in) throws IOException, ClassNotFoundException {
        return (Packet) in.readObject();
    }

    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Scanner sc = new Scanner(System.in);) {
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
                case "PING":
                    sendPacket = new RequestPacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "AUTH");
                    break;
                case "YES":
                    sendPacket = new ResponsePacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "DONE");
                    break;
                default:
                    System.out.println("Enter response: ");
                    String response = sc.nextLine();
                    sendPacket = new ResponsePacket(rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), response);
                    break;
                }
                if (sendPacket != null) {
                    System.out.println("Sending message to client: " + sendPacket.getMessage());
                    sendMessage(sendPacket, out);
                }
            }
            socket.close();
        } 
        catch (SocketTimeoutException e) {
            System.out.println("Connection timed out because the client did not communicate any data. Please try again.");
        }
        catch (EOFException e) {
            System.out.println("Client disconnected.");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("The data is corrupted. Please try connecting again.");
        }
    }

}
