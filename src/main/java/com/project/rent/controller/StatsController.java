package com.project.rent.controller;

import com.project.rent.model.User;
import com.project.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class StatsController {

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/statistika"}, method = RequestMethod.GET)
    public ModelAndView statistika(){ //statistika lehe kuvamine

        List<User> userArrayList = userService.getUserList();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users", userArrayList); //lisame kõikide kasutajate listi statistika lehele kasutamiseks
        modelAndView.setViewName("statistika");
        return modelAndView;
    }

    @RequestMapping(value={"/statistika"}, method = RequestMethod.POST)
    public ModelAndView statistika(User user, int value){ //kasutajate aktiveerimine/deaktiveerimine

       ModelAndView modelAndView = new ModelAndView();
       userService.saveActive(value, user);

        return modelAndView;
    }
}