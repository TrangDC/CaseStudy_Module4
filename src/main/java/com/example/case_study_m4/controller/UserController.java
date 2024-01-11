package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.User;
import com.example.case_study_m4.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public ModelAndView showAll() {
        ModelAndView modelAndView = new ModelAndView("/admin/users/list");
        modelAndView.addObject("users", userService.findAll());
        return modelAndView;
    }

    @GetMapping("/save/{id}")
    public ModelAndView showSave(@PathVariable Long id) {
        Optional<User> userOptional = userService.findByID(id);
        ModelAndView modelAndView = new ModelAndView("/admin/users/edit");

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            modelAndView.addObject("user", user);
        }
        return new ModelAndView("/error_404");
    }

    @GetMapping("/create")
    public ModelAndView showCreate(){
        ModelAndView modelAndView = new ModelAndView("/admin/users/edit");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/create")
    public String CreateUser(@ModelAttribute User user){
        userService.save(user);
        return "redirect:/api/users";
    }


}
