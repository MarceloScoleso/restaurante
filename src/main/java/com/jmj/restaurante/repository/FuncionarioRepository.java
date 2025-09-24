package com.jmj.restaurante.repository;

import com.jmj.restaurante.model.Funcionario;
import com.jmj.restaurante.model.enums.FuncionarioRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByEmail(String email);
    Optional<Funcionario> findByRole(FuncionarioRole role);
}
