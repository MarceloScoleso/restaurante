package com.jmj.restaurante.service;

import com.jmj.restaurante.model.Restaurante;
import com.jmj.restaurante.repository.RestauranteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteRepository repository;

    public RestauranteService(RestauranteRepository repository) {
        this.repository = repository;
    }

    public List<Restaurante> listarTodos() {
        return repository.findAll();
    }

    public Optional<Restaurante> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Restaurante salvar(Restaurante restaurante) {
        return repository.save(restaurante);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
