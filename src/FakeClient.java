import java.io.*;
import java.net.*;

public class FakeClient extends Client {

    public FakeClient(int portNumber, String hostName) {
        super(portNumber, hostName);
    }

    @Override
    public void authenticate() throws UnknownHostException, IOException, ClassNotFoundException {
        
    }
    
}
