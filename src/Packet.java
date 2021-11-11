import java.io.*;
import java.net.*;

public abstract class Packet implements Serializable {
    private InetAddress sourceIP;
    private InetAddress destinationIP;
    private String message;

    Packet(InetAddress sourceIP, InetAddress destinationIP, String message){
        this.sourceIP = sourceIP;
        this.destinationIP = destinationIP;
        this.message = message;
    }

    public InetAddress getSourceIP(){
        return this.sourceIP;
    }

    public InetAddress getDestinationIP(){
        return this.destinationIP;
    }

    public String getMessage(){
        return this.message;
    }
}
