package com.jmj.restaurante.service;

import com.jmj.restaurante.model.Funcionario;
import com.jmj.restaurante.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public List<Funcionario> listarTodos() {
        return repository.findAll();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Funcionario> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }

    public Funcionario salvar(Funcionario funcionario) {
        return repository.save(funcionario);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
