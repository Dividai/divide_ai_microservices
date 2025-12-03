package com.divide_ai.usergroup.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "grupos")
public class Grupo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    @Column(length = 255)
    private String descricao;
    
    @ManyToMany
    @JoinTable(
        name = "grupo_membros",
        joinColumns = @JoinColumn(name = "grupo_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> membros = new ArrayList<>();
    
    public Grupo() {}
    
    public Grupo(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public List<Usuario> getMembros() { return membros; }
    public void setMembros(List<Usuario> membros) { this.membros = membros; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grupo grupo = (Grupo) o;
        return Objects.equals(id, grupo.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}