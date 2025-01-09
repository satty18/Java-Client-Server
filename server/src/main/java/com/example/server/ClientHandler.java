package src.main.java.com.example.server;
import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String outputDirectory;

    public ClientHandler(Socket clientSocket, String outputDirectory) {
        this.clientSocket = clientSocket;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            String fileName = (String) in.readObject();
            Map<String, String> data = (Map<String, String>) in.readObject();

            File outputFile = new File(outputDirectory, fileName);
            try (OutputStream output = new FileOutputStream(outputFile)) {
                Properties properties = new Properties();
                properties.putAll(data);
                properties.store(output, null);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}