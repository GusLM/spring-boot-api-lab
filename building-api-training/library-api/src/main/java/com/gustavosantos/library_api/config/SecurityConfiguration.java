package com.gustavosantos.library_api.config;

import com.gustavosantos.library_api.security.CustomUserDetailsService;
import com.gustavosantos.library_api.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// Habilita a infraestrutura de filtros do Spring Security para as requisições HTTP.
@EnableWebSecurity
// Libera o uso de anotações como @PreAuthorize, @Secured e @RolesAllowed nos métodos.
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
         * SecurityFilterChain define a "porta de entrada" da segurança web.
         * Toda requisição passa por essa cadeia de filtros antes de chegar aos controllers.
         */
        return http
                // CSRF costuma ser desabilitado em APIs stateless ou laboratórios de API.
                .csrf(AbstractHttpConfigurer::disable)
                // Configuração de formulário de “login” customizado
//                .formLogin(configurer -> {
//                    // Define a URL da página de “login” e permite acesso público a ela
//                    configurer.loginPage("/login");
//                })
                // Ativa o login por formulário usando a tela padrão do Spring Security.
                .formLogin(Customizer.withDefaults())
                // Também permite autenticação HTTP Basic, útil para testar a API por Postman/cURL.
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> {
                    // A página de login precisa ser pública; caso contrário o usuário nunca conseguiria acessá-la.
                    authorize.requestMatchers("/login/**").permitAll();
                    // Qualquer outra rota exige ao menos um usuário autenticado.
                    // As regras finas de papel/permissão ficam nos controllers via @PreAuthorize.
                    authorize.anyRequest().authenticated();
                })
                // Habilita login social OAuth2 usando os providers configurados em application.yaml.
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt gera hash com salt e custo configurável; aqui o custo 10 equilibra segurança e desempenho.
        return new BCryptPasswordEncoder(10);
    }

//    @Bean
//    public UserDetailsService userDetailsService(UserService userService) {
//        // Alternativa ao CustomAuthenticationProvider: o Spring carregaria o usuário por login
//        // e compararia a senha usando o PasswordEncoder registrado acima.
//        return new CustomUserDetailsService(userService);
//    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        /*
         * Por padrão, hasRole("ADMIN") procura a authority "ROLE_ADMIN".
         * Com prefixo vazio, a aplicação passa a trabalhar diretamente com "ADMIN", "USER", etc.
         */
        return new GrantedAuthorityDefaults("");
    }
}
