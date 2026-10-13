package com.cefet.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cefet.backend.dto.UploadResponseDTO;
import com.cefet.backend.service.FileStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/uploads")
@Tag(name = "Upload")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload de arquivo (imagem/PDF) - 🔒 autenticado")
    public ResponseEntity<UploadResponseDTO> upload(@RequestParam("file") MultipartFile file) {
        String nome = fileStorageService.salvar(file);
        String url = "/arquivos/" + nome;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UploadResponseDTO(nome, url));
    }
}