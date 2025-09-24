package com.jmj.restaurante.controller;

import com.jmj.restaurante.model.ItemPedido;
import com.jmj.restaurante.model.Pedido;
import com.jmj.restaurante.model.enums.PedidoStatus;
import com.jmj.restaurante.service.PedidoService;
import com.jmj.restaurante.service.ProdutoService;
import com.jmj.restaurante.service.ClienteService;
import com.jmj.restaurante.service.MesaService;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final MesaService mesaService;

    public PedidoController(PedidoService pedidoService,
                            ProdutoService produtoService,
                            ClienteService clienteService,
                            MesaService mesaService) {
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
        this.mesaService = mesaService;
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
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("mesas", mesaService.listarTodas());
        return "pedido/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        model.addAttribute("pedido", pedido);
        model.addAttribute("status", PedidoStatus.values());
        model.addAttribute("produtos", produtoService.listarTodos());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("mesas", mesaService.listarTodas());
        return "pedido/form";
    }

    @PostMapping
public String salvar(@RequestParam(required = false) Long id,
                     @RequestParam Long clienteId,
                     @RequestParam Long mesaId,
                     @RequestParam PedidoStatus status,
                     @RequestParam Long produtoId,
                     @RequestParam Integer quantidade) {

    var cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    var mesa = mesaService.buscarPorId(mesaId)
            .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));
    var produto = produtoService.buscarPorId(produtoId)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

    Pedido pedido;

    if (id != null) {
        pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // limpar itens existentes
        pedido.getItens().clear(); 
    } else {
        pedido = new Pedido();
    }

    pedido.setCliente(cliente);
    pedido.setMesa(mesa);
    pedido.setStatus(status);

    // criar item
    ItemPedido item = new ItemPedido();
    item.setProduto(produto);
    item.setQuantidade(quantidade);
    item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));
    item.setPedido(pedido);

    // adicionar na mesma lista
    pedido.getItens().add(item);

    pedidoService.salvar(pedido);

    return "redirect:/pedidos";
}

    @PostMapping("/atualizarStatus/{id}")
    public String atualizarStatus(@PathVariable Long id, @RequestParam PedidoStatus status) {
        pedidoService.atualizarStatus(id, status);
        return "redirect:/pedidos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        pedidoService.excluir(id);
        return "redirect:/pedidos";
    }
}
