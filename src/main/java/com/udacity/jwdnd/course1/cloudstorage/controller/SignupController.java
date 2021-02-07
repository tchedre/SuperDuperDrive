package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.UserDto;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute UserDto userDto, Model model) {

        if (!userService.isUsernameAvailable(userDto.getUsername())) {
            model.addAttribute("signupError", "The username already exists.");
            return "signup";
        }

        if (userService.createUser(userDto) < 0) {
            model.addAttribute("signupError","There was an error signing you up. Please try again.");
            return "signup";
        }

        model.addAttribute("signupSuccess", true);
        return "login";
    }

}
