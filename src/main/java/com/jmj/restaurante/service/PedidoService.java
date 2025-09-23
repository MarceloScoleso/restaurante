package com.jmj.restaurante.service;

import com.jmj.restaurante.model.ItemPedido;
import com.jmj.restaurante.model.Pedido;
import com.jmj.restaurante.model.Produto;
import com.jmj.restaurante.model.enums.PedidoStatus;
import com.jmj.restaurante.repository.ItemPedidoRepository;
import com.jmj.restaurante.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidoService(PedidoRepository repository, ItemPedidoRepository itemPedidoRepository) {
        this.repository = repository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        // Salva o pedido primeiro para ter o ID
        Pedido savedPedido = repository.save(pedido);

        // Salva todos os itens associados
        if (pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                item.setPedido(savedPedido);
                item.setSubtotal(item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
                itemPedidoRepository.save(item);
            }
        }
        return savedPedido;
    }

    @Transactional
    public Pedido atualizarStatus(Long id, PedidoStatus status) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(status);
        return repository.save(pedido);
    }

    @Transactional
    public Pedido adicionarItem(Long pedidoId, Produto produto, Integer quantidade) {
        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setSubtotal(item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));

        itemPedidoRepository.save(item);

        pedido.getItens().add(item); 
        return repository.save(pedido);
    }

    @Transactional
    public void removerItem(Long itemId) {
        ItemPedido item = itemPedidoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item do pedido não encontrado"));
        itemPedidoRepository.delete(item);
    }

    public void excluir(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Exclui todos os itens associados antes do pedido
        if (pedido.getItens() != null) {
            itemPedidoRepository.deleteAll(pedido.getItens());
        }

        repository.deleteById(id);
    }
}
