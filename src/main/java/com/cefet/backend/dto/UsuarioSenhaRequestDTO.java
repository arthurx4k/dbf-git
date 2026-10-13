package com.cefet.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsuarioSenhaRequestDTO {

    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}