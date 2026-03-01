package com.saas.spring.User;

import java.util.List;

import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties.Apiversion.Use;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "Usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping()
    @Transactional(readOnly = true)
    public ResponseEntity<List<User>> getAllUser(){
        log.info("Obteniendo todo los usuarios");

        List<User> u = userService.getAllUsers();
        log.info("Cantidad de usuario obtenidos : {}", u.size());
        return ResponseEntity.ok(u);
    }
    
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<User> getByIdUser(@PathVariable Long  id) {
        log.info("Obteniendo el usuario : {} ",id);

        User a = userService.getUserById(id);
        log.info("Atributos del usuario : {}",a.getId());

        return ResponseEntity.ok(a);
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        log.info("Eliminado usuario : {}",id);

        userService.deleteUser(id);
        log.info("Se ha elimino el usuario : "+id);

        return ResponseEntity.status(HttpStatus.OK).body("Se ha eliminado el usuario : "+id);
    }

}
