package com.gustavosantos.ecommerce_api.mappers;

import com.gustavosantos.ecommerce_api.dto.orders.OrderDetailDTO;
import com.gustavosantos.ecommerce_api.dto.orders.OrderItemDTO;
import com.gustavosantos.ecommerce_api.dto.orders.OrderListDTO;
import com.gustavosantos.ecommerce_api.dto.products.ProductSummaryDTO;
import com.gustavosantos.ecommerce_api.model.Order;
import com.gustavosantos.ecommerce_api.model.OrderItem;
import com.gustavosantos.ecommerce_api.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    
    private final CustomerMapper customerMapper;
    
    public OrderMapper(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    /**
     * Converte uma entidade Order para um DTO de detalhes do pedido (OrderDetailDTO).
     */
    public OrderDetailDTO toDetailDTO(Order order) {
        List<OrderItemDTO> items = toOrderItemDTOList(order.getItems());
        BigDecimal total = calculateOrderTotal(items);

        return new OrderDetailDTO(
                order.getPublicId(),
                customerMapper.toSummaryDTO(order.getCustomer()),
                order.getStatus(),
                items,
                total,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    /**
     * Converte uma lista de entidades OrderItem para uma lista de DTOs (OrderItemDTO).
     */
    private List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemDTO)
                .toList();
    }

    /**
     * Converte uma entidade OrderItem para um DTO de item do pedido (OrderItemDTO).
     */
    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        BigDecimal subTotal = calculateItemSubtotal(orderItem);

        return new OrderItemDTO(
                orderItem.getPublicId(),
                toProductSummaryDTO(orderItem.getProduct()),
                orderItem.getQuantity(),
                orderItem.getValue(),
                subTotal
        );
    }

    /**
     * Converte uma entidade Product para um DTO de resumo do produto (ProductSummaryDTO).
     */
    private ProductSummaryDTO toProductSummaryDTO(Product product) {
        return new ProductSummaryDTO(
                product.getPublicId(),
                product.getName(),
                product.getCurrentPrice()
        );
    }

    /**
     * Calcula o valor total do pedido somando os subtotais de todos os itens.
     */
    private BigDecimal calculateOrderTotal(List<OrderItemDTO> items) {
        return items.stream()
                .map(OrderItemDTO::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o subtotal de um item do pedido (quantidade * valor unitário).
     */
    private BigDecimal calculateItemSubtotal(OrderItem orderItem) {
        return orderItem.getValue()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
    }

}
