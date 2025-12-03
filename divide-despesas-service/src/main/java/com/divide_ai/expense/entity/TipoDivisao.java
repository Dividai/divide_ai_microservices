package com.divide_ai.expense.entity;

public enum TipoDivisao {
    IGUAL("Divisão igual entre todos"),
    PERSONALIZADA("Divisão personalizada por valor"),
    PORCENTAGEM("Divisão por porcentagem");
    
    private final String descricao;
    
    TipoDivisao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}