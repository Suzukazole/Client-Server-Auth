import java.io.*;
import java.net.*;

public class MyServer {
    private int portNumber;

    public MyServer(int portNumber) {
        this.portNumber = portNumber;
    }

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

    public void authenticate() throws IOException, ClassNotFoundException{
        System.out.println("Server listening for client connections on port " + portNumber);
        try (
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        ObjectOutputStream out =
            new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        PrintWriter outWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        ) { 
            System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
            Packet rcvdPacket;

            // Initiate conversation with client
            while ((rcvdPacket = (Packet) in.readObject()) != null) {
                    // decide action based on message from client
                    Packet sendPacket = null;
                    String message = rcvdPacket.getMessage();
                    System.out.println("Message received from client: " + message);
                    switch(message) {
                        case "PING":
                            sendPacket = new RequestPacket(
                                rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "AUTH");
                        case "YES":
                            sendPacket = new ResponsePacket(
                                rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "DONE");
                    }
                    if (sendPacket != null) {
                        System.out.println("Sending message to client: " + sendPacket.getMessage());
                        out.writeObject(sendPacket);
                    }
                }
            } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }  

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        MyServer server = new MyServer(8080);
        server.authenticate();
    }
}
