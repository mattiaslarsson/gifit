package to.mattias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import to.mattias.services.Converter;
import to.mattias.services.FileStorage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by mattias on 2017-03-10.
 */
@Controller
public class UploadController {
    @Autowired
    private FileStorage storage;
    @Autowired
    private Converter converter;

    private String fileString = null;
    private String gifUrl = null;

    private AtomicInteger counter = new AtomicInteger(0);
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String rootDir(Model model) {
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

        if(file != null) {
            gifUrl = converter.convert(fileString, start, end);
        }
        counter.addAndGet(1);
        model.addAttribute("url", "/images/" + gifUrl.substring(gifUrl.lastIndexOf("/") + 1, gifUrl.length()));
        return "result";
    }




}
