package br.com.pulsepay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "forward:/cadastro.html";
    }

    @GetMapping("/carteira")
    public String carteira() {
        return "forward:/carteira.html";
    }
}