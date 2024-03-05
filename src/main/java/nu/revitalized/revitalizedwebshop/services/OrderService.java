package nu.revitalized.revitalizedwebshop.services;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CreateDate.createDate;
import static nu.revitalized.revitalizedwebshop.helpers.CalculateTotalAmount.calculateTotalAmount;
import static nu.revitalized.revitalizedwebshop.services.ShippingDetailsService.*;
import static nu.revitalized.revitalizedwebshop.specifications.OrderSpecification.*;
import static nu.revitalized.revitalizedwebshop.services.GarmentService.*;
import static nu.revitalized.revitalizedwebshop.services.SupplementService.*;

import nu.revitalized.revitalizedwebshop.dtos.input.*;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderDto;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderItemDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShortOrderDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.exceptions.UsernameNotFoundException;
import nu.revitalized.revitalizedwebshop.models.*;
import nu.revitalized.revitalizedwebshop.repositories.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final SupplementRepository supplementRepository;
    private final GarmentRepository garmentRepository;
    private final UserRepository userRepository;
    private final ShippingDetailsRepository shippingDetailsRepository;


    public OrderService(
            OrderRepository orderRepository,
            SupplementRepository supplementRepository,
            GarmentRepository garmentRepository,
            UserRepository userRepository,
            ShippingDetailsRepository shippingDetailsRepository
    ) {
        this.orderRepository = orderRepository;
        this.supplementRepository = supplementRepository;
        this.garmentRepository = garmentRepository;
        this.userRepository = userRepository;
        this.shippingDetailsRepository = shippingDetailsRepository;
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

        List<OrderItemDto> orderItemDtos = new ArrayList<>();

        if (order.getSupplements() != null) {
            List<OrderItemDto> orderItems = new ArrayList<>();

            for (Supplement supplement : order.getSupplements()) {
                OrderItemDto orderItemDto = supplementToOrderItemDto(supplement);

                boolean exists = false;
                for (OrderItemDto item : orderItems)
                    if (item.getId() == orderItemDto.getId()) {
                        item.setQuantity(item.getQuantity() + 1);
                        exists = true;
                        break;
                    }

                if (!exists) {
                    orderItems.add(orderItemDto);
                }
            }
            orderItemDtos.addAll(orderItems);
        }

        if (order.getGarments() != null) {
            List<OrderItemDto> orderItems = new ArrayList<>();

            for (Garment garment : order.getGarments()) {
                OrderItemDto orderItemDto = garmentToOrderItemDto(garment);

                boolean exists = false;
                for (OrderItemDto item : orderItems)
                    if (item.getId() == orderItemDto.getId()) {
                        item.setQuantity(item.getQuantity() + 1);
                        exists = true;
                        break;
                    }

                if (!exists) {
                    orderItems.add(orderItemDto);
                }
            }

            orderItemDtos.addAll(orderItems);
        }

        orderDto.setProducts(orderItemDtos);

        if (order.getShippingDetails() != null) {
            orderDto.setShippingDetails(shippingDetailsToShortDto(order.getShippingDetails()));
        }

        if (order.getUser() != null) {
            orderDto.setUsername(order.getUser().getUsername());
        }

        return orderDto;
    }

    public static ShortOrderDto orderToShortDto(Order order) {
        ShortOrderDto shortOrderDto = new ShortOrderDto();

        copyProperties(order, shortOrderDto);

        return shortOrderDto;
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

    public List<OrderDto> getAllPaidOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            if (order.getIsPayed()) {
                OrderDto orderDto = orderToDto(order);
                orderDtos.add(orderDto);
            }
        }

        if (orderDtos.isEmpty()) {
            throw new RecordNotFoundException("No paid orders found");
        } else {
            orderDtos.sort(Comparator.comparing(OrderDto::getOrderNumber));

            return orderDtos;
        }
    }

    public List<OrderDto> getAllUnpaidOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            if (!order.getIsPayed()) {
                OrderDto orderDto = orderToDto(order);
                orderDtos.add(orderDto);
            }
        }

        if (orderDtos.isEmpty()) {
            throw new RecordNotFoundException("No unpaid orders found");
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

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            List<Supplement> supplements = order.getSupplements();

            supplements.add(supplement);
            order.setSupplements(supplements);
            order.setTotalAmount(calculateTotalAmount(order));
            orderRepository.save(order);

            return orderToDto(order);
        } else if (optionalGarment.isPresent()) {
            Garment garment = optionalGarment.get();
            List<Garment> garments = order.getGarments();

            garments.add(garment);
            order.setGarments(garments);
            order.setTotalAmount(calculateTotalAmount(order));
            orderRepository.save(order);

            return orderToDto(order);
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }
    }

    public OrderDto removeProductFromOrder(Long orderNumber, Long productId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);

        if (optionalOrder.isEmpty()) {
            throw new BadRequestException(buildIdNotFound("Order", orderNumber));
        }

        Order order = optionalOrder.get();

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            List<Supplement> supplements = order.getSupplements();

            if (!order.getSupplements().contains(supplement)) {
                throw new BadRequestException("Order with order-number: " + orderNumber
                        + " does not contain product: " + supplement.getName() + " with id: " + productId);
            } else {
                supplements.remove(supplement);
                order.setSupplements(supplements);
                order.setTotalAmount(calculateTotalAmount(order));
                orderRepository.save(order);

                return orderToDto(order);
            }
        } else if (optionalGarment.isPresent()) {
            Garment garment = optionalGarment.get();
            List<Garment> garments = order.getGarments();

            if (!order.getGarments().contains(garment)) {
                throw new BadRequestException("Order with order-number: " + orderNumber
                        + " does not contain product: " + garment.getName() + " with id: " + productId);
            } else {
                garments.remove(garment);
                order.setGarments(garments);
                order.setTotalAmount(calculateTotalAmount(order));
                orderRepository.save(order);

                return orderToDto(order);
            }
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }
    }

    // Relation - User Methods
    public OrderDto assignUserToOrder(Long orderNumber, String username) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalOrder.isEmpty()) {
            throw new RecordNotFoundException(buildIdNotFound("Order", orderNumber));
        }

        if (optionalUser.isPresent()) {
            Order order = optionalOrder.get();
            User user = optionalUser.get();

            List<Order> orders = user.getOrders();

            if (user.getOrders().contains(order)) {
                throw new BadRequestException("Order with order-number: " + orderNumber
                        + " is already assigned to user: " + username);
            } else {
                orders.add(order);
                user.setOrders(orders);
                order.setUser(user);
                orderRepository.save(order);

                return orderToDto(order);
            }
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public OrderDto removeUserFromOrder(Long orderNumber, String username) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalOrder.isEmpty()) {
            throw new RecordNotFoundException(buildIdNotFound("Order", orderNumber));
        }

        if (optionalUser.isPresent()) {
            Order order = optionalOrder.get();
            User presentUser = order.getUser();

            if (presentUser == null || !presentUser.getUsername().equalsIgnoreCase(username)) {
                throw new BadRequestException("Order with order-number: " + orderNumber
                        + " is not assigned to user: " + username);
            } else {
                order.setUser(null);
                orderRepository.save(order);

                return orderToDto(order);
            }
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public OrderDto assignShippingDetailsToOrder(Long orderNumber, Long shippingDetailsId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);
        Optional<ShippingDetails> optionalShippingDetails = shippingDetailsRepository.findById(shippingDetailsId);

        if (optionalOrder.isEmpty()) {
            throw new BadRequestException(buildIdNotFound("Order", orderNumber));
        }

        Order order = optionalOrder.get();

        if (optionalShippingDetails.isPresent()) {
            ShippingDetails shippingDetails = optionalShippingDetails.get();

            if (shippingDetails.getUser() == null) {
                throw new BadRequestException("Shipping details with id: " + shippingDetailsId
                        + " is not assigned to any user, assign shipping details to user first");
            } else if (shippingDetails.getUser() != order.getUser()) {
                throw new BadRequestException("Assign user: " + shippingDetails.getUser().getUsername() +
                        " to current order first if you want to assign shipping details with id: " + shippingDetailsId
                        + " from user: " + shippingDetails.getUser().getUsername() + " to current order");
            } else {
                order.setShippingDetails(shippingDetails);
                orderRepository.save(order);

                return orderToDto(order);
            }
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Shipping Details", shippingDetailsId));
        }
    }

    public OrderDto removeShippingDetailsFromOrder(Long orderNumber, Long shippingDetailsId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNumber);
        Optional<ShippingDetails> optionalShippingDetails = shippingDetailsRepository.findById(shippingDetailsId);

        if (optionalOrder.isEmpty()) {
            throw new BadRequestException(buildIdNotFound("Order", orderNumber));
        }

        Order order = optionalOrder.get();

        if (optionalShippingDetails.isPresent()) {
            if (order.getShippingDetails() == null) {
                throw new BadRequestException("Order with order-number: " + orderNumber
                        + " does not contain any shipping details");
            } else if (!order.getShippingDetails().equals(optionalShippingDetails.get())) {
                throw new BadRequestException("Shipping Details with id: " + shippingDetailsId
                        + " is not assigned to order: " + orderNumber);
            } else {
                order.setShippingDetails(null);
                orderRepository.save(order);

                return orderToDto(order);
            }
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Shipping Details", shippingDetailsId));
        }
    }

    // Relation - Authenticated User Methods
    public List<OrderDto> getAllAuthUserOrders(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        List<Order> orders;
        List<OrderDto> orderDtos = new ArrayList<>();

        if (optionalUser.isPresent()) {
            orders = optionalUser.get().getOrders();
        } else {
            throw new UsernameNotFoundException(username);
        }

        for (Order order : orders) {
            OrderDto orderDto = orderToDto(order);
            orderDtos.add(orderDto);
        }

        if (orderDtos.isEmpty()) {
            throw new RecordNotFoundException("No orders found from user: " + username);
        } else {
            orderDtos.sort(Comparator.comparing(OrderDto::getOrderDate).reversed());
            return orderDtos;
        }
    }
}
