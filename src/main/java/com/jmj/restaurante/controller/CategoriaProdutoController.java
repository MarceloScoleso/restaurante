package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.CategoriaProduto;
import com.jmj.restaurante.service.CategoriaProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categoria")
public class CategoriaProdutoController {

    private final CategoriaProdutoService service;

    public CategoriaProdutoController(CategoriaProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", service.listarTodos());
        return "categoria/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("categoria", new CategoriaProduto());
        return "categoria/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute CategoriaProduto categoria) {
        if (categoria.getId() != null) {
            // Categoria existente, buscar e atualizar
            CategoriaProduto existente = service.buscarPorId(categoria.getId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            existente.setNome(categoria.getNome());
            service.salvar(existente);
        } else {
            // Nova categoria
            service.salvar(categoria);
        }
        return "redirect:/categoria";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", service.buscarPorId(id).orElseThrow());
        return "categoria/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/categoria";
    }
}
