package com.divide_ai.expense.controller;

import com.divide_ai.expense.entity.Despesa;
import com.divide_ai.expense.service.DespesaService;
import com.divide_ai.expense.dto.DivisaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/despesas")
@Tag(name = "Despesas", description = "API completa para gerenciamento de despesas, cálculos de divisão e controle de saldos entre membros de grupos")
@CrossOrigin(origins = "*")
@Validated
public class DespesaController {
    
    @Autowired
    private DespesaService despesaService;
    
    @GetMapping({"", "/"})
    @Operation(
        summary = "Listar todas as despesas",
        description = "Retorna uma lista paginada com todas as despesas cadastradas no sistema, incluindo informações de valor, grupo e participantes"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de despesas retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Despesa.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<Despesa> listarTodasDespesas() {
        return despesaService.findAll();
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar despesa por ID",
        description = "Retorna os detalhes completos de uma despesa específica, incluindo valor, participantes e tipo de divisão"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Despesa encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido fornecido")
    })
    public ResponseEntity<Despesa> buscarDespesaPorId(
        @Parameter(description = "ID único da despesa", example = "desp-123456", required = true)
        @PathVariable String id) {
        return despesaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/grupo/{grupoId}")
    @Operation(
        summary = "Listar despesas do grupo",
        description = "Retorna todas as despesas associadas a um grupo específico, ordenadas por data de criação"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de despesas do grupo retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Grupo não encontrado"),
        @ApiResponse(responseCode = "400", description = "ID do grupo inválido")
    })
    public List<Despesa> listarDespesasDoGrupo(
        @Parameter(description = "ID do grupo", example = "1", required = true)
        @PathVariable Long grupoId) {
        return despesaService.findByGrupoId(grupoId);
    }
    
    @PostMapping({"", "/"})
    @Operation(
        summary = "Registrar nova despesa",
        description = "Cadastra uma nova despesa no sistema. A despesa será automaticamente associada ao grupo e poderá ser dividida entre os participantes especificados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Despesa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "422", description = "Erro de validação dos dados")
    })
    public ResponseEntity<Despesa> registrarDespesa(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados da nova despesa",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Despesa.class),
                examples = @ExampleObject(
                    name = "Exemplo de despesa",
                    value = "{\"descricao\": \"Jantar no restaurante\", \"valor\": 120.50, \"grupoId\": 1, \"pagadorId\": 1, \"participantesIds\": [1, 2], \"tipoDivisao\": \"IGUAL\"}"
                )
            )
        )
        @RequestBody Despesa despesa) {
        try {
            // Basic validation
            if (despesa.getDescricao() == null || despesa.getDescricao().trim().isEmpty()) {
                throw new IllegalArgumentException("Descrição é obrigatória");
            }
            if (despesa.getValor() == null || despesa.getValor() <= 0) {
                throw new IllegalArgumentException("Valor deve ser positivo");
            }
            if (despesa.getGrupoId() == null) {
                throw new IllegalArgumentException("Grupo é obrigatório");
            }
            if (despesa.getPagadorId() == null) {
                throw new IllegalArgumentException("Pagador é obrigatório");
            }
            
            return ResponseEntity.ok(despesaService.save(despesa));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar despesa",
        description = "Atualiza os dados de uma despesa existente. A data de criação original é preservada"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<Despesa> atualizarDespesa(
        @Parameter(description = "ID da despesa a ser atualizada", example = "desp-123456", required = true)
        @PathVariable String id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Novos dados da despesa",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Despesa.class))
        )
        @RequestBody Despesa despesa) {
        try {
            return despesaService.findById(id)
                    .map(existing -> {
                        despesa.setId(id);
                        despesa.setDataCreacao(existing.getDataCreacao()); // Manter data original
                        return ResponseEntity.ok(despesaService.save(despesa));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Erro ao atualizar despesa: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping({"/{id}", "/{id}/"})
    @Operation(
        summary = "Excluir despesa",
        description = "Remove permanentemente uma despesa do sistema. Esta ação não pode ser desfeita"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido fornecido")
    })
    public ResponseEntity<Void> excluirDespesa(
        @Parameter(description = "ID da despesa a ser excluída", example = "desp-123456", required = true)
        @PathVariable String id) {
        despesaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/health")
    @Operation(
        summary = "Health Check",
        description = "Endpoint de monitoramento para verificar se o serviço de despesas está operacional"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Serviço funcionando corretamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "object")))
    })
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Serviço de Despesas está funcionando!");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/usuario/{usuarioId}")
    @Operation(
        summary = "Listar despesas do usuário",
        description = "Retorna todas as despesas em que o usuário participou, seja como pagador ou como participante da divisão"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de despesas do usuário retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "ID do usuário inválido")
    })
    public List<Despesa> listarDespesasDoUsuario(
        @Parameter(description = "ID do usuário", example = "1", required = true)
        @PathVariable Long usuarioId) {
        return despesaService.findByUsuarioId(usuarioId);
    }
}