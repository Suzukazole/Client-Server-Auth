import java.net.*;

import java.io.*;

public class MyClient extends Client{
 
    public MyClient(int portNumber, String hostName) {
        super(portNumber, hostName);
    }

    public void authenticate() throws UnknownHostException, IOException, ClassNotFoundException{
        try (
            Socket clientSocket = new Socket(hostName, portNumber);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        ) {
            System.out.println("Client communicating through port:" + portNumber);
            Packet rcvdPacket = null;
            Packet sendPacket = null;
        
            // initiate conversation with server
            System.out.println("Client: sending packet to server " + InetAddress.getLocalHost().getHostAddress());
            if (rcvdPacket == null) {
                clientSocket.getInetAddress();
                sendPacket = new RequestPacket(
                    getLocalIP(), InetAddress.getLocalHost(), "PING");
            }

            // continue conversation with server
            while ((rcvdPacket = (Packet) in.readObject()) != null) {
                if (rcvdPacket.getMessage().equals("AUTH")) {
                    sendPacket = new RequestPacket(
                        rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "YES");
                }
                if (rcvdPacket.getMessage().equals("DONE")) {
                    System.out.println("Client authenticated");
                    break;
                }
                if (sendPacket != null) {
                    out.writeObject(sendPacket);
                }
            }
 
            } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}