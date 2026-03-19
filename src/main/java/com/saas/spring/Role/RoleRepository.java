package com.saas.spring.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Set<Role> findAllByIdSet(List<Long> id);

    Optional<Role> findByNombre(String nombre);
}
