import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    Socket socket;
    BufferedReader br;
    BufferedWriter out;    
    String clientName;

    public Client() {
        try {
            System.out.println("Enter Username: ");
            Scanner sc = new Scanner(System.in);
            clientName = sc.nextLine(); 

            // Giving Ip address and port to establish the connection
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connected to Server");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

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
            String msgFromGroupChat;

            while (!socket.isClosed()) {
            try {
                    msgFromGroupChat = br.readLine();    
                    System.out.println(msgFromGroupChat);
                }
            catch (IOException e) {
                    closeEverything(socket, br, out);
                }
            } 
        };

        new Thread(r1).start();
    }

    // Takes data from user and sends to client
    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writing Started");
            try {
                out.write(clientName);
                out.newLine();
                out.flush();
                Scanner sc = new Scanner(System.in);

                while (!socket.isClosed()) {
                    String msgToSend = sc.nextLine();
                    out.write(clientName + ": " + msgToSend);
                    out.newLine();
                    out.flush();
                }
            } 
            catch (Exception e) {
                closeEverything(socket, br, out);
            }
        };

        new Thread(r2).start();
    }

    public void closeEverything(Socket socket,BufferedReader br,BufferedWriter out){
        try{
            if(socket != null){
                socket.close();
            }
            if(br != null){
                br.close();
            }
            if(out != null){
                out.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
    }
}
