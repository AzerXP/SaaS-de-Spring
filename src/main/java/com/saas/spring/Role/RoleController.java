package com.saas.spring.Role;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saas.spring.Role.dto.RoleInDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


;

@RestController
@RequestMapping("/role")
@Slf4j
@Tag(name = "Roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    @Transactional(readOnly = true)
    public ResponseEntity<List<Role>> getAllRole() {
        log.info("Obteniendo todos los roles");
        
        List<Role> roles = roleService.getAllRoles();
        log.info("Cantidad de roles: {}", roles.size());
        
        return ResponseEntity.ok(roles);
    }   


    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Role> getByIdRole(@PathVariable Long id){
        log.info("Obteniendo el rol de : "+id);
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<String> createRole(RoleInDto roleInDto){
        log.info("creando el rol con los valos : " + roleInDto.nombre());

        roleService.creatRole(roleInDto);
        return ResponseEntity.status(HttpStatus.OK).body("Se ha creado con exito el rol");
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> updateRole(@PathVariable long id, RoleInDto roleInDto){
        log.info("actualizando el rol con los valos : " + roleInDto.nombre());

        roleService.updatRole(id, roleInDto);
        log.info(" se ha actualizado el rol con los valos : " + id); 
        return ResponseEntity.status(HttpStatus.OK).body("Se ha actualido con exito el rol");
        
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteRole(@PathVariable long id){
        log.info("eliminado el rol : "+id);

        roleService.deleteRole(id);
        log.info("Se ha eliminado el rol con exito : "+id);
        return ResponseEntity.status(HttpStatus.OK).body("Se ha eliminado con exito el rol");
    }
}
