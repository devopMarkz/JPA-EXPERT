package com.algaworks.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SexoCliente {

    MASCULINO("Masculino"),
    FEMININO("Feminino");

    final String descricao;

}
