package com.example.case_study_m4.controller;

import com.example.case_study_m4.dto.UserDto;
import com.example.case_study_m4.model.User;
import com.example.case_study_m4.repository.IUserRepository;
import com.example.case_study_m4.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@AllArgsConstructor
@SessionAttributes("userdto")
public class LoginUserController {
    private IUserService userService;
    @ModelAttribute("userdto")
    public UserDto userDto(){
        return new UserDto();
    }

    @GetMapping("/login")
    public String showLoginForm(){
        return "/admin/users/login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute("userdto") UserDto userDto, Model model){
        if (userService.checkUserByEmail(userDto.getEmail()) == false){
            return "redirect:/login?emailWrong";
        }
        User user = userService.getUserByEmail(userDto.getEmail());
        if (user.equals("ADMIN@a1")){
            return "redirect:/api/users/users/{id}";
        }
        if (userService.checkPasswordUser(userDto.getEmail(),userDto.getPassword())){
            return "redirect:/api/users/users/{id}";
        }
        return "redirect:/login?passWordWrong";
    }
}
