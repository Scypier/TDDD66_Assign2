import java.util.*;
import java.io.*;
import java.net.URL;

public class main {

    public static void main(String[] args) throws IOException {
    }

    private static Vector trace() throws IOException {
        URL traceUrl = new URL("http://www.ida.liu.se/~TDDD66/labs/2017/report.2011-01-30_1323CET.log");
        Scanner s = new Scanner(traceUrl.openStream());
        Vector bandwidth = new Vector();
        String row;
        int traceWidth;

        while(s.hasNextLine()){
            row = s.nextLine();
            String[] parts = row.split(" ");
            traceWidth = Integer.parseInt(parts[4]);
            bandwidth.add(traceWidth);
        }

        return bandwidth;
    }
}