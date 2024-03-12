package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.*;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderDto;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderItemDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShortOrderDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.exceptions.UsernameNotFoundException;
import nu.revitalized.revitalizedwebshop.models.*;
import nu.revitalized.revitalizedwebshop.repositories.*;
import nu.revitalized.revitalizedwebshop.specifications.OrderSpecification;
import org.springframework.stereotype.Service;
import java.util.*;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CalculateTotalAmount.calculateTotalAmount;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.CreateDate.createDate;
import static nu.revitalized.revitalizedwebshop.services.GarmentService.garmentToOrderItemDto;
import static nu.revitalized.revitalizedwebshop.services.ShippingDetailsService.shippingDetailsToShortDto;
import static nu.revitalized.revitalizedwebshop.services.SupplementService.supplementToOrderItemDto;

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
                    if (Objects.equals(item.getId(), orderItemDto.getId())) {
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
                    if (Objects.equals(item.getId(), orderItemDto.getId())) {
                        item.setQuantity(item.getQuantity() + 1);
                        exists = true;
                        break;
                    }

                if (!exists) {
                    orderItems.add(orderItemDto);
                }
            }

            orderItemDtos.addAll(orderItems);
            orderItemDtos.sort(Comparator.comparing(OrderItemDto::getId));
        }

        orderDto.setProducts(orderItemDtos);
        orderDto.setTotalAmount(calculateTotalAmount(order));

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
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        return orderToDto(order);
    }

    public List<OrderDto> getALlOrdersByParam(
            Double minTotalAmount,
            Double maxTotalAmount
            ) {
        OrderSpecification params = new OrderSpecification(
                minTotalAmount, maxTotalAmount);

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
            if (order.getIsPaid()) {
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
            if (!order.getIsPaid()) {
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
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        copyProperties(inputDto, order);
        order.setOrderDate(createDate());
        Order updatedOrder = orderRepository.save(order);

        return orderToDto(updatedOrder);
    }

    public OrderDto updateOrderStatus(OrderStatusInputDto inputDto, Long orderNumber) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        order.setStatus(inputDto.getStatus());
        order.setOrderDate(createDate());
        Order updatedOrder = orderRepository.save(order);

        return orderToDto(updatedOrder);
    }

    public OrderDto updateOrderPayment(OrderIsPaidInputDto inputDto, Long orderNumber) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        order.setIsPaid(inputDto.getIsPaid());
        order.setOrderDate(createDate());
        Order updatedOrder = orderRepository.save(order);

        return orderToDto(updatedOrder);
    }

    public OrderDto updateOrderDiscount(OrderDiscountInputDto inputDto, Long orderNumber) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        if (order.getUser() == null) {
            throw new BadRequestException("Assign user to order first before assigning a discount to order");
        }

        boolean hasDiscount = false;
        for (Discount discount : order.getUser().getDiscounts()) {
            if (inputDto.getDiscountCode().equalsIgnoreCase(discount.getName())) {
                hasDiscount = true;
                break;
            }
        }
        if (!hasDiscount) {
            throw new BadRequestException("User: " + order.getUser().getUsername() + " does not have discount: "
                    + inputDto.getDiscountCode());
        }

        order.setDiscountCode(inputDto.getDiscountCode());
        order.setOrderDate(createDate());
        order.setTotalAmount(calculateTotalAmount(order));
        Order updatedOrder = orderRepository.save(order);

        return orderToDto(updatedOrder);
    }

    public String deleteOrder(Long orderNumber) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        if (order.getUser() != null) {
            throw new BadRequestException("Order with order number: " + orderNumber + " is assigned to user: "
                    + order.getUser().getUsername() + " unassign user first before delete this order");
        }

        orderRepository.deleteById(orderNumber);

        return "Order with order number: " + order.getOrderNumber() + " is deleted";
    }

    // Relation - Product Methods
    public OrderDto assignProductToOrder(Long orderNumber, Long productId) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        if (supplementRepository.existsById(productId)) {
            Supplement supplement = supplementRepository.findById(productId).orElseThrow();
            order.getSupplements().add(supplement);
        } else if (garmentRepository.existsById(productId)) {
            Garment garment = garmentRepository.findById(productId).orElseThrow();
            order.getGarments().add(garment);
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }

        order.setTotalAmount(calculateTotalAmount(order));
        orderRepository.save(order);

        return orderToDto(order);
    }

    public OrderDto assignMultipleProductsToOrder(Long orderNumber, List<Long> productIds) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        List<Supplement> newSupplements = new ArrayList<>();
        List<Garment> newGarments = new ArrayList<>();

        for (Long productId : productIds) {
            Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
            Optional<Garment> optionalGarment = garmentRepository.findById(productId);

            if (optionalSupplement.isPresent()) {
                newSupplements.add(optionalSupplement.get());
            } else if (optionalGarment.isPresent()) {
                newGarments.add(optionalGarment.get());
            } else {
                throw new BadRequestException(buildIdNotFound("Product", productId));
            }
        }

        order.getSupplements().addAll(newSupplements);
        order.getGarments().addAll(newGarments);
        order.setTotalAmount(calculateTotalAmount(order));
        orderRepository.save(order);

        return orderToDto(order);
    }

    public OrderDto removeMultipleProductsFromOrder(Long orderNumber, List<Long> productIds) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        for (Long productId : productIds) {
            if (supplementRepository.existsById(productId)) {
                Supplement supplement = supplementRepository.findById(productId).orElseThrow();
                if (!order.getSupplements().remove(supplement)) {
                    throw new BadRequestException("Order with order-number: " + orderNumber
                            + " does not contain product: " + supplement.getName() + " with id: " + productId);
                }
            } else if (garmentRepository.existsById(productId)) {
                Garment garment = garmentRepository.findById(productId).orElseThrow();
                if (!order.getGarments().remove(garment)) {
                    throw new BadRequestException("Order with order-number: " + orderNumber
                            + " does not contain product: " + garment.getName() + " with id: " + productId);
                }
            } else {
                throw new BadRequestException(buildIdNotFound("Product", productId));
            }
        }

        order.setTotalAmount(calculateTotalAmount(order));
        orderRepository.save(order);

        return orderToDto(order);
    }

    public OrderDto removeProductFromOrder(Long orderNumber, Long productId) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        if (supplementRepository.existsById(productId)) {
            Supplement supplement = supplementRepository.findById(productId).orElseThrow();
            if (!order.getSupplements().remove(supplement)) {
                throw new BadRequestException("Order with order-number: " + orderNumber
                        + " does not contain product: " + supplement.getName() + " with id: " + productId);
            }
        } else if (garmentRepository.existsById(productId)) {
            Garment garment = garmentRepository.findById(productId).orElseThrow();
            if (!order.getGarments().remove(garment)) {
                throw new BadRequestException("Order with order-number: " + orderNumber
                        + " does not contain product: " + garment.getName() + " with id: " + productId);
            }
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }

        order.setTotalAmount(calculateTotalAmount(order));
        orderRepository.save(order);

        return orderToDto(order);
    }

    // Relation - User Methods
    public OrderDto assignUserToOrder(Long orderNumber, String username) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (order.getUser() != null) {
            if (user.getOrders().contains(order)) {
                throw new BadRequestException("Order with order-number: " + orderNumber
                        + " is already assigned to user: " + username);
            } else {
                throw new BadRequestException("Order with order-number: " + orderNumber
                        + " is already assigned to user: " + order.getUser().getUsername());
            }
        }

        order.setUser(user);
        orderRepository.save(order);

        return orderToDto(order);
    }

    public OrderDto removeUserFromOrder(Long orderNumber, String username) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (order.getUser() == null || !order.getUser().equals(user)) {
            throw new BadRequestException("Order with order-number: " + orderNumber
                    + " is not assigned to user: " + username);
        }

        if (order.getDiscountCode() != null) {
            order.setDiscountCode(null);
            order.setTotalAmount(calculateTotalAmount(order));
        }

        if (order.getShippingDetails() != null) {
            order.setShippingDetails(null);
        }

        order.setUser(null);
        orderRepository.save(order);

        return orderToDto(order);
    }

    // Relation - ShippingDetails Methods
    public OrderDto assignShippingDetailsToOrder(Long orderNumber, Long shippingDetailsId) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        ShippingDetails shippingDetails = shippingDetailsRepository.findById(shippingDetailsId)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Shipping details",
                        shippingDetailsId)));

        if (shippingDetails.getUser() == null) {
            throw new BadRequestException("Shipping details with id: " + shippingDetailsId
                    + " is not assigned to any user, assign shipping details to user first");
        } else if (shippingDetails.getUser() != order.getUser()) {
            throw new BadRequestException("Assign user: " + shippingDetails.getUser().getUsername() +
                    " to current order first if you want to assign shipping details with id: " + shippingDetailsId
                    + " from " + shippingDetails.getUser().getUsername() + " to current order");
        }

        order.setShippingDetails(shippingDetails);
        orderRepository.save(order);

        return orderToDto(order);
    }

    public OrderDto removeShippingDetailsFromOrder(Long orderNumber, Long shippingDetailsId) {
        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        ShippingDetails shippingDetails = shippingDetailsRepository.findById(shippingDetailsId)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Shipping details",
                        shippingDetailsId)));

        if (order.getShippingDetails() == null) {
            throw new BadRequestException("Order with order-number: " + orderNumber
                    + " does not contain any shipping details");
        } else if (!order.getShippingDetails().equals(shippingDetails)) {
            throw new BadRequestException("Shipping Details with id: " + shippingDetailsId
                    + " is not assigned to order: " + orderNumber);
        }

        order.setShippingDetails(null);
        orderRepository.save(order);

        return orderToDto(order);
    }

    // Relation - Authenticated User Methods
    public List<OrderDto> getAllAuthUserOrders(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<Order> orders = user.getOrders();
        List<OrderDto> orderDtos = new ArrayList<>();

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

    public OrderDto getAuthUserOrderById(String username, Long orderNumber) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Order order = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Order", orderNumber)));

        List<Order> orders = user.getOrders();
        OrderDto orderDto = null;

        for (Order foundOrder : orders) {
            if (foundOrder.equals(order)) {
                orderDto = orderToDto(order);
            }
        }

        if (orderDto != null) {
            return orderDto;
        } else {
            throw new BadRequestException("User: " + username + " does not have order with order-number: "
                    + orderNumber);
        }
    }

    public OrderDto createAuthUserOrder(String username, AuthUserOrderInputDto inputDto) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        ShippingDetails shippingDetails = shippingDetailsRepository.findById(inputDto.getShippingDetailsId()).orElseThrow();

        Order order = new Order();
        order.setOrderDate(createDate());
        order.setStatus("in process");
        order.setIsPaid(false);
        order.setDiscountCode(inputDto.getDiscountCode());
        order.setUser(user);
        order.setShippingDetails(shippingDetails);
        orderRepository.save(order);

        return orderToDto(order);
    }
}