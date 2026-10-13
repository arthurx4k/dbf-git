package com.cefet.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cefet.backend.dto.AutenticacaoRequestDTO;
import com.cefet.backend.dto.AutenticacaoResponseDTO;
import com.cefet.backend.entity.Professor;
import com.cefet.backend.entity.Usuario;
import com.cefet.backend.exception.BusinessException;
import com.cefet.backend.repository.ProfessorRepository;
import com.cefet.backend.repository.UsuarioRepository;
import com.cefet.backend.security.JwtService;

@Service
public class AutenticacaoService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProfessorRepository professorRepository;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    public AutenticacaoResponseDTO autenticar(AutenticacaoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail());
        if (usuario == null || !encoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new BusinessException("Login e/ou senha inválidos.");
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
        } catch (AuthenticationException e) {
            throw new BusinessException("Login e/ou senha inválidos.");
        }

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Professor não encontrado para este usuário."));

        AutenticacaoResponseDTO response = new AutenticacaoResponseDTO(usuario);
        response.setProfessorId(professor.getId());
        response.setProfessorNome(professor.getNome());
        response.setFoto(professor.getFoto());
        response.setToken(jwtService.gerarToken(usuario));
        return response;
    }
}