package com.jmj.restaurante.service;

import com.jmj.restaurante.model.ItemPedido;
import com.jmj.restaurante.model.Pedido;
import com.jmj.restaurante.model.Produto;
import com.jmj.restaurante.model.enums.PedidoStatus;
import com.jmj.restaurante.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        // Ajusta cada item para referenciar o pedido e calcular subtotal
        if (pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                item.setPedido(pedido);

                BigDecimal preco = item.getProduto().getPreco();
                if (preco == null) {
                    throw new RuntimeException(
                        "O produto '" + item.getProduto().getNome() + "' não possui preço definido."
                    );
                }

                item.setSubtotal(preco.multiply(BigDecimal.valueOf(item.getQuantidade())));
            }
        }

        return repository.save(pedido); // salva pedido e itens juntos
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

        BigDecimal preco = produto.getPreco();
        if (preco == null) {
            throw new RuntimeException("O produto '" + produto.getNome() + "' não possui preço definido.");
        }

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setSubtotal(preco.multiply(BigDecimal.valueOf(quantidade)));

        pedido.getItens().add(item);
        return repository.save(pedido); // salva pedido e itens juntos
    }

    @Transactional
    public void removerItem(Long pedidoId, Long itemId) {
        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        boolean removed = pedido.getItens().removeIf(item -> item.getId().equals(itemId));
        if (!removed) {
            throw new RuntimeException("Item do pedido não encontrado");
        }

        repository.save(pedido);
    }

    public void excluir(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        repository.delete(pedido);
    }
}
