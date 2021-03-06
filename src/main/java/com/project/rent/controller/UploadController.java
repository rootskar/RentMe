package com.project.rent.controller;

import com.project.rent.service.RentService;
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

    @Autowired
    RentService rentService;

    // Üleslaetava faili kataloog
    private static String UPLOADED_FOLDER = System.getProperty("user.dir")+"/src/main/webapp/resources/avatars/"; //TESTSYSTEM
    //private static String UPLOADED_FOLDER = "/opt/tomcat/webapps/rent/resources/avatars/"; //DEPLOYMENT

    @PostMapping("profiil/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String filetype = file.getOriginalFilename().substring(file.getOriginalFilename().length()-3).toLowerCase();
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("failError", "Vali üleslaadimiseks fail");
            return "redirect:/profiil";
        }

        try {

            if(filetype.equals("jpg") || filetype.equals("png")) {
                byte[] bytes = file.getBytes();
                String failinimi = userService.findUserByEmail(auth.getName()).getUsername()+"."+filetype;
                Path path = Paths.get(UPLOADED_FOLDER+failinimi);
                Files.write(path, bytes);
                userService.saveAvatar(failinimi, userService.findUserByEmail(auth.getName())); // salvestame kasutaja avatari info andmebaasi
            } else {
                redirectAttributes.addFlashAttribute("failError", "Failiformaat peab olema JPG või PNG");
                return "redirect:/profiil";
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/profiil";

    }

}
