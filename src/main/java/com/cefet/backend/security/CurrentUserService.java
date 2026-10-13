package com.cefet.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cefet.backend.entity.Usuario;
import com.cefet.backend.exception.BusinessException;
import com.cefet.backend.exception.ResourceNotFoundException;
import com.cefet.backend.repository.UsuarioRepository;

@Service
public class CurrentUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            throw new BusinessException("Usuário não autenticado.");
        }

        Usuario usuario = usuarioRepository.findByEmail(auth.getName());
        if (usuario == null) {
            throw new ResourceNotFoundException("Usuário autenticado não encontrado.");
        }
        return usuario;
    }
}