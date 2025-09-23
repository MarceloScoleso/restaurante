package com.jmj.restaurante.service;

import com.jmj.restaurante.model.Mesa;
import com.jmj.restaurante.repository.MesaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    private final MesaRepository repository;

    public MesaService(MesaRepository repository) {
        this.repository = repository;
    }

    public List<Mesa> listarTodas() {
        return repository.findAll();
    }

    public Optional<Mesa> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Mesa salvar(Mesa mesa) {
        return repository.save(mesa);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
