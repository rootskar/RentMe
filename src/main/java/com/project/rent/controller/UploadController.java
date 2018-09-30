package com.project.rent.controller;

import com.project.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    @Autowired
    UserService userService;

    // Üleslaetava faili kataloog
    private static String UPLOADED_FOLDER = System.getProperty("user.dir")+"\\src\\main\\webapp\\resources\\avatars\\"; //TESTSYSTEM
    //private static String UPLOADED_FOLDER = "/opt/tomcat/webapps/rent/resources/avatars/"; //DEPLOYMENT
    @PostMapping("profiil/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/profiil";
        }

        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER+ file.getOriginalFilename());
            Files.write(path, bytes);
            userService.saveAvatar(file.getOriginalFilename(), userService.findUserByEmail(auth.getName())); // salvestame kasutaja avatari info andmebaasi


            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/profiil";

    }


}
