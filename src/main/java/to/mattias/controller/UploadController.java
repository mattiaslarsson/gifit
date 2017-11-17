package to.mattias.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import to.mattias.services.Converter;
import to.mattias.services.FileStorage;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

    private Logger logger = LoggerFactory.getLogger(getClass().getName());
    private String fileString = null;
    private String gifUrl = null;

    private AtomicInteger counter;

    private Map<String, List<String>> definedTags = new HashMap<>();
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String rootDir(@RequestParam(value = "tags", required = false) String[] tags, Model model) {

        counter = new AtomicInteger(0);

        List<String> uploadedGifs = new ArrayList<>(); // List of all uploaded gifs
        List<String> showableGifs = new ArrayList<>(); // List of gifs that has the correct tag

        // Get all uploaded gifs
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
          storage.createUploadDir();
        }
        // An array of tags is submitted in the URI
        if (tags != null) {
            Arrays.stream(tags).forEach(tag -> {
                // If the map of tags contains one of the submitted tags show the images
                if (definedTags.containsKey(tag)) {
                    showableGifs.addAll(definedTags.get(tag));
                }
            });
        // No tags were submitted in the URI, so we show them all
        } else {
            showableGifs.addAll(uploadedGifs);
        }

        model.addAttribute("showableGifs", showableGifs);
        model.addAttribute("counter", counter.get());

        return "index";
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadVideo(@RequestPart("file") MultipartFile file,
                              @RequestParam("start") int start,
                              @RequestParam("end") int end,
                              @RequestParam(value = "tag") String tag,
                              Model model) {
        try {
            fileString = storage.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gifUrl = converter.convert(fileString, start, end);
        String imageUrl = "/images/" + gifUrl.substring(gifUrl.lastIndexOf("/") + 1, gifUrl.length());

        List<String> imagesWithTag = null;
        try {
            // Get the list of images with the tag submitted and add the uploaded gif to this list
            imagesWithTag = definedTags.get(tag);
            imagesWithTag.add(imageUrl);
        } catch (NullPointerException e) {
            // If there is no images with that tag we create a new tag and new list
            imagesWithTag = new ArrayList<>();
            imagesWithTag.add(imageUrl);
            definedTags.put(tag, imagesWithTag);
        }

        model.addAttribute("url", imageUrl);
        return "result";
    }





}
