import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FTPClient {
    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        // localhost for testing
        Socket socket = new Socket("127.0.0.1", 8000);
        System.out.println("Connecting...");
        InputStream inputStream = socket.getInputStream();
        // receive file
        new FTPClient().receiveFile(inputStream);
        OutputStream outputStream = socket.getOutputStream();
        //new FTPClient().send(outputStream);
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        socket.close();
    }


    public void send(OutputStream outputStream) throws Exception {
        // sendfile
        File myFile = new File("FTPApp/about.txt)");
        byte[] byteArray = new byte[(int) myFile.length() + 1];
        FileInputStream fileInputStream = new FileInputStream(myFile);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedInputStream.read(byteArray, 0, byteArray.length);
        System.out.println("Sending...");
        outputStream.write(byteArray, 0, byteArray.length);
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