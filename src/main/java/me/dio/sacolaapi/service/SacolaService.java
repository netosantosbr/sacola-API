package me.dio.sacolaapi.service;

import me.dio.sacolaapi.model.Item;
import me.dio.sacolaapi.model.Sacola;
import me.dio.sacolaapi.resource.dto.ItemDto;

public interface SacolaService {
    public Item incluirItemNaSacola(ItemDto itemDto);
    public Sacola verSacola(Long id);
    public Sacola fecharSacola(Long id, int formaPagamento);
}
