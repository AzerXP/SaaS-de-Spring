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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Roles", description = "Gestión de roles del sistema")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    @Transactional(readOnly = true)
    @Operation(summary = "Get all roles", description = "Obtiene todos los roles disponibles en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = Role.class)))
    })
    public ResponseEntity<List<Role>> getAllRole() {
        log.info("Obteniendo todos los roles");

        List<Role> roles = roleService.getAllRoles();
        log.info("Cantidad de roles: {}", roles.size());

        return ResponseEntity.ok(roles);
    }


    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Get role by ID", description = "Obtiene un rol específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rol encontrado",
            content = @Content(schema = @Schema(implementation = Role.class),
                examples = {
                    @ExampleObject(name = "Role Example", summary = "Ejemplo de rol",
                        value = """
                        {
                          "id": 1,
                          "nombre": "ADMIN"
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<Role> getByIdRole(@PathVariable Long id){
        log.info("Obteniendo el rol de : "+id);
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping()
    @Transactional
    @Operation(summary = "Create role", description = "Crea un nuevo rol en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rol creado exitosamente",
            content = @Content(schema = @Schema(type = "string"),
                examples = {
                    @ExampleObject(name = "Success Response", summary = "Respuesta de creación exitosa",
                        value = "Se ha creado con exito el rol")
                }
            )),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<String> createRole(RoleInDto roleInDto){
        log.info("creando el rol con los valos : " + roleInDto.nombre());

        roleService.creatRole(roleInDto);
        return ResponseEntity.status(HttpStatus.OK).body("Se ha creado con exito el rol");
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Update role", description = "Actualiza completamente un rol existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rol actualizado",
            content = @Content(schema = @Schema(type = "string"),
                examples = {
                    @ExampleObject(name = "Success Response", summary = "Respuesta de actualización exitosa",
                        value = "Se ha actualido con exito el rol")
                }
            )),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<String> updateRole(@PathVariable long id, RoleInDto roleInDto){
        log.info("actualizando el rol con los valos : " + roleInDto.nombre());

        roleService.updatRole(id, roleInDto);
        log.info(" se ha actualizado el rol con los valos : " + id);
        return ResponseEntity.status(HttpStatus.OK).body("Se ha actualido con exito el rol");

    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Delete role", description = "Elimina permanentemente un rol del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente",
            content = @Content(schema = @Schema(type = "string"),
                examples = {
                    @ExampleObject(name = "Success Response", summary = "Respuesta de eliminación exitosa",
                        value = "Se ha eliminado con exito el rol")
                }
            )),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<String> deleteRole(@PathVariable long id){
        log.info("eliminado el rol : "+id);

        roleService.deleteRole(id);
        log.info("Se ha eliminado el rol con exito : "+id);
        return ResponseEntity.status(HttpStatus.OK).body("Se ha eliminado con exito el rol");
    }
}
