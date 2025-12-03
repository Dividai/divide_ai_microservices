package com.divide_ai.usergroup.repository;

import com.divide_ai.usergroup.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    
    @Query("SELECT g FROM Grupo g JOIN g.membros m WHERE m.id = :usuarioId")
    List<Grupo> findByMembrosId(@Param("usuarioId") Long usuarioId);
}