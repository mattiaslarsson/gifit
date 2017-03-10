package to.mattias.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by mattias on 2017-03-10.
 */
@Component
public class FileStorage {
    @Autowired
    private Environment env;


    public String save(MultipartFile fileToStore) throws IOException {
        String location = env.getProperty("spring.http.multipart.location");
        System.out.println(location);
        String filename = String.valueOf(System.currentTimeMillis()) + ".mp4";
        File file = new File(filename);
        fileToStore.transferTo(file);
        return location+filename;
    }
}
