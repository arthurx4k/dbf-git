package com.cefet.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cefet.backend.entity.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {
}