package com.example.gabojago_server.web.controller;

import com.example.gabojago_server.error.ErrorCode;
import com.example.gabojago_server.error.GabojagoException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @RequestMapping("/error/login")
    public void loginError() {
        throw new GabojagoException(ErrorCode.UNAUTHORIZED_MEMBER);
    }
}
