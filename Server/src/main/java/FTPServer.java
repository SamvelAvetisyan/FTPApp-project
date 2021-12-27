import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FTPServer {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8000);
        while (true) {
            System.out.println("Waiting...");
            Socket socket = serverSocket.accept();

            OutputStream outputStream = socket.getOutputStream();
//            new FTPServer().send(outputStream);

            InputStream inputStream = socket.getInputStream();
            new FTPServer().receiveFile(inputStream);
            socket.close();
        }
    }

    ExecutorService threadPool = Executors.newFixedThreadPool(8);

    public void send(OutputStream outputStream) throws Exception {
        // sendfile
        File serverDirectory = new File(System.getProperty("user.home"), "Desktop\\FTPApp");
        serverDirectory.mkdirs();
        File file = new File(serverDirectory, "hello-local.txt");
        byte[] myByteArray = new byte[(int) file.length() + 1];
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedInputStream.read(myByteArray, 0, myByteArray.length);
        System.out.println("Sending...");
        outputStream.write(myByteArray, 0, myByteArray.length);
        outputStream.flush();
    }

    public void receiveFile(InputStream is) throws Exception {

        int filesize = 6022386;
        int bytesRead;
        int current = 0;
        byte[] myByteArray = new byte[filesize];

        BufferedOutputStream bufferedOutputStream;
        try (FileOutputStream fileOutputStream = new FileOutputStream("def.txt")) {
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bytesRead = is.read(myByteArray, 0, myByteArray.length);
            current = bytesRead;

            do {
                bytesRead = is.read(myByteArray, current,
                        (myByteArray.length - current));
                if (bytesRead >= 0)
                    current += bytesRead;
            } while (bytesRead > -1);
            int finalCurrent = current;
            threadPool.execute(() -> {
                System.out.println("receiving");
                try {
                    bufferedOutputStream.write(myByteArray, 0, finalCurrent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

