package ro.damaris;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerApp {
    private static final int SERVER_PORT = 8080;
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static AtomicInteger clientCounter = new AtomicInteger(0);
    public static List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("Server pornit pe portul " + SERVER_PORT);
        System.out.println("Astept conexiuni de la clienti...\n");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                //aici accepta conexiuni noi de la clienti
                Socket clientSocket = serverSocket.accept();
                int id = clientCounter.incrementAndGet();

                //se creeaza un thread separat pentru fiecare client
                ClientHandler handler = new ClientHandler(clientSocket, id);
                clients.add(handler);
                executor.execute(handler);
            }
        } catch (IOException e) {
            System.err.println("Eroare server: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private int clientId;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, int id) {
        this.clientSocket = socket;
        this.clientId = id;
    }
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            out.println("END_OF_MESSAGE");
        }
    }
    public String getClientIP() {
        return clientSocket.getInetAddress().getHostAddress();
    }

    public int getClientPort() {
        return clientSocket.getPort();
    }

    @Override
    public void run() {
        try {
            //aici configureaza stream urile de input/output
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            //obține informatiile despre client
            String clientIP = clientSocket.getInetAddress().getHostAddress();
            int clientPort = clientSocket.getPort();
            String serverIP = clientSocket.getLocalAddress().getHostAddress();
            int serverPort = clientSocket.getLocalPort();

            System.out.println("Client" + clientId + " conectat de la " + clientIP + ":" + clientPort);

            //mesaj de bun venit
            out.println("Bun venit Client" + clientId + "! Esti conectat la server.");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("exit")) {
                    break;
                }

                //afișeaza mesaj de la client
                System.out.println("\n--- MESAJ PRIMIT ---");
                System.out.println("Mesaj client" + clientId + ": " + inputLine);
                System.out.println("Adresa IP sursa: " + clientIP);
                System.out.println("Adresa IP destinatie: " + serverIP);
                System.out.println("Port sursa: " + clientPort);
                System.out.println("Port destinatie: " + serverPort );
                System.out.println("--------------------\n");

                //raspunsul pentru client
                for (ClientHandler client : ServerApp.clients) {
                    String response = String.format(
                            "Mesaj de la Client%d: %s\n" + "Adresa IP sursa: %s\n" + "Adresa IP destinatie: %s\n" + "Port sursa: %d\n" + "Port destinatie: %d",
                            clientId, inputLine, serverIP, client.getClientIP(), serverPort, client.getClientPort()
                    );

                    client.sendMessage(response);
                }
            }

            System.out.println("Client" + clientId + " deconectat.");

        } catch (IOException e) {
            System.err.println("Eroare cu client" + clientId + ": " + e.getMessage());
        } finally {

            try {
                ServerApp.clients.remove(this);
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
