package com.divide_ai.expense.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "Resposta detalhada do cálculo de divisão de uma despesa")
public class DivisaoResponse {
    @Schema(description = "ID único da despesa", example = "desp-123456")
    private String despesaId;
    @Schema(description = "Descrição da despesa", example = "Jantar no restaurante")
    private String descricao;
    @Schema(description = "Valor total da despesa em reais", example = "120.50")
    private Double valorTotal;
    @Schema(description = "Tipo de divisão aplicado", example = "IGUAL", allowableValues = {"IGUAL", "PERSONALIZADA", "PORCENTAGEM"})
    private String tipoDivisao;
    @Schema(description = "Mapa com o valor que cada participante deve pagar (ID do usuário -> valor)", 
            example = "{\"1\": 60.25, \"2\": 60.25}")
    private Map<Long, Double> divisao;
    
    public DivisaoResponse() {}
    
    public DivisaoResponse(String despesaId, String descricao, Double valorTotal, 
                          String tipoDivisao, Map<Long, Double> divisao) {
        this.despesaId = despesaId;
        this.descricao = descricao;
        this.valorTotal = valorTotal;
        this.tipoDivisao = tipoDivisao;
        this.divisao = divisao;
    }
    
    // Getters e Setters
    public String getDespesaId() { return despesaId; }
    public void setDespesaId(String despesaId) { this.despesaId = despesaId; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    
    public String getTipoDivisao() { return tipoDivisao; }
    public void setTipoDivisao(String tipoDivisao) { this.tipoDivisao = tipoDivisao; }
    
    public Map<Long, Double> getDivisao() { return divisao; }
    public void setDivisao(Map<Long, Double> divisao) { this.divisao = divisao; }
}