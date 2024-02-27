package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.specifications.OrderSpecification.*;
import nu.revitalized.revitalizedwebshop.dtos.input.OrderInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Order;
import nu.revitalized.revitalizedwebshop.repositories.OrderRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Transfer Methods
    public static Order dtoToOrder(OrderInputDto inputDto) {
        Order order = new Order();

        copyProperties(inputDto, order);

        return order;
    }

    public static OrderDto orderToDto(Order order) {
        OrderDto orderDto = new OrderDto();

        copyProperties(order, orderDto);

        return orderDto;
    }

    // CRUD Methods
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = orderToDto(order);
            orderDtos.add(orderDto);
        }

        if (orderDtos.isEmpty()) {
            throw new RecordNotFoundException("No orders found");
        } else {
            orderDtos.sort(Comparator.comparing(OrderDto::getOrderNumber));

            return orderDtos;
        }
    }

    public OrderDto getOrderByOrderNumber(Long orderNumber) {
        Optional<Order> order = orderRepository.findById(orderNumber);

        if (order.isPresent()) {
            return orderToDto(order.get());
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Order", orderNumber));
        }
    }

    public List<OrderDto> getALlOrdersByParam(
            LocalDate orderDate,
            LocalDate beforeDate,
            LocalDate afterDate,
            Double price,
            Double minPrice,
            Double maxPrice
    ) {
        Specification<Order> params = Specification.where
                (orderDate == null ? null : getOrderDateLikeFilter(orderDate))
                .and(beforeDate == null ? null : getOrderBeforeDateFilter(beforeDate))
                .and(afterDate == null ? null : getOrderAfterDateFilter(afterDate))
                .and(price == null ? null : getOrderPriceLikeFilter(price))
                .and(minPrice == null ? null : getOrderPriceMoreThanFilter(minPrice))
                .and(maxPrice == null ? null : getOrderPriceLessThanFilter(maxPrice));

        List<Order> orders = orderRepository.findAll(params);
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = orderToDto(order);
            orderDtos.add(orderDto);
        }

        if (orderDtos.isEmpty()) {
            throw new RecordNotFoundException("No orders found with the specified filters");
        } else {
            orderDtos.sort(Comparator.comparing(OrderDto::getOrderNumber));

            return orderDtos;
        }
    }

    public List<OrderD> getAllPayedOrders() {}
    public List<Order> getAllUnpayedOrders() {}
    public OrderDto createOrder() {}
    public OrderDto updateOrder() {}
    public OrderDto patchOrder() {}
    public String deleteOrder() {}


}
