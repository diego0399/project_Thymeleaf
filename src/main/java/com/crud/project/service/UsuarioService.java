package com.crud.project.service;

import com.crud.project.dto.request.UsuarioDTO;
import com.crud.project.dto.response.UsuariosDTO;
import com.crud.project.entity.Usuario;
import com.crud.project.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuariosDTO validar(UsuarioDTO request){
        Usuario usuario = usuarioRepository.findByUserIgnoreCase(request.getLogin()).orElse(null);

        if (usuario == null) {
            return new UsuariosDTO(0, "Usuario incorrecto");
        }


        if (usuario.getEnabled() == null || usuario.getEnabled() != 1) {
            return new UsuariosDTO(-1,"Usuario inactivo");
        }

        if (!usuario.getPassword().equals(request.getPass())) {
            return new UsuariosDTO(-2,"Credenciales incorrectas");
        }

        List<String> roles = usuario.getRoles()
                .stream()
                .map(rol -> rol.getNombre())
                .toList();

        return new UsuariosDTO(
                usuario.getId(),
                usuario.getUser(),
                usuario.getEnabled(),
                roles
        );
    }
}
