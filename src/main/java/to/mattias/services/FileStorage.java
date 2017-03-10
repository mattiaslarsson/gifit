package to.mattias.services;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by mattias on 2017-03-10.
 */
@Component
public class FileStorage {

    public static void save(MultipartFile fileToStore) throws IOException {
        String filename = String.valueOf(System.currentTimeMillis());
        File file = new File(filename);
        fileToStore.transferTo(file);
    }
}
