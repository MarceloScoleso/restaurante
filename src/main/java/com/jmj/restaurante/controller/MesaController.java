package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.Mesa;
import com.jmj.restaurante.service.MesaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mesa")
public class MesaController {

    private final MesaService service;

    public MesaController(MesaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mesas", service.listarTodas());
        return "mesa/lista";
    }

    @GetMapping("/novo")
    public String novaForm(Model model) {
        model.addAttribute("mesa", new Mesa());
        return "mesa/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Mesa mesa) {
        service.salvar(mesa);
        return "redirect:/mesa";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("mesa", service.buscarPorId(id).orElseThrow());
        return "mesa/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/mesa";
    }
}
