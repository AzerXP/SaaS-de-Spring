package com.saas.spring.User;

import java.util.List;

import com.saas.spring.Role.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "EL nombre del usurio no puede ser nulo o vacio")
    private String nombre;

    @NotBlank(message = "El password del usuario no puede ser nulo o vacio")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> role;


}
