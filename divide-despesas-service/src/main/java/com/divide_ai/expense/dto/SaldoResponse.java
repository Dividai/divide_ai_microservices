package com.divide_ai.expense.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta detalhada do saldo de um usuário em um grupo")
public class SaldoResponse {
    
    @Schema(description = "ID do usuário", example = "1")
    private Long usuarioId;
    
    @Schema(description = "Total pago pelo usuário em reais", example = "320.50")
    private Double totalPago;
    
    @Schema(description = "Total devido pelo usuário em reais", example = "240.25")
    private Double totalDevido;
    
    @Schema(description = "Saldo final (positivo = tem a receber, negativo = deve pagar)", example = "80.25")
    private Double saldoFinal;
    
    @Schema(description = "Status do saldo", example = "CREDOR", allowableValues = {"CREDOR", "DEVEDOR", "QUITADO"})
    private String status;
    
    public SaldoResponse() {}
    
    public SaldoResponse(Long usuarioId, Double totalPago, Double totalDevido, Double saldoFinal) {
        this.usuarioId = usuarioId;
        this.totalPago = totalPago;
        this.totalDevido = totalDevido;
        this.saldoFinal = saldoFinal;
        this.status = saldoFinal > 0 ? "CREDOR" : saldoFinal < 0 ? "DEVEDOR" : "QUITADO";
    }
    
    // Getters e Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public Double getTotalPago() { return totalPago; }
    public void setTotalPago(Double totalPago) { this.totalPago = totalPago; }
    
    public Double getTotalDevido() { return totalDevido; }
    public void setTotalDevido(Double totalDevido) { this.totalDevido = totalDevido; }
    
    public Double getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(Double saldoFinal) { 
        this.saldoFinal = saldoFinal;
        this.status = saldoFinal > 0 ? "CREDOR" : saldoFinal < 0 ? "DEVEDOR" : "QUITADO";
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}