package com.mycompany.scrabble_project.scrabble_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Each new socket connection will have its own client socket and gameState
 * which represents the board, and the logic for interpreting a client's message
 * to the server.
 *
 * @author Daniel
 */
public class EchoProtocol implements Runnable {

    static public final int BUFSIZE = 23;

    private Socket clientSocket;
    private ServerLogic gameState;

    /**
     * Constructor which sets the client socket and initializes the gameState
     * for a specific client.
     *
     * @param clntSock The client socket
     */
    public EchoProtocol(Socket clntSock) {
        this.clientSocket = clntSock;
        this.gameState = new ServerLogic();
    }

    /**
     * Continuously read a client's message and respond with the appropriate
     * message until the client closes the connection or no longer needs to send
     * messages.
     */
    @Override
    public void run() {
        try {
            // Get the input and output I/O streams from socket
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            byte[] clientMessage = new byte[BUFSIZE];

            //continue reading until the client stops sending bytes
            while ((in.read(clientMessage)) != -1) {
                //interpret the client's message and respond depending on if it was a GameStart message or play by client
                clientMessage = gameState.validateClientMessage(clientMessage);

                //write the response message back to the client
                out.write(clientMessage);
            }

        } catch (IOException e) {
            System.out.println("Exception = " + e.getMessage());
        }

        try // Close socket once we are done with this client
        {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Exception = " + e.getMessage());
        }
    }
}
