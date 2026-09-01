package com.gustavosantos.library_api.security;

import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
/*
 * Adapta o User da aplicação para o formato UserDetails esperado pelo Spring Security.
 * Esta classe é útil quando a configuração registra um bean UserDetailsService.
 */
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // O Spring chama este método passando o login informado pelo usuário.
        User user = userService.findByLogin(login);

        /*
         * Aqui a entidade User vira um UserDetails:
         * - username: login usado para autenticar;
         * - password: hash salvo no banco;
         * - roles: papéis usados nas regras @PreAuthorize.
         */
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(Enum::name)
                        .toArray(String[]::new))
                .build();
    }
}
