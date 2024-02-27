package nu.revitalized.revitalizedwebshop.services;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CreateDate.createDate;
import static nu.revitalized.revitalizedwebshop.specifications.OrderSpecification.*;

import nu.revitalized.revitalizedwebshop.dtos.input.*;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Order;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import nu.revitalized.revitalizedwebshop.repositories.OrderRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final GarmentRepository garmentRepository;
    private final SupplementRepository supplementRepository;

    public OrderService(
            OrderRepository orderRepository,
            SupplementRepository supplementRepository,
            GarmentRepository garmentRepository
    ) {
        this.orderRepository = orderRepository;
        this.supplementRepository = supplementRepository;
        this.garmentRepository = garmentRepository;
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

    public OrderDto updateOrderStatus(OrderStatusInputDto inputDto, Long orderNumber) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(inputDto.getStatus());
            order.setOrderDate(createDate());
            Order updatedOrder = orderRepository.save(order);

            return orderToDto(updatedOrder);
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Order", orderNumber));
        }
    }

    public OrderDto updateOrderPayment(OrderIsPayedInputDto inputDto, Long orderNumber) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setIsPayed(inputDto.getIsPayed());
            order.setOrderDate(createDate());
            Order updatedOrder = orderRepository.save(order);

            return orderToDto(updatedOrder);
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Order", orderNumber));
        }
    }

    public OrderDto updateOrderDiscount(OrderDiscountInputDto inputDto, Long orderNumber) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setDiscountCode(inputDto.getDiscountCode());
            order.setOrderDate(createDate());
            Order updatedOrder = orderRepository.save(order);

            return orderToDto(updatedOrder);
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

    // Relation - Product Methods
    public OrderDto assignProductToOrder(Long orderNumber, Long productId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);

        if (optionalOrder.isEmpty()) {
            throw new BadRequestException(buildIdNotFound("Order", orderNumber));
        }

        Order order = optionalOrder.get();
        OrderDto orderDto;

        if (optionalSupplement.isPresent()) {

        }

    }
}
