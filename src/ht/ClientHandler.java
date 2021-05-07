package ht;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(),true);
    }

    @Override
    public void run() {
        String clientName = null;
        try {
            while (true) {
                int firstSpace = -1;
                String request = in.readLine();
                if (request.startsWith("name")) {
                    firstSpace = request.indexOf(" ");
                    if (firstSpace != -1) {
                        clientName = request.substring(firstSpace+1);
                        out.println("[SERVER] says: Your name is: " + clientName);
                        out.println(">");
                        outToAll(clientName + " joined the conversation" );
                        out.println(">");
                        out.println("[SERVER] says: " + clientName + ", say Hello to the others");
                    }
                } else {
                    outToAll(clientName +" says: " + request.substring(firstSpace+1));
                }
                }
            } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void outToAll(String msg) {
        for (ClientHandler aClient : clients) {
            aClient.out.println(msg);
        }
    }
}
