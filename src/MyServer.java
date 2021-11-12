import java.io.*;
import java.net.*;

public class MyServer {
    private int portNumber;

    public void sendMessage(Packet packet, ObjectOutputStream out) {
        try {
            out.writeObject(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Packet receiveMessage(ObjectInputStream in) {
        try {
            return (Packet) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean authenticateIP(Socket clientSocket, ServerSocket serverSocket, 
                                    Packet packet) {
        return (serverSocket.getInetAddress() == packet.getDestinationIP() 
        && clientSocket.getInetAddress() == packet.getSourceIP());
    }

    public void communicate() throws IOException, ClassNotFoundException{
        try (
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        ObjectOutputStream out =
            new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        ) {
            System.out.println("Server listening for client connections on port " + portNumber);
            Packet rcvdPacket;

            // Initiate conversation with client
            while ((rcvdPacket = (Packet) in.readObject()) != null) {
                if(authenticateIP(clientSocket, serverSocket, rcvdPacket)) {
                    // decide action based on message from client
                    Packet sendPacket = null;
                    switch(rcvdPacket.getMessage()) {
                        case "PING":
                            sendPacket = new RequestPacket(
                                rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "AUTH");
                        case "YES":
                            sendPacket = new ResponsePacket(
                                rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "DONE");
                    }
                    if (sendPacket != null) {
                        out.writeObject(sendPacket);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
