package com.divide_ai.expense.repository;

import com.divide_ai.expense.entity.Despesa;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class DespesaRepositoryMemory {
    
    private final Map<String, Despesa> despesas = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    
    @PostConstruct
    public void initSampleData() {
        // Add some sample data for testing
        Despesa despesa1 = new Despesa("Jantar no restaurante", 120.0, 1L, 1L);
        despesa1.setParticipantesIds(Arrays.asList(1L, 2L));
        despesa1.setTipoDivisao("IGUAL");
        save(despesa1);
        
        Despesa despesa2 = new Despesa("Compras do supermercado", 85.50, 1L, 2L);
        despesa2.setParticipantesIds(Arrays.asList(1L, 2L, 3L));
        despesa2.setTipoDivisao("IGUAL");
        save(despesa2);
    }
    
    public Despesa save(Despesa despesa) {
        if (despesa.getId() == null || despesa.getId().isEmpty()) {
            despesa.setId("desp-" + idCounter.getAndIncrement());
        }
        despesas.put(despesa.getId(), despesa);
        return despesa;
    }
    
    public Optional<Despesa> findById(String id) {
        return Optional.ofNullable(despesas.get(id));
    }
    
    public List<Despesa> findAll() {
        return new ArrayList<>(despesas.values());
    }
    
    public void deleteById(String id) {
        despesas.remove(id);
    }
}