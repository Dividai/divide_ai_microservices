package com.divide_ai.expense.service;

import com.divide_ai.expense.entity.Despesa;
import com.divide_ai.expense.repository.DespesaRepositoryMemory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DespesaServiceTest {

    @Mock
    private DespesaRepositoryMemory despesaRepository;

    @InjectMocks
    private DespesaService despesaService;

    @Test
    void testFindById() {
        Despesa despesa = new Despesa("Jantar", 100.00, 1L, 1L);
        despesa.setId("123");
        
        when(despesaRepository.findById("123")).thenReturn(Optional.of(despesa));
        
        Optional<Despesa> result = despesaService.findById("123");
        
        assertTrue(result.isPresent());
        assertEquals("Jantar", result.get().getDescricao());
    }

    @Test
    void testSave() {
        Despesa despesa = new Despesa("Almoço", 50.00, 1L, 1L);
        
        when(despesaRepository.save(despesa)).thenReturn(despesa);
        
        Despesa result = despesaService.save(despesa);
        
        assertEquals("Almoço", result.getDescricao());
        verify(despesaRepository).save(despesa);
    }
}