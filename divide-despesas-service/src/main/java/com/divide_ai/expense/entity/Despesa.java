package com.divide_ai.expense.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

@DynamoDbBean
@Schema(description = "Entidade que representa uma despesa a ser dividida entre membros de um grupo")
public class Despesa {
    
    @Schema(description = "Identificador único da despesa", example = "desp-123456")
    private String id;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Schema(description = "Descrição da despesa", example = "Jantar no restaurante", required = true)
    private String descricao;
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Schema(description = "Valor total da despesa em reais", example = "120.50", required = true)
    private Double valor;
    
    @NotNull(message = "Grupo é obrigatório")
    @Schema(description = "ID do grupo ao qual a despesa pertence", example = "1", required = true)
    private Long grupoId;
    
    @NotNull(message = "Pagador é obrigatório")
    @Schema(description = "ID do usuário que pagou a despesa", example = "1", required = true)
    private Long pagadorId;
    @Schema(description = "Lista de IDs dos usuários que participaram da despesa", example = "[1, 2, 3]")
    private List<Long> participantesIds;
    @Schema(description = "Tipo de divisão da despesa", example = "IGUAL", allowableValues = {"IGUAL", "PERSONALIZADA", "PORCENTAGEM"})
    private String tipoDivisao;
    @Schema(description = "Data e hora de criação da despesa", example = "2024-01-15T10:30:00Z")
    private Instant dataCreacao;
    
    public Despesa() {
        this.dataCreacao = Instant.now();
        this.tipoDivisao = "IGUAL";
    }
    
    public Despesa(String descricao, Double valor, Long grupoId, Long pagadorId) {
        this();
        this.descricao = descricao;
        this.valor = valor;
        this.grupoId = grupoId;
        this.pagadorId = pagadorId;
    }
    
    @DynamoDbPartitionKey
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    
    public Long getGrupoId() { return grupoId; }
    public void setGrupoId(Long grupoId) { this.grupoId = grupoId; }
    
    public Long getPagadorId() { return pagadorId; }
    public void setPagadorId(Long pagadorId) { this.pagadorId = pagadorId; }
    
    public List<Long> getParticipantesIds() { return participantesIds; }
    public void setParticipantesIds(List<Long> participantesIds) { this.participantesIds = participantesIds; }
    
    public String getTipoDivisao() { return tipoDivisao; }
    public void setTipoDivisao(String tipoDivisao) { this.tipoDivisao = tipoDivisao; }
    
    public Instant getDataCreacao() { return dataCreacao; }
    public void setDataCreacao(Instant dataCreacao) { this.dataCreacao = dataCreacao; }
}