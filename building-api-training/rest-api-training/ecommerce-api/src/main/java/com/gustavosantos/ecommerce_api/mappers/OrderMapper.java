package com.gustavosantos.ecommerce_api.mappers;

import com.gustavosantos.ecommerce_api.dto.orders.OrderDetailDTO;
import com.gustavosantos.ecommerce_api.dto.orders.OrderItemDTO;
import com.gustavosantos.ecommerce_api.model.Order;
import com.gustavosantos.ecommerce_api.model.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {
    
    private final CustomerMapper customerMapper;
    
    public OrderMapper(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

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

    private List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemDTO)
                .toList();
    }

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        BigDecimal subTotal = calculateItemSubtotal(orderItem);

        return new OrderItemDTO(
                orderItem.getPublicId(),
                orderItem.getProduct(),
                orderItem.getQuantity(),
                orderItem.getValue(),
                subTotal
        );
    }

    private BigDecimal calculateOrderTotal(List<OrderItemDTO> items) {
        return items.stream()
                .map(OrderItemDTO::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateItemSubtotal(OrderItem orderItem) {
        return orderItem.getValue()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
    }
}
