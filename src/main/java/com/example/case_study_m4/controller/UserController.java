package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.User;
import com.example.case_study_m4.repository.IUserRepository;
import com.example.case_study_m4.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRepository userRepository;

    @GetMapping
    public ModelAndView listUsersActive(@PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/admin/users/list");
        Page<User> userPage = userService.findAllPage(pageable);
        modelAndView.addObject("users", userPage);
        return modelAndView;
    }


    @GetMapping("/update/{id}")
    public ModelAndView showSave(@PathVariable Long id) {
        Optional<User> userOptional = userService.findByID(id);
        if (userOptional.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/admin/users/edit");
            User user = userOptional.get();
            modelAndView.addObject("user", user);
            return modelAndView;
        }else {
            return new ModelAndView("/error_404");
        }
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute  User user) {
            userService.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/change-status/{id}")
    public String changeStatus(@RequestParam("id") Long id, @RequestParam("newStatus") boolean newStatus) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(newStatus);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }



}
