package com.gustavosantos.library_api.security;

import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
/*
 * Provider responsável por autenticar login e senha manualmente.
 * Quando o Spring recebe um UsernamePasswordAuthenticationToken, ele pergunta
 * a este provider se as credenciais são válidas.
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Dados digitados pelo usuário no formulário/login Basic chegam dentro de Authentication.
        String login = authentication.getName();
        String passwordEntered = Objects.requireNonNull(authentication.getCredentials()).toString();

        // Busca o usuário da aplicação pelo login informado.
        User userFound = userService.findByLogin(login);

        if (userFound == null) {
            throw createUsernameNotFoundException();
        }

        String encryptedPassword = userFound.getPassword();

        // Compara a senha em texto puro enviada na requisição com o hash BCrypt salvo no banco.
        boolean passwordMatch = encoder.matches(passwordEntered, encryptedPassword);

        if (passwordMatch) {
            // Se a senha conferir, devolve uma autenticação já marcada como válida.
            return new CustomAuthentication(userFound);
        }

        throw  createUsernameNotFoundException();
    }

    private UsernameNotFoundException createUsernameNotFoundException() {
        // Mensagem genérica evita revelar se o login existe ou se apenas a senha falhou.
        return new UsernameNotFoundException("Incorrect username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Informa ao Spring que este provider autentica o token padrão de usuário/senha.
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
