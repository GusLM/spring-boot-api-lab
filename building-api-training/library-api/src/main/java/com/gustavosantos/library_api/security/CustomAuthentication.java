package com.gustavosantos.library_api.security;

import com.gustavosantos.library_api.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
/*
 * Representa o usuário já autenticado dentro do Spring Security.
 * O Authentication fica guardado no SecurityContext e pode ser consultado depois
 * por services/controllers para descobrir quem fez a requisição.
 */
public class CustomAuthentication implements Authentication {

    // Entidade de domínio da aplicação; aqui ela vira o principal da autenticação.
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /*
         * Authorities são as permissões/papéis que o Spring usa em expressões como:
         * @PreAuthorize("hasRole('ADMIN')").
         */
        return this.user
                .getRoles()
                .stream()
                .map(userRole -> new SimpleGrantedAuthority(String.valueOf(userRole)))
                .toList();
    }

    @Override
    public @Nullable Object getCredentials() {
        // Depois de autenticar, a senha não deve ficar exposta no objeto de segurança.
        return null;
    }

    @Override
    public @Nullable Object getDetails() {
        // Detalhes extras da autenticação; neste caso, reutiliza o próprio usuário.
        return user;
    }

    @Override
    public @Nullable Object getPrincipal() {
        // Principal é a identidade autenticada. Aqui é a entidade User completa.
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        // Este objeto só é criado após validação bem-sucedida de login e senha.
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // Sem alteração manual de estado: a autenticação é controlada pelo provider customizado.

    }

    @Override
    public String getName() {
        // Nome exibido pelo Spring Security quando alguém chama authentication.getName().
        return user.getLogin();
    }
}
