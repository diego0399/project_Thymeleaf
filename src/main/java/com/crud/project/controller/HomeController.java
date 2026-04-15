package com.crud.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Cuando el usuario entra a la raíz del proyecto
    // por ejemplo: http://localhost:8080/
    // lo redirigimos al login
    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }
}
