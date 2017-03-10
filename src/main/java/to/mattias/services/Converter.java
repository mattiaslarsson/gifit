package to.mattias.services;

import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * <h1>Created by Mattias on 2017-03-10.</h1>
 */
@Service
public class Converter {

    public void convert(String file, int start, int end) {
        String command = "ffmpeg -t " + (end-start) + "-ss 00:00:0" + start + " -i "
                + file + " " + file + ".gif";
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
