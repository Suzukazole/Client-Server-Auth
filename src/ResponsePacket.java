import java.net.*;

public class ResponsePacket extends Packet {

    ResponsePacket(InetAddress sourceIP, InetAddress destinationIP, String message) {
        super(sourceIP, destinationIP, message);
    }

}
