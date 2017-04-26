package to.mattias.services;

import org.springframework.stereotype.Service;
import java.io.IOException;

/**
 * <h1>Created by Mattias on 2017-03-10.</h1>
 */
@Service
public class Converter {
    public String convert(String file, int start, int end) {
    	String outFile = file.substring(0, file.lastIndexOf("."));
        String command = "ffmpeg -i " + file + " -ss 00:00:" + start + " -t 00:00:" + end + " " + outFile + ".gif";
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            Process p2 = Runtime.getRuntime().exec("rm " + file);
            p2.waitFor();
            return outFile + ".gif";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
