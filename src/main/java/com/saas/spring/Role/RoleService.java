package com.saas.spring.Role;

import java.util.List;

import org.springframework.stereotype.Service;

import com.saas.spring.Role.dto.RoleInDto;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role creatRole(RoleInDto roleInDto){

        if (roleInDto.nombre().isBlank()) {
            throw new IllegalArgumentException("Error el nombre no puede ser nulo");
        }

        return roleRepository.save(Role.builder().nombre(roleInDto.nombre()).build());
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id){
        return roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Error en buscar el role con el id: "+id));
    }

    public void deleteRole(Long id){
        roleRepository.delete(getRoleById(id));
    }

}
