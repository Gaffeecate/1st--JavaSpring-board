package com.example.firstproject.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {

    @GetMapping("/hi")
    public String nicetomeetyou(Model model){
        model.addAttribute("username", "짱구");
        return "greetings";
    }

    @GetMapping("/bye")
    public String seeyounext(Model model){
        model.addAttribute("username", "둘리");
        return "goodbye";
    }



}


