# Client Server Authentication

##  Build Instructions (zsh)
- To compile the source run

    ``` javac -sourcepath src -d build src/**/*.java```

- To start the Server application run 

    ``` java -cp ".:build:**/*.class" Server.MyServer ```

- To start the Client application run 

    ``` java -cp ".:build:**/*.class" Client.MyClient ```

##  Usage Instructions
 ### Opening the Server for connections and setting up the Client program
 - Run the Server on your machine first. (refer build instructions)
 - Once the Server interface opens, select the port number to listen for connections.
    Example: 8080.
- Now, run the Client jar file on a separate machine or on the same machine on a
different terminal instance.(refer build instructions)
- Once the Client interface is running, input the address of the server. To connect
to a remote machine, you will have to find the IP address first.
If the server is running locally, i.e on the same machine as the client simply type
“localhost”.
- After that, enter the port number of the server. This must be the same number
entered while initialising the server-side (8080, in this example).
- This will initiate the authentication process.

### Communication loop usage for Server and Client
-  Once the Authentication has been set up in the backend, messages can be sent to and
from the client and the server.
-  To initiate the communication loop, send a message to the Server from the client,
through the interface. Input 1 to begin the process.
-  Once a message has been sent, the server will receive the message through a packet
and it will be displayed on the server interface. At this point, the server can choose to
respond to the client with another message. This can be done directly through the server
interface.
-  The client can choose to continue in this loop or can close the connection by entering
any other number to close the connection to the Server. The client will close the
connection to the server if the delay period is exceeded. It is currently set to 30s.
-  When the server is done listening for connections kill the server process directly to exit the application.


### Setting up Multiple Clients
Since the server is a multithreaded application it can support multiple client connections at the
same time. Setting up multiple clients is no different. Just run the Client application on different
IP addresses and connect to the correct server address and port. The authentication and
communication loop happen in the same way.