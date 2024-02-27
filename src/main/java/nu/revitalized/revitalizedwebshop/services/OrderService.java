package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CreateDate.createDate;
import static nu.revitalized.revitalizedwebshop.specifications.OrderSpecification.*;
import nu.revitalized.revitalizedwebshop.dtos.input.OrderInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Order;
import nu.revitalized.revitalizedwebshop.repositories.OrderRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;

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
            Double price,
            Double minPrice,
            Double maxPrice
    ) {
        Specification<Order> params = Specification.where
                (price == null ? null : getOrderPriceLikeFilter(price))
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

    public List<OrderDto> getAllPayedOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            if (order.getIsPayed()) {
                OrderDto orderDto = orderToDto(order);
                orderDtos.add(orderDto);
            }
        }

        if (orderDtos.isEmpty()) {
            throw new RecordNotFoundException("No payed orders found");
        } else {
            orderDtos.sort(Comparator.comparing(OrderDto::getOrderNumber));

            return orderDtos;
        }
    }

    public List<OrderDto> getAllUnpayedOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            if (!order.getIsPayed()) {
                OrderDto orderDto = orderToDto(order);
                orderDtos.add(orderDto);
            }
        }

        if (orderDtos.isEmpty()) {
            throw new RecordNotFoundException("No unpayed orders found");
        } else {
            orderDtos.sort(Comparator.comparing(OrderDto::getOrderNumber));

            return orderDtos;
        }
    }

    public OrderDto createOrder(OrderInputDto inputDto) {
        Order order = dtoToOrder(inputDto);

        order.setOrderDate(createDate());
        orderRepository.save(order);

        return orderToDto(order);
    }

    public OrderDto updateOrder(OrderInputDto inputDto, Long orderNumber) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            copyProperties(inputDto, order);
            order.setOrderDate(createDate());
            Order updatedOrder = orderRepository.save(order);

            return orderToDto(updatedOrder);
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Order", orderNumber));
        }
    }

    public OrderDto patchOrder(OrderInputDto inputDto, Long orderNumber) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            if (inputDto.getStatus() != null) {
                order.setStatus(inputDto.getStatus());
            }

            if (inputDto.getIsPayed() != null) {
               order.setIsPayed(inputDto.getIsPayed());
            }

            if (inputDto.getDiscountCode() != null) {
                order.setDiscountCode(inputDto.getDiscountCode());
            }

            Order patchedOrder = orderRepository.save(order);

            return orderToDto(patchedOrder);
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Order", orderNumber));
        }
    }

    public String deleteOrder(Long orderNumber) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);

        if (optionalOrder.isPresent()) {
            orderRepository.deleteById(orderNumber);
            return "Order with order number: " + orderNumber + " is removed";
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Order", orderNumber));
        }
    }
}
