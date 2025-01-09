package src.main.java.com.example.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Server <config-file-path>");
            return;
        }

        Properties config = new Properties();
        try (InputStream input = new FileInputStream(args[0])) {
            config.load(input);
        }

        String outputDirectory = config.getProperty("output.directory");
        int port = Integer.parseInt(config.getProperty("server.port"));

        // ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executor = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket, outputDirectory));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}