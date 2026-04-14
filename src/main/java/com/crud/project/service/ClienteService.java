package com.crud.project.service;

import com.crud.project.dto.request.ClienteDTO;
import com.crud.project.dto.response.ClientesDTO;
import com.crud.project.entity.Cliente;
import com.crud.project.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClientesDTO> listar() {
        return clienteRepository.findAll(Sort.by("id"))
                .stream()
                .map(cliente -> new ClientesDTO(
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getApellido(),
                        cliente.getCorreo(),
                        cliente.getTelefono(),
                        cliente.getFechaCreacion()
                ))
                .toList();
    }
    @Transactional
    public ClientesDTO insertar(ClienteDTO request) {
        try {

            if(!Validardatos(request)){
                return new ClientesDTO(0, "Debe enviar todos los datos correctamente");
            }

            if (clienteRepository.existsByCorreo(request.getCorreo())) {
                return new ClientesDTO(-1, "El correo ya existe");
            }

            Cliente cliente = new Cliente();
            cliente.setNombre(request.getNombre().trim());
            cliente.setApellido(request.getApellido().trim());
            cliente.setCorreo(request.getCorreo().trim());
            cliente.setTelefono(request.getTelefono().trim());

            Cliente guardado = clienteRepository.save(cliente);

            if (guardado.getId() != null) {
                return new ClientesDTO(1, "Registro exitoso");
            }

            return new ClientesDTO(-1, "No se pudo insertar el cliente");

        } catch (Exception e) {
            return new ClientesDTO(-2, "Ocurrió un error al insertar el cliente");
        }
    }


    @Transactional
    public ClientesDTO actualizar(ClienteDTO request) {
        try {

            if(!Validardatos(request)){
                return new ClientesDTO(0, "Debe enviar todos los datos correctamente");
            }

            Optional<Cliente> clienteOptional = clienteRepository.findById(request.getId());

            if (clienteOptional.isEmpty()) {
                return new ClientesDTO(-1, "El cliente no existe");
            }

            Cliente cliente = clienteOptional.get();

            if (clienteRepository.existsByCorreoAndIdNot(request.getCorreo(), request.getId())) {
                return new ClientesDTO(-1, "El correo ya está registrado por otro cliente");
            }

            cliente.setNombre(request.getNombre().trim());
            cliente.setApellido(request.getApellido().trim());
            cliente.setCorreo(request.getCorreo().trim());
            cliente.setTelefono(request.getTelefono().trim());

            Cliente actualizado = clienteRepository.save(cliente);

            if (actualizado.getId() != null) {
                return new ClientesDTO(1, "Cliente actualizado correctamente");
            }

            return new ClientesDTO(-1, "No se pudo actualizar el cliente");

        } catch (Exception e) {
            return new ClientesDTO(-2, "Ocurrió un error al actualizar el cliente");
        }
    }

    @Transactional
    public ClientesDTO eliminar(Long id){
        try{

            if (id == null) {
                return new ClientesDTO(-4, "El id del cliente es obligatorio");
            }

            if (!clienteRepository.existsById(id)) {
                return new ClientesDTO(-3, "El cliente no existe");
            }

            clienteRepository.deleteById(id);
            if (!clienteRepository.existsById(id)) {
                return new ClientesDTO(1, "Cliente eliminado correctamente");
            }

            return new ClientesDTO(-1, "No se pudo eliminar el cliente");

        } catch (Exception e) {
            return new ClientesDTO(-2, "Ocurrió un error al eliminar el cliente");
        }
    }

    public boolean Validardatos(ClienteDTO request){
        if (request == null) {
            return false;
        }

        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            return false;
        }

        if (request.getApellido() == null || request.getApellido().trim().isEmpty()) {
            return false;
        }

        if (request.getCorreo() == null || request.getCorreo().trim().isEmpty()) {
            return false;
        }

        if (!request.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return false;
        }

        if (request.getTelefono() == null || request.getTelefono().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
