package com.cefet.backend.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cefet.backend.entity.Arquivo;
import com.cefet.backend.exception.BusinessException;
import com.cefet.backend.exception.ResourceNotFoundException;
import com.cefet.backend.repository.ArquivoRepository;

@Service
public class FileStorageService {

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Transactional
    public Arquivo salvar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Arquivo vazio.");
        }

        try {
            Arquivo a = new Arquivo();
            a.setNomeOriginal(file.getOriginalFilename());
            a.setContentType(file.getContentType());
            a.setTamanho(file.getSize());
            a.setDados(file.getBytes());
            return arquivoRepository.save(a);
        } catch (IOException e) {
            throw new BusinessException("Falha ao ler arquivo: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Arquivo buscar(Long id) {
        return arquivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado. Id: " + id));
    }
}