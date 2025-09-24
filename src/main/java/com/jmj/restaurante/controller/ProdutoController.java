package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.Produto;
import com.jmj.restaurante.service.ProdutoService;
import com.jmj.restaurante.service.CategoriaProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService service;
    private final CategoriaProdutoService categoriaService; // 👈 necessário

    public ProdutoController(ProdutoService service, CategoriaProdutoService categoriaService) {
        this.service = service;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", service.listarTodos());
        return "produto/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("categorias", categoriaService.listarTodos()); // 👈 lista de categorias
        return "produto/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Produto produto) {
        service.salvar(produto);
        return "redirect:/produto";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("produto", service.buscarPorId(id).orElseThrow());
        model.addAttribute("categorias", categoriaService.listarTodos()); // 👈 lista de categorias tbm
        return "produto/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/produto";
    }
}
