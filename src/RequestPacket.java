import java.net.InetAddress;

public class RequestPacket extends Packet {

    RequestPacket(InetAddress sourceIP, InetAddress destinationIP, String message) {
        super(sourceIP, destinationIP, message);
    }

}
