package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    // Renderiza a página de login quando a configuração usar loginPage("/login").
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String homePage(Authentication authentication) {
        // O Spring injeta a autenticação da requisição atual como parâmetro do controller.
        if (authentication instanceof CustomAuthentication customAuthentication) {
            System.out.println(customAuthentication.getUser());
        }
        return "Hello " + authentication.getName();
    }

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthorizationCode(@RequestParam("code") String code) {
        return code;
    }
}
