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
            PrintWriter outWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            System.out.println("Client communicating through port:" + portNumber);
            Packet rcvdPacket = new ResponsePacket(null, null, "");
            Packet sendPacket = null;
        
            // initiate conversation with server
            if (rcvdPacket.getMessage().equals("")) {
                System.out.println("Client: sending packet to server " + InetAddress.getLocalHost().getHostAddress());
                sendPacket = new RequestPacket(
                    getLocalIP(), InetAddress.getLocalHost(), "PING");
                System.out.println("Sending message to server: " + sendPacket.getMessage());
                // send packet to server
                out.writeObject(sendPacket);
            }

            // continue conversation with server
            while ((rcvdPacket = (Packet) in.readObject()) != null) {
                // decide action based on message from client
                String message = rcvdPacket.getMessage();
                System.out.println("Message received from server: " + message);
                if (message.equals("AUTH")) {
                    sendPacket = new RequestPacket(
                        rcvdPacket.getDestinationIP(), rcvdPacket.getSourceIP(), "YES");
                    System.out.println("Sending message to server: " + sendPacket.getMessage());
                }
                if (message.equals("DONE")) {
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

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        MyClient client = new MyClient(8080, "172.17.74.82");
        client.authenticate();
    }

}