package com.cefet.backend.dto;

import lombok.Getter;

@Getter
public class UploadResponseDTO {
    private String nomeArquivo;
    private String url;

    public UploadResponseDTO(String nomeArquivo, String url) {
        this.nomeArquivo = nomeArquivo;
        this.url = url;
    }
}