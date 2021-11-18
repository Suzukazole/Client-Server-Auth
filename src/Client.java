import java.io.*;
import java.net.*;

public abstract class Client {
    private int portNumber;
    private String hostName;

    private InetAddress getLocalIP() {
        try (Socket socket = new Socket();) {
            socket.connect(new InetSocketAddress("google.com", 80));
            return socket.getLocalAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessage(Packet packet, ObjectOutputStream out) {
        try {
            out.writeObject(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Packet receiveMessage(ObjectInputStream in) {
        try{
            return (Packet) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void communicate() throws IOException {
        try(
            Socket clientSocket = new Socket(hostName, portNumber);
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

        ){
            System.out.println("Client communicating through port:"+portNumber);
            Packet sendPacket; 
            Packet rcvdPacket;
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
