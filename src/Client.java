import java.io.*;
import java.net.*;

public abstract class Client {
    protected int portNumber;
    protected String hostName;

    public Client(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    public abstract void authenticate() throws UnknownHostException, IOException, ClassNotFoundException;

    protected InetAddress getLocalIP() {
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

    // public void communicate() throws IOException, ClassNotFoundException {
    //     try(
    //         Socket clientSocket = new Socket(hostName, portNumber);
    //         ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
    //         ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
    //     ){
    //         System.out.println("Client communicating through port:" + portNumber);
    //         Packet rcvdPacket, sendPacket = null;

    //     } catch (IOException e) {
    //         System.out.println("Exception caught when trying to listen on port "
    //             + portNumber + " or listening for a connection");
    //         System.out.println(e.getMessage());
    //     }

    // }      
 }
