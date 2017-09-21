import java.util.*;
import java.io.*;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {
    }

    private static Vector trace() throws IOException {
        Scanner s = new Scanner(new URL("http://www.ida.liu.se/~TDDD66/labs/2017/report.2011-01-30_1323CET.log").openStream());
        Vector bandwidth = new Vector();

        while(s.hasNextLine()){
            String[] parts = s.nextLine().split(" ");
            bandwidth.add(Integer.parseInt(parts[4]));
        }

        return bandwidth;
    }
}