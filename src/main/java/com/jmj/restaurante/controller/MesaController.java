package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.Mesa;
import com.jmj.restaurante.model.Restaurante;
import com.jmj.restaurante.service.MesaService;
import com.jmj.restaurante.service.RestauranteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mesa")
public class MesaController {

    private final MesaService service;
    private final RestauranteService restauranteService;

    public MesaController(MesaService service, RestauranteService restauranteService) {
        this.service = service;
        this.restauranteService = restauranteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mesas", service.listarTodas());
        return "mesa/lista";
    }

    @GetMapping("/novo")
    public String novaForm(Model model) {
        model.addAttribute("mesa", new Mesa());
        model.addAttribute("restaurantes", restauranteService.listarTodos());
        return "mesa/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Mesa mesa = service.buscarPorId(id).orElseThrow(() -> new RuntimeException("Mesa não encontrada"));
        model.addAttribute("mesa", mesa);
        model.addAttribute("restaurantes", restauranteService.listarTodos());
        return "mesa/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Mesa mesa) {
        // Buscar restaurante pelo ID e associar
        Restaurante restaurante = restauranteService
            .buscarPorId(mesa.getRestaurante().getId())
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
        mesa.setRestaurante(restaurante);

        // Salva ou atualiza a mesa conforme o ID
        service.salvar(mesa);

        return "redirect:/mesa";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/mesa";
    }
}
