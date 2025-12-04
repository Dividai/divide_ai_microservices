package com.divide_ai.usergroup.service;

import com.divide_ai.usergroup.entity.Grupo;
import com.divide_ai.usergroup.entity.Usuario;
import com.divide_ai.usergroup.repository.GrupoRepository;
import com.divide_ai.usergroup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class GrupoService {
    
    @Autowired
    private GrupoRepository grupoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Grupo> findAll() {
        return grupoRepository.findAll();
    }
    
    public Optional<Grupo> findById(Long id) {
        return grupoRepository.findById(id);
    }
    
    public Grupo save(Grupo grupo) {
        return grupoRepository.save(grupo);
    }
    
    @Transactional
    public void deleteById(Long id) {
        if (!grupoRepository.existsById(id)) {
            throw new RuntimeException("Grupo não encontrado com ID: " + id);
        }
        grupoRepository.deleteById(id);
    }
    
    public Grupo adicionarMembro(Long grupoId, Long usuarioId) {
        Grupo grupo = grupoRepository.findById(grupoId)
            .orElseThrow(() -> new RuntimeException("Grupo não encontrado com ID: " + grupoId));
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));
        
        // Verificar se usuário já é membro
        if (grupo.getMembros().contains(usuario)) {
            throw new RuntimeException("Usuário já é membro do grupo");
        }
        
        grupo.getMembros().add(usuario);
        return grupoRepository.save(grupo);
    }
    
    public List<Grupo> findByUsuarioId(Long usuarioId) {
        return grupoRepository.findByMembrosId(usuarioId);
    }
}