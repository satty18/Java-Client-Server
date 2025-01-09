package src.main.java.com.example.client;
import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
public class Client {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Client <config-file-path>");
            return;
        }

        Properties config = new Properties();
        try (InputStream input = new FileInputStream(args[0])) {
            config.load(input);
        }

        String directoryPath = config.getProperty("directory.path");
        String keyPattern = config.getProperty("key.pattern");
        String serverAddress = config.getProperty("server.address");
        int serverPort = Integer.parseInt(config.getProperty("server.port"));

        DirectoryMonitor monitor = new DirectoryMonitor(directoryPath, keyPattern, serverAddress, serverPort);
        monitor.startMonitoring();
    }
}