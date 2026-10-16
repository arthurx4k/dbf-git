package com.cefet.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cefet.backend.exception.BusinessException;

@Service
public class FileStorageService {

    public String salvar(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new BusinessException("Arquivo vazio.");
        }

        try {
            Path pasta = Paths.get("C:\\Users\\Alunos\\Downloads").normalize();

            if (!Files.exists(pasta)) {
                Files.createDirectories(pasta);
            }

            String ext = "";
            String original = arquivo.getOriginalFilename();
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }
            String nome = UUID.randomUUID() + ext;

            Path destino = pasta.resolve(nome);
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return destino.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new BusinessException("Falha ao salvar arquivo: " + e.getMessage());
        }
    }
}