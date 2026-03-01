package com.saas.spring.User;

import java.util.List;

import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties.Apiversion.Use;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "Users", description = "Gestión de usuarios del sistema")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping()
    @Transactional(readOnly = true)
    @Operation(summary = "Get all users", description = "Obtiene todos los usuarios registrados en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = User.class)))
    })
    public ResponseEntity<List<User>> getAllUser(){
        log.info("Obteniendo todo los usuarios");

        List<User> u = userService.getAllUsers();
        log.info("Cantidad de usuario obtenidos : {}", u.size());
        return ResponseEntity.ok(u);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Get user by ID", description = "Obtiene un usuario específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(schema = @Schema(implementation = User.class),
                examples = {
                    @ExampleObject(name = "User Example", summary = "Ejemplo de usuario",
                        value = """
                        {
                          "id": 1,
                          "nombre": "Juan Perez",
                          "password": "hashed_password",
                          "role": []
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<User> getByIdUser(@PathVariable Long  id) {
        log.info("Obteniendo el usuario : {} ",id);

        User a = userService.getUserById(id);
        log.info("Atributos del usuario : {}",a.getId());

        return ResponseEntity.ok(a);
    }


    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Delete user", description = "Elimina permanentemente un usuario del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente",
            content = @Content(schema = @Schema(type = "string"),
                examples = {
                    @ExampleObject(name = "Success Response", summary = "Respuesta de eliminación exitosa",
                        value = "Se ha eliminado el usuario : 1")
                }
            )),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        log.info("Eliminado usuario : {}",id);

        userService.deleteUser(id);
        log.info("Se ha elimino el usuario : "+id);

        return ResponseEntity.status(HttpStatus.OK).body("Se ha eliminado el usuario : "+id);
    }

}
