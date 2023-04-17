import java.net.*;
import java.io.*;

class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Server() {
        try {
            // creating server in port 7777
            server = new ServerSocket(7777);
            System.out.println("Waiting for Connection.........");
            // Accepts request from client
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Checking for messages from client and updating it
    public void startReading() {
        Runnable r1 = () -> {

            System.out.println("Reading Started");

            try {
                String msg;
                while (true) {
                    msg = br.readLine();
                    if (msg.equals("quit")) {
                        System.out.println("Client Exited the Chat!!");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);
                }
            } catch (IOException e) {
                // e.printStackTrace();
                System.out.println("Connection Closed");
            }
        };

        new Thread(r1).start();
    }

    // Takes data from user and sends to client
    public void startWriting() {
        Runnable r2 = () -> {
            try {
                while (true && !socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("quit")) {
                        socket.close();
                        break;
                    }
                }
            }
             catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        Server s = new Server();
    }
}
