package Packet;
import java.net.InetAddress;

public class ResponsePacket extends Packet {

    public ResponsePacket(InetAddress sourceIP, InetAddress destinationIP, String message) {
        super(sourceIP, destinationIP, message);
    }

}
