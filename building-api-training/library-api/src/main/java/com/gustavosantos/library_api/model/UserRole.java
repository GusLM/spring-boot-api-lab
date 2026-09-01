package com.gustavosantos.library_api.model;

public enum UserRole {
    // Pode acessar operações administrativas, como cadastrar novos usuários.
    ADMIN,
    // Papel comum da aplicação, liberado para as principais operações de biblioteca.
    USER,
    // Papel de exemplo para usuário autenticado com acesso mais restrito.
    GUEST
}
