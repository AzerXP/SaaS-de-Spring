package com.saas.spring.User;

import java.util.List;

import org.springframework.stereotype.Service;

import com.saas.spring.Role.Role;
import com.saas.spring.Role.RoleRepository;
import com.saas.spring.User.dto.UserInDto;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private User toEntity(UserInDto userInDto, List<Role> roles){
        return User.builder()
            .nombre(userInDto.nombre())
            .password(userInDto.password())
            .role(roles).build();
    }

    public User createUser(UserInDto userInDto){

        if (userInDto.IdRoles() == null || userInDto.nombre().isBlank() || userInDto.password().isBlank()) {
            throw new IllegalArgumentException("Error en el dto no puede ser nulo o vacio el idRole, nombre , passowrd");
        }

        List<Role> roles = roleRepository.findAllById(userInDto.IdRoles());

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Error la lista esta vacio");
        }

        User user = toEntity(userInDto, roles);

        return userRepository.save(user);
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con : "+id));
    }

    public void deleteUser(Long id){
        userRepository.delete(getUserById(id));
    }
}
