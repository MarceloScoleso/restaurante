package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.Restaurante;
import com.jmj.restaurante.service.RestauranteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteService service;

    public RestauranteController(RestauranteService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("restaurantes", service.listarTodos());
        return "restaurante/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("restaurante", new Restaurante());
        return "restaurante/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Restaurante restaurante) {
        service.salvar(restaurante);
        return "redirect:/restaurantes";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("restaurante", service.buscarPorId(id).orElseThrow());
        return "restaurante/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/restaurantes";
    }
}
