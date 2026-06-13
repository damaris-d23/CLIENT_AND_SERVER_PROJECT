package ro.damaris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    private String serverIP;
    private int serverPort;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;

    public ClientApp(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        try {
            //conectare la server
            System.out.println("Conectare la server " + serverIP + ":" + serverPort);
            socket = new Socket(serverIP, serverPort);

            //aici configureaza stream urile
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            //primește mesajul de bun venit
            String welcomeMessage = in.readLine();
            System.out.println("Server: " + welcomeMessage);
            System.out.println("\nPoti trimite mesaje catre server.");
            System.out.println("Scrie 'exit' pentru a inchide conexiunea.\n");

            Thread listenerThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println("\n--- RASPUNS DE LA SERVER ---");
                        while (response != null && !response.equals("END_OF_MESSAGE")) {
                            System.out.println(response);
                            response = in.readLine();
                        }
                        System.out.println("----------------------------\n");
                        System.out.print("Introdu mesaj: ");
                    }
                } catch (IOException e) {
                    System.out.println("\nConexiunea cu serverul s-a inchis.");
                }
            });
            listenerThread.start();

            //afiseaza promptul initial o singura data
            System.out.print("Introdu mesaj: ");

            //thread pentru citirea input ului de la utilizator
            String userInput;
            while (true) {
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    out.println("exit");
                    break;
                }

                //trimite mesajul la server
                out.println(userInput);
            }

            System.out.println("Deconectat de la server.");

        } catch (IOException e) {
            System.err.println("Eroare client: " + e.getMessage());
        } finally {
            cleanup();
        }
    }
    //inchidem toate fluxurile si conexiunile
    private void cleanup() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            if (scanner != null) scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //verifica argumentele din linia de comanda
        String serverIP = "localhost"; //valoare implicita
        int serverPort = 8080; //valoare implicita

        if (args.length >= 2) {
            serverIP = args[0];
            try {
                serverPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Port invalid. Folosesc portul implicit 8080.");
            }
        } else {
            System.out.println("Folosire: java ClientApp <server_ip> <server_port>");
            System.out.println("Folosesc valori implicite: localhost:8080\n");
        }

        ClientApp client = new ClientApp(serverIP, serverPort);
        client.start();
    }
}
