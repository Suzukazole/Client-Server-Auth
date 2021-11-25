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