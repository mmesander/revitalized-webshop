package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderDto;
import nu.revitalized.revitalizedwebshop.models.Order;
import nu.revitalized.revitalizedwebshop.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Transfer Methods
    public static OrderDto orderToDto(Order order) {
        OrderDto orderDto = new OrderDto();

        copyProperties(order, orderDto);

        return orderDto;
    }

    // CRUD Methods
    public List<Order> getAllOrders() {}
    public OrderDto getOrderById() {}
    public List<Order> getALlOrdersByParam() {}
    public List<Order> getAllPayedOrders() {}
    public List<Order> getAllUnpayedOrders() {}
    public OrderDto createOrder() {}
    public OrderDto updateOrder() {}
    public OrderDto patchOrder() {}
    public String deleteOrder() {}


}
