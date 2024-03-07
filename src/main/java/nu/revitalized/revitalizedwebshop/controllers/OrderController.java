package nu.revitalized.revitalizedwebshop.controllers;

// Imports
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
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriOrderNumber;

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

    @GetMapping("/users/orders-paid")
    public ResponseEntity<List<OrderDto>> getAllPaidOrders() {
        List<OrderDto> dtos = orderService.getAllPaidOrders();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/users/orders-unpaid")
    public ResponseEntity<List<OrderDto>> getAllUnpaidOrders() {
        List<OrderDto> dtos = orderService.getAllUnpaidOrders();

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
            @RequestBody OrderIsPaidInputDto inputDto,
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

    // Relation - Product Requests
    @PutMapping(value = "/users/orders/{orderNumber}/shoppinglist")
    public ResponseEntity<Object> assignMultipleProductsToOrder(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody MultipleIdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.assignMultipleProductsToOrder(orderNumber, inputDto.getProductIds());

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping(value = "/users/orders/{orderNumber}/shoppinglist")
    public ResponseEntity<Object> removeMultipleProductsFromOrder(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody MultipleIdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.removeMultipleProductsFromOrder(orderNumber, inputDto.getProductIds());

            return ResponseEntity.ok().body(dto);
        }
    }

    @PutMapping(value = "/users/orders/{orderNumber}/product")
    public ResponseEntity<Object> assignProductToOrder(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.assignProductToOrder(orderNumber, inputDto.getId());

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping(value = "/users/orders/{orderNumber}/product")
    public ResponseEntity<Object> removeProductFromOrder(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.removeProductFromOrder(orderNumber, inputDto.getId());

            return ResponseEntity.ok().body(dto);
        }
    }

    // Relation - User Requests
    @PutMapping(value = "/users/orders/{orderNumber}/user")
    public ResponseEntity<OrderDto> assignUserToOrder(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody UsernameInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.assignUserToOrder(orderNumber, inputDto.getUsername());

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping(value = "/users/orders/{orderNumber}/user")
    public ResponseEntity<OrderDto> removeUserFromOrder(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody UsernameInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.removeUserFromOrder(orderNumber, inputDto.getUsername());

            return ResponseEntity.ok().body(dto);
        }
    }

    // Relation - ShippingDetails Requests
    @PutMapping(value = "/users/orders/{orderNumber}/shipping-details")
    public ResponseEntity<Object> assignShippingDetailsToOrder(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.assignShippingDetailsToOrder(orderNumber, inputDto.getId());

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping(value = "/users/orders/{orderNumber}/shipping-details")
    public ResponseEntity<Object> removeShippingDetailsFromOrder(
            @PathVariable("orderNumber") Long orderNumber,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            OrderDto dto = orderService.removeShippingDetailsFromOrder(orderNumber, inputDto.getId());

            return ResponseEntity.ok().body(dto);
        }
    }
}