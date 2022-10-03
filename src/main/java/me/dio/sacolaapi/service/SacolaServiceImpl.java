package me.dio.sacolaapi.service;

import lombok.RequiredArgsConstructor;
import me.dio.sacolaapi.enumeration.FormaPagamento;
import me.dio.sacolaapi.model.Item;
import me.dio.sacolaapi.model.Restaurante;
import me.dio.sacolaapi.model.Sacola;
import me.dio.sacolaapi.repository.ItemRepository;
import me.dio.sacolaapi.repository.ProdutoRepository;
import me.dio.sacolaapi.repository.SacolaRepository;
import me.dio.sacolaapi.resource.dto.ItemDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@RequiredArgsConstructor
@Service
public class SacolaServiceImpl implements SacolaService{
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemRepository itemRepository;

    @Override
    public Item incluirItemNaSacola(ItemDto itemDto) throws RuntimeException{
        Sacola sacola = verSacola(itemDto.getIdSacola());

        List<Item> sacolas = sacola.getItensSacola();
        if(sacola.getFechada()){
            throw new RuntimeException("Esta sacola está fechada!");
        }

        Item itemASerInserido = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola).produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("O produto não existe!");
                        }
                ))
                .build();

        List<Item> itensDaSacola = sacola.getItensSacola();
        if(itensDaSacola.isEmpty()){
            itensDaSacola.add(itemASerInserido);
        } else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteDoItemParaAdicionar = itemASerInserido.getProduto().getRestaurante();
            if(restauranteAtual.equals(restauranteDoItemParaAdicionar)){
                itensDaSacola.add(itemASerInserido);
            } else {
                throw new RuntimeException("Não é possível adicionar produtos de restaurantes diferentes. Feche a sacola ou esvazie.");
            }
        }

        List<Double> valorDosItens = new ArrayList<>();

        for(Item item : itensDaSacola){
            Double valorTotalItem = item.getProduto().getValorUnitario() * item.getQuantidade();
            valorDosItens.add(valorTotalItem);
        }

        Double valorTotalDaSacola = valorDosItens
                .stream()
                .mapToDouble(valorTotalDeCadaItem -> valorTotalDeCadaItem)
                .sum();

        sacola.setValorTotal(valorTotalDaSacola);
        sacolaRepository.save(sacola);
        return itemASerInserido; //itemRepository.save(itemASerInserido);
    }

    @Override
    public Sacola verSacola(Long id) {
         return sacolaRepository.findById(id).orElseThrow(
             () -> {
                 throw new RuntimeException("Essa sacola não existe!");
             }
         );
    }

    @Override
    public Sacola fecharSacola(Long id, int formaPagamento) {
        Sacola sacola = verSacola(id);
        if(sacola.getItensSacola().isEmpty()){
            throw new RuntimeException("Inclua itens na sacola!");
        }
        FormaPagamento formaPagamento1 = formaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINETA;
        sacola.setFormaPagamento(formaPagamento1);
        sacola.setFechada(true);
        return sacolaRepository.save(sacola);
    }
}
