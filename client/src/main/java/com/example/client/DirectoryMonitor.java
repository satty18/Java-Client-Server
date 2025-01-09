package src.main.java.com.example.client;
import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public class DirectoryMonitor {
    private final String directoryPath;
    private final Pattern keyPattern;
    private final String serverAddress;
    private final int serverPort;

    public DirectoryMonitor(String directoryPath, String keyPattern, String serverAddress, int serverPort) {
        this.directoryPath = directoryPath;
        this.keyPattern = Pattern.compile(keyPattern);
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void startMonitoring() throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(directoryPath);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();
                Path filePath = path.resolve(fileName);

                if (fileName.toString().endsWith(".properties")) {
                    processFile(filePath.toFile());
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    private void processFile(File file) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(file)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Map<String, String> filteredMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (keyPattern.matcher(key).matches()) {
                filteredMap.put(key, properties.getProperty(key));
            }
        }

        sendToServer(filteredMap, file.getName());

        if (!file.delete()) {
            System.out.println("Failed to delete file: " + file.getName());
        }
    }

    private void sendToServer(Map<String, String> data, String fileName) {
        try (Socket socket = new Socket(serverAddress, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(fileName);
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}