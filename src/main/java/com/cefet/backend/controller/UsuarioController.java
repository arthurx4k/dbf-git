package com.cefet.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.cefet.backend.dto.UsuarioRequestDTO;
import com.cefet.backend.dto.UsuarioResponseDTO;
import com.cefet.backend.dto.UsuarioSenhaRequestDTO;
import com.cefet.backend.entity.Usuario;
import com.cefet.backend.repository.UsuarioRepository;
import com.cefet.backend.security.CurrentUserService;
import com.cefet.backend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuário")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CurrentUserService currentUserService;

    @PostMapping
    @Operation(summary = "Criar usuário - 🌐")
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(dto));
    }

    @GetMapping("/existe")
    @Operation(summary = "Verificar se e-mail já existe - 🌐")
    public ResponseEntity<Boolean> existe(@RequestParam String email) {
        return ResponseEntity.ok(usuarioRepository.findByEmail(email) != null);
    }

    @GetMapping("/meus-dados")
    @Operation(summary = "Meus dados - 🔒")
    public ResponseEntity<UsuarioResponseDTO> meusDados() {
        Usuario u = currentUserService.getUsuario();
        return ResponseEntity.ok(new UsuarioResponseDTO(u));
    }

    @PatchMapping("/senha")
    @Operation(summary = "Alterar minha senha - 🔒")
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody UsuarioSenhaRequestDTO dto) {
        Usuario u = currentUserService.getUsuario();
        u.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuarioRepository.save(u);
        return ResponseEntity.ok().build();
    }
}