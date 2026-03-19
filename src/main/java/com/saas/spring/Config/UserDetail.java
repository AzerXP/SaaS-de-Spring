package com.saas.spring.Config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.saas.spring.User.User;
import com.saas.spring.User.UserRepository;

@Service
public class UserDetail implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {
        User u = userRepository.findByNombre(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<GrantedAuthority> authorities = u.getRoles().stream()
                                                        .map(r -> new SimpleGrantedAuthority
                                                            (r.getNombre())).collect(Collectors.toList());

       return new org.springframework.security.core.userdetails.User(u.getNombre(),u.getPassword(),authorities);
    }

}
