package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.Cliente;
import com.jmj.restaurante.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", service.listarTodos());
        return "cliente/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Cliente cliente) {
    if (cliente.getId() != null) {
        // Atualiza cliente existente
        Cliente existente = service.buscarPorId(cliente.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        existente.setNome(cliente.getNome());
        existente.setEmail(cliente.getEmail());
        existente.setTelefone(cliente.getTelefone());
        service.salvar(existente);
    } else {
        // Cria novo cliente
        service.salvar(cliente);
    }
    return "redirect:/cliente";
    }


    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", service.buscarPorId(id).orElseThrow());
        return "cliente/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/cliente";
    }
}
