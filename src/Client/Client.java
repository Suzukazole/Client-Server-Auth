package Client;
import java.io.*;
import java.net.*;

import Packet.Packet;

public abstract class Client {

    protected int portNumber; // port number to connect to
    protected String hostName; // host name to connect to

    // constructor
    public Client(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
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
    public abstract void communicate() throws UnknownHostException, IOException, ClassNotFoundException;

}
