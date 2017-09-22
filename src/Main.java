import java.util.*;
import java.io.*;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {
        Player player = new Player(4, 6, 2);
        player.run(trace());
    }

    private static Vector<Integer> trace() throws IOException {
        Scanner s = new Scanner(new URL("http://www.ida.liu.se/~TDDD66/labs/2017/report.2011-01-30_1323CET.log").openStream());
        Vector<Integer> bandwidth = new Vector();

        while(s.hasNextLine()){
            String[] parts = s.nextLine().split(" ");
            bandwidth.add(Integer.parseInt(parts[4])*8);
        }

        return bandwidth; // Expressed in bits per second
    }
}