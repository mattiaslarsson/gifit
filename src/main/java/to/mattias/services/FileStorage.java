package to.mattias.services;

import static java.lang.System.currentTimeMillis;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by mattias on 2017-03-10.
 */
@Component
public class FileStorage {

  private final Environment env;

  @Autowired
  public FileStorage(Environment env) {
    this.env = env;
  }

  public String save(MultipartFile fileToStore) throws IOException {

    String location = env.getProperty("spring.http.multipart.location");
    // Generate a unique filename for the uploaded file and save the uploaded file
    String filename = currentTimeMillis() + ".mp4";
    File file = new File(filename);
    fileToStore.transferTo(file);

    return location + "/" + filename;
  }

  public void createUploadDir() {
    String location = env.getProperty("spring.http.multipart.location");
    new File(location).mkdir();
  }
}
