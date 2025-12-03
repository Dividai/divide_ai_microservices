package com.divide_ai.expense.controller;

import com.divide_ai.expense.service.DespesaService;
import com.divide_ai.expense.dto.DivisaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculos")
@Tag(name = "Cálculos", description = "Endpoints para cálculos de divisão, saldos e estatísticas")
@CrossOrigin(origins = "*")
public class CalculosController {
    
    @Autowired
    private DespesaService despesaService;
    
    @GetMapping("/despesa/{id}/divisao")
    @Operation(
        summary = "Calcular divisão da despesa",
        description = "Calcula como uma despesa específica deve ser dividida entre os participantes"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Divisão calculada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Erro no cálculo da divisão")
    })
    public ResponseEntity<?> calcularDivisao(
        @Parameter(description = "ID da despesa", example = "desp-123456", required = true)
        @PathVariable String id) {
        try {
            return despesaService.findById(id)
                    .map(despesa -> {
                        var divisao = despesaService.calcularDivisao(despesa);
                        var response = new DivisaoResponse(
                            despesa.getId(),
                            despesa.getDescricao(),
                            despesa.getValor(),
                            despesa.getTipoDivisao(),
                            divisao
                        );
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao calcular divisão: " + e.getMessage());
        }
    }
    
    @GetMapping("/grupo/{grupoId}/saldos")
    @Operation(
        summary = "Calcular saldos do grupo",
        description = "Calcula o saldo financeiro de cada membro do grupo"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saldos calculados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Grupo não encontrado")
    })
    public ResponseEntity<?> calcularSaldosGrupo(
        @Parameter(description = "ID do grupo", example = "1", required = true)
        @PathVariable Long grupoId) {
        try {
            var saldos = despesaService.calcularSaldosGrupo(grupoId);
            return ResponseEntity.ok(saldos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao calcular saldos: " + e.getMessage());
        }
    }
    
    @GetMapping("/grupo/{grupoId}/estatisticas")
    @Operation(
        summary = "Estatísticas do grupo",
        description = "Retorna estatísticas completas das despesas do grupo"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estatísticas geradas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Grupo não encontrado")
    })
    public ResponseEntity<?> obterEstatisticasGrupo(
        @Parameter(description = "ID do grupo", example = "1", required = true)
        @PathVariable Long grupoId) {
        try {
            var estatisticas = despesaService.obterEstatisticasGrupo(grupoId);
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao gerar estatísticas: " + e.getMessage());
        }
    }
    
    @PostMapping("/grupo/{grupoId}/processar")
    @Operation(
        summary = "Processar todas as despesas do grupo",
        description = "Processa todas as despesas de um grupo e retorna relatório completo"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Processamento concluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Grupo não encontrado")
    })
    public ResponseEntity<?> processarDespesasGrupo(
        @Parameter(description = "ID do grupo", example = "1", required = true)
        @PathVariable Long grupoId) {
        try {
            var resultado = despesaService.processarTodasDespesasGrupo(grupoId);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar despesas: " + e.getMessage());
        }
    }
}