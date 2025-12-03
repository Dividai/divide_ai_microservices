package com.divide_ai.usergroup.service;

import com.divide_ai.usergroup.entity.Usuario;
import com.divide_ai.usergroup.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testFindById() {
        Usuario usuario = new Usuario("João", "joao@email.com", "11999999999");
        usuario.setId(1L);
        
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        
        Optional<Usuario> result = usuarioService.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("João", result.get().getNome());
    }

    @Test
    void testSave() {
        Usuario usuario = new Usuario("Maria", "maria@email.com", "11888888888");
        
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        
        Usuario result = usuarioService.save(usuario);
        
        assertEquals("Maria", result.getNome());
        verify(usuarioRepository).save(usuario);
    }
}