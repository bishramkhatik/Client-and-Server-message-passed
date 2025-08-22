import java.io.*;
import java.net.*;

public class UDPServer {
    private static Socket client1Socket = null;
    private static Socket client2Socket = null;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started. Waiting for clients...");

            client1Socket = serverSocket.accept();
            System.out.println("Client 1 connected.");
            client2Socket = serverSocket.accept();
            System.out.println("Client 2 connected.");

            Thread client1Handler = new Thread(() -> handleClient(client1Socket, client2Socket));
            Thread client2Handler = new Thread(() -> handleClient(client2Socket, client1Socket));

            client1Handler.start();
            client2Handler.start();

            client1Handler.join();
            client2Handler.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, Socket otherClientSocket) {
        try (
                BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter outputToOtherClient = new PrintWriter(otherClientSocket.getOutputStream(), true);
        ) {
            String message;
            while ((message = inputFromClient.readLine()) != null) {
                System.out.println("Received: " + message);
                outputToOtherClient.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
