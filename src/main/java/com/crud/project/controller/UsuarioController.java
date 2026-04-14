package com.crud.project.controller;

import com.crud.project.dto.request.UsuarioDTO;
import com.crud.project.dto.response.UsuariosDTO;
import com.crud.project.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping("/login")
    public ResponseEntity<UsuariosDTO> login(@RequestBody UsuarioDTO request) {
        return ResponseEntity.ok(usuarioService.validar(request));
    }
}
