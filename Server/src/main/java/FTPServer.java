import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FTPServer {
    public static void main(String[] args) throws Exception {
        // create socket
        ServerSocket serverSocket = new ServerSocket(8000);
        while (true) {
            System.out.println("Waiting...");

            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection : " + socket);
            OutputStream outputStream = socket.getOutputStream();
            //new FTPServer().send(outputStream);
            InputStream inputStream = socket.getInputStream();
            new FTPServer().receiveFile(inputStream);
            socket.close();
        }
    }

    public void send(OutputStream outputStream) throws Exception {
        // sendfile
        File myFile = new File("FTPApp/about.txt)");
        byte[] myByteArray = new byte[(int) myFile.length() + 1];
        FileInputStream fileInputStream = new FileInputStream(myFile);
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

        FileOutputStream fileOutputStream = new FileOutputStream("def.txt");
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bytesRead = is.read(myByteArray, 0, myByteArray.length);
        current = bytesRead;

        do {
            bytesRead = is.read(myByteArray, current,
                    (myByteArray.length - current));
            if (bytesRead >= 0)
                current += bytesRead;
        } while (bytesRead > -1);

        bufferedOutputStream.write(myByteArray, 0, current);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }
} 