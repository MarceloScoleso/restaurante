package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.Usuario;
import com.jmj.restaurante.model.enums.UsuarioRole;
import com.jmj.restaurante.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", service.listarTodos());
        return "usuario/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", UsuarioRole.values());
        return "usuario/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Usuario usuario) {
        service.salvar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", service.buscarPorId(id).orElseThrow());
        model.addAttribute("roles", UsuarioRole.values());
        return "usuario/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/usuarios";
    }
}
