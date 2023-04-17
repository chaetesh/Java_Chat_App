import java.net.*;
import java.io.*;

public class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending Request to Server");
            // Giving Ip address and port to establish the connection
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connected to Server");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            // TODO: handle exception
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
                        System.out.println("Server Exited the Chat!!");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            } 
            catch (IOException e) {
                // e.printStackTrace();
                System.out.println("Connection Closed");
            }
        };

        new Thread(r1).start();
    }

    // Takes data from user and sends to client
    public void startWriting() {
        Runnable r2 = () -> {
            String content = "";
            try {
                while (true && !socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("quit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("here");
                // TODO: handle exception
                e.printStackTrace();
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        Client c = new Client();
    }
}
