package com.gustavosantos.library_api.security;

import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
/*
 * Serviço auxiliar para consultar o usuário autenticado no fluxo de negócio.
 * Ele evita espalhar acesso direto ao SecurityContextHolder por vários services.
 */
public class SecurityService {

    private final UserService userService;

    public User getLoggedUser() {
        // SecurityContextHolder armazena a Authentication criada pelo Spring para a requisição atual.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;

        if (authentication instanceof CustomAuthentication customAuth) {
            return customAuth.getUser();
         }

        return null;
    }
}
