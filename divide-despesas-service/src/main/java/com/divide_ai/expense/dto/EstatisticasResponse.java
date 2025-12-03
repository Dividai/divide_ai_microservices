package com.divide_ai.expense.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "Estatísticas detalhadas das despesas de um grupo")
public class EstatisticasResponse {
    
    @Schema(description = "Número total de despesas do grupo", example = "15")
    private Integer totalDespesas;
    
    @Schema(description = "Valor total de todas as despesas em reais", example = "1250.75")
    private Double valorTotal;
    
    @Schema(description = "Valor médio das despesas em reais", example = "83.38")
    private Double valorMedio;
    
    @Schema(description = "Maior despesa registrada em reais", example = "200.00")
    private Double maiorDespesa;
    
    @Schema(description = "Menor despesa registrada em reais", example = "15.50")
    private Double menorDespesa;
    
    @Schema(description = "Distribuição de despesas por tipo de divisão", 
            example = "{\"IGUAL\": 10, \"PERSONALIZADA\": 3, \"PORCENTAGEM\": 2}")
    private Map<String, Long> porTipoDivisao;
    
    @Schema(description = "Total gasto por cada usuário (ID do usuário -> valor total)", 
            example = "{\"1\": 450.25, \"2\": 380.50, \"3\": 420.00}")
    private Map<Long, Double> gastosPorUsuario;
    
    @Schema(description = "ID do usuário que mais gastou", example = "1")
    private Long maiorGastadorId;
    
    @Schema(description = "Valor total gasto pelo maior gastador", example = "450.25")
    private Double maiorGastadorValor;
    
    public EstatisticasResponse() {}
    
    // Getters e Setters
    public Integer getTotalDespesas() { return totalDespesas; }
    public void setTotalDespesas(Integer totalDespesas) { this.totalDespesas = totalDespesas; }
    
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    
    public Double getValorMedio() { return valorMedio; }
    public void setValorMedio(Double valorMedio) { this.valorMedio = valorMedio; }
    
    public Double getMaiorDespesa() { return maiorDespesa; }
    public void setMaiorDespesa(Double maiorDespesa) { this.maiorDespesa = maiorDespesa; }
    
    public Double getMenorDespesa() { return menorDespesa; }
    public void setMenorDespesa(Double menorDespesa) { this.menorDespesa = menorDespesa; }
    
    public Map<String, Long> getPorTipoDivisao() { return porTipoDivisao; }
    public void setPorTipoDivisao(Map<String, Long> porTipoDivisao) { this.porTipoDivisao = porTipoDivisao; }
    
    public Map<Long, Double> getGastosPorUsuario() { return gastosPorUsuario; }
    public void setGastosPorUsuario(Map<Long, Double> gastosPorUsuario) { this.gastosPorUsuario = gastosPorUsuario; }
    
    public Long getMaiorGastadorId() { return maiorGastadorId; }
    public void setMaiorGastadorId(Long maiorGastadorId) { this.maiorGastadorId = maiorGastadorId; }
    
    public Double getMaiorGastadorValor() { return maiorGastadorValor; }
    public void setMaiorGastadorValor(Double maiorGastadorValor) { this.maiorGastadorValor = maiorGastadorValor; }
}