package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.Funcionario;
import com.jmj.restaurante.model.enums.FuncionarioRole;
import com.jmj.restaurante.service.FuncionarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("funcionarios", service.listarTodos());
        return "funcionario/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        model.addAttribute("roles", FuncionarioRole.values());
        return "funcionario/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Funcionario funcionario) {
    if (funcionario.getId() != null) {
        // Atualizar funcionário existente
        Funcionario existente = service.buscarPorId(funcionario.getId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        existente.setNome(funcionario.getNome());
        existente.setEmail(funcionario.getEmail());
        existente.setRole(funcionario.getRole());
        service.salvar(existente);
    } else {
        // Criar novo funcionário
        service.salvar(funcionario);
    }
    return "redirect:/funcionario";
}


    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("funcionario", service.buscarPorId(id).orElseThrow());
        model.addAttribute("roles", FuncionarioRole.values());
        return "funcionario/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/funcionario";
    }
}
