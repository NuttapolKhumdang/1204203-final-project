package experimentals;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * clinet
 */
public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 33406);
            PrintStream stream = new PrintStream(socket.getOutputStream());

            Scanner s = new Scanner(System.in);

            System.out.print("Message: ");
            stream.println(s.nextLine());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}