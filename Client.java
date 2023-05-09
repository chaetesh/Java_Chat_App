import java.net.*;
import java.util.Scanner;
import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    BufferedWriter out;
    String clientName;

    JLabel heading = new JLabel("Client Area");
    JTextArea messagArea = new JTextArea();
    JTextField messageInput = new JTextField();
    Font font = new Font("Roboto", Font.PLAIN, 20);

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

            createGUI();
            handleEvents();

            startReading();
            // startWriting();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    public void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // 10 is keycode for enter
                if (e.getKeyCode() == 10) {
                    String sendMessage = messageInput.getText();
                    messagArea.append("Me: " + sendMessage + "\n");
                    try {
                        out.write(clientName + ": " + sendMessage);
                        out.newLine();
                        out.flush();
                        messageInput.setText("");
                        messageInput.requestFocus();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        });
    }

    public void createGUI() {
        // GUI
        this.setTitle("Client Messenger");
        this.setSize(550, 600);
        this.setLocationRelativeTo(null); // Sets window location to middle
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Component
        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);
        ImageIcon icon = new ImageIcon("chat.png");
        Image scaleImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT); // resizing image
        heading.setIcon(new ImageIcon(scaleImage));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // frame layout
        this.setLayout(new BorderLayout());

        // adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        this.add(messagArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    // Checking for messages from client and updating it
    public void startReading() {
        Runnable r1 = () -> {

            System.out.println("Reading Started");
            String msgFromGroupChat;

            while (!socket.isClosed()) {
                try {
                    msgFromGroupChat = br.readLine();
                    // System.out.println(msgFromGroupChat);
                    messagArea.append(msgFromGroupChat + "\n");
                } catch (IOException e) {
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
            } catch (Exception e) {
                closeEverything(socket, br, out);
            }
        };

        new Thread(r2).start();
    }

    public void closeEverything(Socket socket, BufferedReader br, BufferedWriter out) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (br != null) {
                br.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        Client c = new Client();
    }
}
