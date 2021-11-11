import java.io.*;
import java.net.*;

public abstract class Client {
    private int portNumber;
    private String hostName;

    private InetAddress getLocalIP(){
        try (Socket socket = new Socket();) {
            socket.connect(new InetSocketAddress("google.com", 80));
            return socket.getLocalAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessage(Packet p, ObjectOutputStream out) {
        try {
            out.writeObject(p);
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

    public void communicate(){
        
    }
}
