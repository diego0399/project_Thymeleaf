package com.crud.project.controller;

import com.crud.project.dto.request.ClienteDTO;
import com.crud.project.dto.response.ClientesDTO;
import com.crud.project.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteViewController {

    private final ClienteService service;

    public ClienteViewController(ClienteService service) {
        this.service = service;
    }

    // Muestra la vista principal del CRUD
    @GetMapping
    public String listar(Model model, HttpSession session) {

        // Si no hay usuario logueado, lo mandamos al login
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        // Obtiene la lista de clientes desde el servicio
        List<ClientesDTO> clientes = service.listar();

        // Enviamos la lista a la vista
        model.addAttribute("clientes", clientes);

        // Enviamos un objeto vacío para el formulario de nuevo cliente
        model.addAttribute("cliente", new ClienteDTO());

        // Indicamos que no estamos editando
        model.addAttribute("editando", false);

        // Retorna templates/clientes.html
        return "clientes";
    }

    // Inserta un nuevo cliente
    @PostMapping
    public String insertar(@ModelAttribute("cliente") ClienteDTO request,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        // Si no hay sesión, redirigimos al login
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        // Llama al servicio para insertar
        ClientesDTO resp = service.insertar(request);

        // Si tu servicio usa res/msj para errores, puedes validarlo aquí
        if (resp.getRes() != null && resp.getRes() < 0) {
            model.addAttribute("clientes", service.listar());
            model.addAttribute("cliente", request);
            model.addAttribute("editando", false);
            model.addAttribute("mensaje", resp.getMsj());
            return "clientes";
        }

        // Si todo sale bien, vuelve al listado
        redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado correctamente");
        return "redirect:/clientes";
    }

    // Actualiza un cliente existente
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("cliente") ClienteDTO request,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        // Si no hay sesión, redirigimos al login
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        // Llama al servicio para actualizar
        ClientesDTO resp = service.actualizar(request);

        // Si hubo error, vuelve a cargar la vista con el mensaje
        if (resp.getRes() != null && resp.getRes() < 0) {
            model.addAttribute("clientes", service.listar());
            model.addAttribute("cliente", request);
            model.addAttribute("editando", true);
            model.addAttribute("mensaje", resp.getMsj());
            return "clientes";
        }

        // Si sale bien, redirige al listado
        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Cliente " + request.getCorreo() + " actualizado correctamente"
        );
        return "redirect:/clientes";
    }

    // Elimina un cliente por id
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        // Si no hay sesión, redirigimos al login
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        // Llama al servicio para eliminar
        service.eliminar(id);

        // Redirige nuevamente al listado
        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Registro eliminado correctamente."
        );
        return "redirect:/clientes";
    }


    //Eventos
    // Carga el cliente en el formulario para editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                         Model model,
                         HttpSession session) {

        // Si no hay sesión, redirigimos al login
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        // Obtenemos todos los clientes para seguir mostrando la tabla
        List<ClientesDTO> clientes = service.listar();

        // Buscamos el cliente que se va a editar
        ClientesDTO clienteSeleccionado = clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        // Enviamos lista y cliente seleccionado a la vista
        model.addAttribute("clientes", clientes);
        model.addAttribute("cliente", clienteSeleccionado);
        model.addAttribute("editando", true);

        return "clientes";
    }
}
