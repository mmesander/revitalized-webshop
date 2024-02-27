package nu.revitalized.revitalizedwebshop.controllers;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriOrderNumber;

import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.*;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ADMIN - CRUD Requests
    @GetMapping("/users/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> dtos = orderService.getAllOrders();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/users/orders/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByOrderNumber(
            @PathVariable("orderNumber") Long orderNumber) {
        OrderDto dto = orderService.getOrderByOrderNumber(orderNumber);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/users/orders/search")
    public ResponseEntity<List<OrderDto>> getOrdersByParam(
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        List<OrderDto> dtos = orderService.getALlOrdersByParam(
                price, minPrice, maxPrice
        );

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/users/orders-payed")
    public ResponseEntity<List<OrderDto>> getAllPayedOrders() {
        List<OrderDto> dtos = orderService.getAllPayedOrders();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/users/orders-unpayed")
    public ResponseEntity<List<OrderDto>> getAllUnpayedOrders() {
        List<OrderDto> dtos = orderService.getAllUnpayedOrders();

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/users/orders")
    public ResponseEntity<OrderDto> createOrder(
            @Valid
            @RequestBody OrderInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.createOrder(inputDto);

            URI uri = buildUriOrderNumber(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

    @PutMapping("/users/orders/{orderNumber}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody OrderInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.updateOrder(inputDto, orderNumber);

            return ResponseEntity.ok().body(dto);
        }
    }

    @PutMapping("/users/orders/{orderNumber}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody OrderStatusInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.updateOrderStatus(inputDto, orderNumber);

            return ResponseEntity.ok().body(dto);
        }
    }

    @PutMapping("/users/orders/{orderNumber}/payment")
    public ResponseEntity<OrderDto> updateOrderPayment(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody OrderIsPayedInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.updateOrderPayment(inputDto, orderNumber);

            return ResponseEntity.ok().body(dto);
        }
    }

    @PutMapping("/users/orders/{orderNumber}/discount")
    public ResponseEntity<OrderDto> updateOrderDiscount(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody OrderDiscountInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.updateOrderDiscount(inputDto, orderNumber);

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping("/users/orders/{orderNumber}")
    public ResponseEntity<Object> deleteOrder(
            @PathVariable("orderNumber") Long orderNumber
    ) {
        String confirmation = orderService.deleteOrder(orderNumber);

        return ResponseEntity.ok().body(confirmation);
    }
}
