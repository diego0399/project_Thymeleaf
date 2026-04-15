package com.crud.project.controller;

import com.crud.project.dto.request.UsuarioDTO;
import com.crud.project.dto.response.UsuariosDTO;
import com.crud.project.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Muestra la vista login.html
    @GetMapping("/login")
    public String loginForm(Model model) {

        // Enviamos un objeto vacío al formulario
        // para que Thymeleaf lo pueda bindear
        model.addAttribute("usuario", new UsuarioDTO());

        // Retorna templates/login.html
        return "login";
    }

    // Procesa el formulario de login
    @PostMapping("/login")
    public String login(@ModelAttribute("usuario") UsuarioDTO request,
                        Model model,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        // Llama al servicio para validar credenciales
        UsuariosDTO resp = usuarioService.validar(request);

        // Si viene un código de error, volvemos al login
        if (resp.getRes() != null && resp.getRes() < 0) {
            model.addAttribute("error", resp.getMsj());
            model.addAttribute("usuario", request);
            model.addAttribute("mensaje", resp.getMsj());
            return "login";
        }

        // Si autentica correctamente, guardamos algo en sesión
        session.setAttribute("usuarioLogueado", resp.getLogin());
        session.setAttribute("roles", resp.getRoles());

        // Redirige al CRUD de clientes
        redirectAttributes.addFlashAttribute("mensaje", "Inicio de sesión exitoso.");
        return "redirect:/clientes";
    }
    // Cierra sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        // Elimina toda la sesión actual
        session.invalidate();

        // Redirige al login
        return "redirect:/login";
    }

}
