package Packet;
import java.net.InetAddress;

public class RequestPacket extends Packet {

    public RequestPacket(InetAddress sourceIP, InetAddress destinationIP, String message) {
        super(sourceIP, destinationIP, message);
    }

}
