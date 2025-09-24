package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.Funcionario;
import com.jmj.restaurante.model.Restaurante;
import com.jmj.restaurante.model.enums.FuncionarioRole;
import com.jmj.restaurante.service.FuncionarioService;
import com.jmj.restaurante.service.RestauranteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
    private final RestauranteService restauranteService;

    public FuncionarioController(FuncionarioService funcionarioService, RestauranteService restauranteService) {
        this.funcionarioService = funcionarioService;
        this.restauranteService = restauranteService;
    }

    // Lista todos os funcionários
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("funcionarios", funcionarioService.listarTodos());
        return "funcionario/lista";
    }

    // Formulário para criar um novo funcionário
    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        model.addAttribute("roles", FuncionarioRole.values());
        model.addAttribute("restaurantes", restauranteService.listarTodos());
        return "funcionario/form";
    }

    // Salvar novo funcionário ou atualizar existente
    @PostMapping
    public String salvar(@ModelAttribute Funcionario funcionario, @RequestParam Long restauranteId) {
        Restaurante restaurante = restauranteService.buscarPorId(restauranteId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        funcionario.setRestaurante(restaurante);

        if (funcionario.getId() != null) {
            Funcionario existente = funcionarioService.buscarPorId(funcionario.getId())
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

            existente.setNome(funcionario.getNome());
            existente.setEmail(funcionario.getEmail());
            existente.setRole(funcionario.getRole());
            existente.setRestaurante(restaurante);

            funcionarioService.salvar(existente);
        } else {
            funcionarioService.salvar(funcionario);
        }

        return "redirect:/funcionario";
    }

    // Formulário de edição de funcionário
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Funcionario funcionario = funcionarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        model.addAttribute("funcionario", funcionario);
        model.addAttribute("roles", FuncionarioRole.values());
        model.addAttribute("restaurantes", restauranteService.listarTodos());
        return "funcionario/form";
    }

    // Excluir funcionário
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        funcionarioService.excluir(id);
        return "redirect:/funcionario";
    }
}
