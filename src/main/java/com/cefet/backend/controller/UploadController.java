package com.cefet.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cefet.backend.dto.UploadResponseDTO;
import com.cefet.backend.entity.Arquivo;
import com.cefet.backend.service.FileStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping
@Tag(name = "Upload")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload de arquivo - 🔒 autenticado")
    public ResponseEntity<UploadResponseDTO> upload(@RequestParam("file") MultipartFile file) {
        Arquivo salvo = fileStorageService.salvar(file);
        String url = "/arquivos/" + salvo.getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UploadResponseDTO(salvo.getNomeOriginal(), url));
    }

    @GetMapping("/arquivos/{id}")
    @Operation(summary = "Baixar/visualizar arquivo por ID - 🌐 público")
    public ResponseEntity<byte[]> baixar(@PathVariable Long id) {
        Arquivo a = fileStorageService.buscar(id);

        MediaType tipo = MediaType.APPLICATION_OCTET_STREAM;
        if (a.getContentType() != null) {
            try {
                tipo = MediaType.parseMediaType(a.getContentType());
            } catch (Exception ignored) {}
        }

        return ResponseEntity.ok()
                .contentType(tipo)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + a.getNomeOriginal() + "\"")
                .body(a.getDados());
    }
}