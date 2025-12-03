package com.divide_ai.usergroup.controller;

import com.divide_ai.usergroup.entity.Grupo;
import com.divide_ai.usergroup.entity.Usuario;
import com.divide_ai.usergroup.service.GrupoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@Tag(name = "Grupos", description = "Gerenciamento de grupos para divisão de despesas")
public class GrupoController {
    
    @Autowired
    private GrupoService grupoService;
    
    @GetMapping
    @Operation(summary = "Listar todos os grupos", description = "Retorna uma lista com todos os grupos cadastrados")
    public List<Grupo> listarTodosGrupos() {
        return grupoService.findAll();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar grupo por ID", description = "Retorna um grupo específico pelo seu ID")
    public ResponseEntity<Grupo> buscarGrupoPorId(@PathVariable Long id) {
        return grupoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Criar novo grupo", description = "Cadastra um novo grupo para divisão de despesas")
    public Grupo criarGrupo(@Valid @RequestBody Grupo grupo) {
        return grupoService.save(grupo);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar grupo", description = "Atualiza os dados de um grupo existente")
    public ResponseEntity<Grupo> atualizarGrupo(@PathVariable Long id, @Valid @RequestBody Grupo grupo) {
        return grupoService.findById(id)
                .map(existing -> {
                    grupo.setId(id);
                    return ResponseEntity.ok(grupoService.save(grupo));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{grupoId}/membros/{usuarioId}")
    @Operation(summary = "Adicionar membro ao grupo", description = "Adiciona um usuário como membro de um grupo")
    public ResponseEntity<?> adicionarMembroAoGrupo(@PathVariable Long grupoId, @PathVariable Long usuarioId) {
        try {
            Grupo grupo = grupoService.adicionarMembro(grupoId, usuarioId);
            return ResponseEntity.ok(grupo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao adicionar membro: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/membros")
    @Operation(summary = "Listar membros do grupo", description = "Retorna todos os membros de um grupo específico")
    public ResponseEntity<List<Usuario>> listarMembrosDoGrupo(@PathVariable Long id) {
        return grupoService.findById(id)
                .map(grupo -> ResponseEntity.ok(grupo.getMembros()))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir grupo", description = "Remove um grupo do sistema")
    public ResponseEntity<Void> excluirGrupo(@PathVariable Long id) {
        grupoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}