package com.jmj.restaurante.service;

import com.jmj.restaurante.model.CategoriaProduto;
import com.jmj.restaurante.repository.CategoriaProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProdutoService {

    private final CategoriaProdutoRepository repository;

    public CategoriaProdutoService(CategoriaProdutoRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaProduto> listarTodos() {
        return repository.findAll();
    }

    public Optional<CategoriaProduto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public CategoriaProduto salvar(CategoriaProduto categoria) {
        return repository.save(categoria);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
