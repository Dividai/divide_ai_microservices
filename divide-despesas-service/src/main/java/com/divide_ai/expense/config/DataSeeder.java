package com.divide_ai.expense.config;

import com.divide_ai.expense.entity.Despesa;
import com.divide_ai.expense.repository.DespesaRepositoryMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {
    
    @Autowired
    private DespesaRepositoryMemory despesaRepository;
    
    @Override
    public void run(String... args) {
        // Criar algumas despesas de exemplo para diferentes grupos
        Despesa despesa1 = new Despesa("Jantar no restaurante", 120.50, 1L, 1L);
        despesa1.setParticipantesIds(Arrays.asList(1L, 2L, 3L));
        
        Despesa despesa2 = new Despesa("Combustível viagem", 80.00, 1L, 2L);
        despesa2.setParticipantesIds(Arrays.asList(1L, 2L));
        
        Despesa despesa3 = new Despesa("Pizza da turma", 45.00, 15L, 3L);
        despesa3.setParticipantesIds(Arrays.asList(3L, 4L, 5L));
        
        Despesa despesa4 = new Despesa("Cinema", 60.00, 15L, 4L);
        despesa4.setParticipantesIds(Arrays.asList(3L, 4L));
        
        despesaRepository.save(despesa1);
        despesaRepository.save(despesa2);
        despesaRepository.save(despesa3);
        despesaRepository.save(despesa4);
        
        System.out.println("Dados de exemplo criados!");
    }
}