package com.jmj.restaurante.repository;

import com.jmj.restaurante.model.Usuario;
import com.jmj.restaurante.model.enums.UsuarioRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByRole(UsuarioRole role);
}
