import java.io.*;
import java.net.*;

public abstract class Packet implements Serializable {
    private InetAddress sourceIP; // IP address of the source
    private InetAddress destinationIP; // IP address of the destination
    private String message; // message to be sent

    // constructor
    Packet(InetAddress sourceIP, InetAddress destinationIP, String message) {
        this.sourceIP = sourceIP;
        this.destinationIP = destinationIP;
        this.message = message;
    }

    // getters
    public InetAddress getSourceIP() {
        return this.sourceIP;
    }

    public InetAddress getDestinationIP() {
        return this.destinationIP;
    }

    public String getMessage() {
        return this.message;
    }
}
