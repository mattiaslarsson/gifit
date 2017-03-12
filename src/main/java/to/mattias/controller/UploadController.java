package to.mattias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import to.mattias.services.Converter;
import to.mattias.services.FileStorage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


/**
 * Created by mattias on 2017-03-10.
 */
@Controller
public class UploadController {
    @Autowired
    private FileStorage storage;
    @Autowired
    private Converter converter;
    @Autowired
    private Environment env;

    private String fileString = null;
    private String gifUrl = null;

    private AtomicInteger counter;
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String rootDir(Model model) {
        counter = new AtomicInteger(0);
        List<String> uploadedGifs = new ArrayList<>();
        try(Stream<Path> paths = Files.walk(Paths.get(env.getProperty("spring.http.multipart.location")))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    counter.addAndGet(1);
                    uploadedGifs.add("/images/" +
                            filePath.toString().substring(
                                    filePath.toString().lastIndexOf("/") + 1, filePath.toString().length()));
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("uploadedGifs", uploadedGifs);
        model.addAttribute("counter", counter.get());
        return "index";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadVideo(@RequestPart("file") MultipartFile file,
                              @RequestParam("start") int start,
                              @RequestParam("end") int end,
                              Model model) {
        try {
            fileString = storage.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gifUrl = converter.convert(fileString, start, end);
        model.addAttribute("url", "/images/" + gifUrl.substring(gifUrl.lastIndexOf("/") + 1, gifUrl.length()));
        return "result";
    }




}
