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
    	  // Command for running ffmpeg
        String command = "ffmpeg -i " + file + " -ss 00:00:" + start + " -t 00:00:" + end + " " + outFile + ".gif";
        try {
            // Execute the ffmpeg command
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            // Remove the old file
            Process p2 = Runtime.getRuntime().exec("rm " + file);
            p2.waitFor();

            return outFile + ".gif";
        } catch (IOException  | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
