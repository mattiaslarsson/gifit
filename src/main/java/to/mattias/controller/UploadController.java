package to.mattias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import to.mattias.services.Converter;
import to.mattias.services.FileStorage;

import java.io.IOException;

/**
 * Created by mattias on 2017-03-10.
 */
@RestController
public class UploadController {
    @Autowired
    private FileStorage storage;
    @Autowired
    private Converter converter;

    private String fileString = null;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadVideo(@RequestPart("file")MultipartFile file,
                              @RequestParam("start") int start,
                              @RequestParam("end") int end) {
        try {
            fileString = storage.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(file != null) {
            converter.convert(fileString, start, end);
        }
        return "";
    }
}
