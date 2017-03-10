package to.mattias.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import to.mattias.services.FileStorage;

import java.io.IOException;

/**
 * Created by mattias on 2017-03-10.
 */
@RestController
public class UploadController {



    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadVideo(@RequestPart("file")MultipartFile file,
                              @RequestParam("start") int start,
                              @RequestParam("end") int end) {
        try {
            FileStorage.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
