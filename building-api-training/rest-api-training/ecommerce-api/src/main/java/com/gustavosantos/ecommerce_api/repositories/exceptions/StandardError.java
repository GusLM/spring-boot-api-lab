package com.gustavosantos.ecommerce_api.repositories.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Representa a estrutura padrão de resposta para erros da API.
 * Essa classe é usada para devolver ao cliente informações consistentes
 * quando uma exceção acontece, facilitando o consumo da API e a depuração.
 */
@Getter
@Setter
public class StandardError {

    // Data e hora exatas em que o erro aconteceu.
    // O formato abaixo garante que o valor seja serializado como texto legível no JSON.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT")
    private Instant moment;

    // Código HTTP do erro, por exemplo: 404, 500, 400.
    private Integer status;

    // Título resumido do erro, usado para identificação rápida.
    private String error;

    // Mensagem detalhada com mais contexto sobre o problema ocorrido.
    private String message;

    // Caminho da requisição que gerou o erro, útil para rastreamento.
    private String path;

    // Construtor vazio necessário para serialização/deserialização por frameworks.
    public StandardError() {
    }

    // Construtor completo para facilitar a criação do objeto de erro
    // em handlers globais de exceção.
    public StandardError(Instant moment, Integer status, String error, String message, String path) {
        this.moment = moment;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
