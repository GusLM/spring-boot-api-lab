package com.gustavosantos.library_api.config;

import com.gustavosantos.library_api.security.LoginSocialSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// Habilita a infraestrutura de filtros do Spring Security para as requisições HTTP.
@EnableWebSecurity
// Libera o uso de anotações como @PreAuthorize, @Secured e @RolesAllowed nos métodos.
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSuccessHandler socialSuccessHandler) throws Exception {
        /*
         * SecurityFilterChain define a "porta de entrada" da segurança web.
         * Toda requisição passa por essa cadeia de filtros antes de chegar aos controllers.
         */
        return http
                // CSRF costuma ser desabilitado em APIs stateless ou laboratórios de API.
                .csrf(AbstractHttpConfigurer::disable)
                // Configuração de formulário de “login” customizado
                .formLogin(configurer -> {
                    // Define a URL da página de “login” e permite acesso público a ela
                    configurer.loginPage("/login");
                })
                // Ativa o login por formulário usando a tela padrão do Spring Security.
//                .formLogin(Customizer.withDefaults())
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
                .oauth2Login(oauth2 -> {
                    oauth2.loginPage("/login");
                    oauth2.successHandler(socialSuccessHandler);
                })
                .oauth2ResourceServer(oauth2Rs ->
                        oauth2Rs.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        /*
         * Por padrão, hasRole("ADMIN") procura a authority "ROLE_ADMIN".
         * Com prefixo vazio, a aplicação passa a trabalhar diretamente com "ADMIN", "USER", etc.
         */
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        /*
         * O JwtGrantedAuthoritiesConverter extrai as authorities dos claims "scope" ou "scp" do JWT.
         * Por padrão, ele adiciona o prefixo "SCOPE_"; com o prefixo vazio, os valores do token
         * são usados diretamente como authorities, como "ADMIN" e "USER".
         */
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}
