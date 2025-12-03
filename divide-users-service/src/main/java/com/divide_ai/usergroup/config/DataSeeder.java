package com.divide_ai.usergroup.config;

import com.divide_ai.usergroup.entity.Grupo;
import com.divide_ai.usergroup.entity.Usuario;
import com.divide_ai.usergroup.repository.GrupoRepository;
import com.divide_ai.usergroup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private GrupoRepository grupoRepository;
    
    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            // Criar usuários de exemplo
            Usuario user1 = new Usuario("João Silva", "joao@email.com", "11999999999");
            Usuario user2 = new Usuario("Maria Santos", "maria@email.com", "11888888888");
            Usuario user3 = new Usuario("Pedro Costa", "pedro@email.com", "11777777777");
            Usuario user4 = new Usuario("Ana Oliveira", "ana@email.com", "11666666666");
            Usuario user5 = new Usuario("Carlos Lima", "carlos@email.com", "11555555555");
            
            usuarioRepository.save(user1);
            usuarioRepository.save(user2);
            usuarioRepository.save(user3);
            usuarioRepository.save(user4);
            usuarioRepository.save(user5);
            
            // Criar grupos de exemplo
            Grupo grupo1 = new Grupo("Viagem Praia", "Grupo para dividir gastos da viagem");
            grupo1.getMembros().add(user1);
            grupo1.getMembros().add(user2);
            grupo1.getMembros().add(user3);
            
            Grupo grupo2 = new Grupo("Apartamento Compartilhado", "Divisão de contas do apartamento");
            grupo2.getMembros().add(user3);
            grupo2.getMembros().add(user4);
            grupo2.getMembros().add(user5);
            
            grupoRepository.save(grupo1);
            grupoRepository.save(grupo2);
            
            System.out.println("Dados de exemplo criados para usuários e grupos!");
        }
    }
}