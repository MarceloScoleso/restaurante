package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.ItemPedido;
import com.jmj.restaurante.model.Pedido;
import com.jmj.restaurante.model.Produto;
import com.jmj.restaurante.model.enums.PedidoStatus;
import com.jmj.restaurante.service.PedidoService;
import com.jmj.restaurante.service.ProdutoService;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;

    public PedidoController(PedidoService pedidoService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "pedido/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("status", PedidoStatus.values());
        model.addAttribute("produtos", produtoService.listarTodos());
        return "pedido/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Pedido pedido, @RequestParam(required = false) Long[] produtoIds,
                        @RequestParam(required = false) Integer[] quantidades) {

        if (produtoIds != null && quantidades != null) {
            for (int i = 0; i < produtoIds.length; i++) {
                Produto produto = produtoService.buscarPorId(produtoIds[i])
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
                ItemPedido item = new ItemPedido();
                item.setProduto(produto);
                item.setQuantidade(quantidades[i]);
                BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidades[i]));
                item.setSubtotal(subtotal);
                pedido.getItens().add(item);
            }
        }

        pedidoService.salvar(pedido);
        return "redirect:/pedidos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.buscarPorId(id).orElseThrow();
        model.addAttribute("pedido", pedido);
        model.addAttribute("status", PedidoStatus.values());
        model.addAttribute("produtos", produtoService.listarTodos());
        return "pedido/form";
    }

    @PostMapping("/atualizarStatus/{id}")
    public String atualizarStatus(@PathVariable Long id, @RequestParam PedidoStatus status) {
        pedidoService.atualizarStatus(id, status);
        return "redirect:/pedidos";
    }

    @PostMapping("/adicionarItem/{pedidoId}")
    public String adicionarItem(@PathVariable Long pedidoId,
                                @RequestParam Long produtoId,
                                @RequestParam Integer quantidade) {
        Produto produto = produtoService.buscarPorId(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        pedidoService.adicionarItem(pedidoId, produto, quantidade);
        return "redirect:/pedidos/editar/" + pedidoId;
    }

    @GetMapping("/removerItem/{pedidoId}/{itemId}")
    public String removerItem(@PathVariable Long pedidoId, @PathVariable Long itemId) {
        pedidoService.removerItem(itemId);
        return "redirect:/pedidos/editar/" + pedidoId;
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        pedidoService.excluir(id);
        return "redirect:/pedidos";
    }
}
