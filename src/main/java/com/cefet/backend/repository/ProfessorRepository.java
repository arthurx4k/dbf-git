package com.cefet.backend.repository;

import com.cefet.backend.entity.Professor;
import com.cefet.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByUsuario(Usuario usuario);

    @Query("SELECT p FROM Professor p WHERE p.usuario.cargo = 'PROFESSOR'")
    List<Professor> findAllProfessores();
}