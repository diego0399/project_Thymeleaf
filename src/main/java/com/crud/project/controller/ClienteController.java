package com.crud.project.controller;

import com.crud.project.dto.request.ClienteDTO;
import com.crud.project.dto.response.ClientesDTO;
import com.crud.project.service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClientesDTO> clientes() {
        return service.listar();
    }

    @PostMapping
    public ClientesDTO insertar(@RequestBody ClienteDTO request){
        return service.insertar(request);
    }

    @PutMapping("/{id}")
    public ClientesDTO actualizar(@PathVariable Long id, @RequestBody ClienteDTO request){
        request.setId(id);
        return service.actualizar(request);
    }

    @DeleteMapping("/{id}")
    public ClientesDTO eliminar(@PathVariable Long id){
        return service.eliminar(id);
    }
}
