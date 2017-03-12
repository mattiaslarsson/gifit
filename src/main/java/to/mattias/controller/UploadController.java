package to.mattias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import to.mattias.services.Converter;
import to.mattias.services.FileStorage;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String rootDir(HttpServletRequest request, HttpServletResponse response) {
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
        model.addAttribute("url", "/images/" + gifUrl.substring(gifUrl.lastIndexOf("/") + 1, gifUrl.length()));
        return "result";
    }




}
