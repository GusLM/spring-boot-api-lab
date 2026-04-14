package com.gustavosantos.ecommerce_api.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T> (
        // Lista com os itens da página atual.
        // Ex.: se você pediu a página 2, aqui vem apenas o conteúdo dessa página,
        // não todos os registros do banco.
        List<T> content,

        // Número da página atual.
        // Importante: em Spring Data, normalmente a contagem começa em 0.
        // Então page = 0 significa "primeira página".
        int page,

        // Quantidade de itens por página.
        // Ex.: size = 10 significa que cada página deve trazer até 10 registros.
        int size,

        // Quantidade total de elementos encontrados na consulta.
        // Isso representa todos os registros que atenderam ao filtro,
        // antes de separar em páginas.
        long totalElements,

        // Quantidade total de páginas disponíveis.
        // É calculada com base em totalElements e size.
        long totalPages,

        // Indica se esta é a primeira página do resultado.
        boolean first,

        // Indica se esta é a última página do resultado.
        boolean last,

        // Indica se a página atual está vazia.
        // Ou seja, não veio nenhum item no content.
        boolean empty
) {

    public static <T> PageResponse<T> from(Page<T> page) {
        // Este método "converte" um Page<T> do Spring Data para o nosso DTO.
        // A ideia é esconder os detalhes internos do Page e expor apenas
        // os dados que queremos devolver na API.
        return new PageResponse<>(
                // Conteúdo da página atual
                page.getContent(),

                // Número da página atual
                page.getNumber(),

                // Tamanho da página
                page.getSize(),

                // Total de registros encontrados
                page.getTotalElements(),

                // Total de páginas calculadas
                page.getTotalPages(),

                // Se é a primeira página
                page.isFirst(),

                // Se é a última página
                page.isLast(),

                // Se a página está vazia
                page.isEmpty()
        );
    }
}
