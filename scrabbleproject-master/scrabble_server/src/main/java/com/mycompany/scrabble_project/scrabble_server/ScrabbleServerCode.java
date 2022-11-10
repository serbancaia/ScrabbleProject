package com.mycompany.scrabble_project.scrabble_server;

import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream

/**
 * Handles client connections and responds to message in a 23 byte format.
 *
 * @author Daniel
 */
public class ScrabbleServerCode {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;

    /**
     * Infinitely loops, accepting Socket connections from the client and acts
     * as a concurrent server which performs a thread-per-client where each new
     * thread is spawned to handle each new client
     *
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) // Test for correct # of args
        {
            throw new IllegalArgumentException("Parameter(s): <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        // Create a server socket to accept client connection requests
        serverSocket = new ServerSocket(servPort);

        //Display the IP Address at which the server is running
        System.out.println("Server running at IP Address : " + java.net.InetAddress.getLocalHost());

        // Run forever, accepting and spawning threads to service each connection
        for (;;) {
            try {
                clientSocket = serverSocket.accept();// Block waiting for connection

                SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
                System.out.println("Handling client at " + clientAddress);

                EchoProtocol protocol = new EchoProtocol(clientSocket);

                Thread thread = new Thread(protocol);
                thread.start();

                System.out.println("Created and started Thread = " + thread.getName());

            } catch (IOException e) {
                System.out.println("Exception = " + e.getMessage());
            }
        }
    }
}
