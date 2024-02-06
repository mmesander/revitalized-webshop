package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.ProductDto;
import nu.revitalized.revitalizedwebshop.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    // CRUD Requests -- GET Requests
    @GetMapping("/producten")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> dtos = productService.getAllProducts();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/producten/{id}")
    public ResponseEntity<Object> getProductById(
            @PathVariable("id") Long id
    ) {
        Object dto = productService.getProductById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/producten/niet-op-voorraad")
    public ResponseEntity<List<ProductDto>> getOutOfStockProducts() {
        List<ProductDto> dtos = productService.getOutOfStockProducts();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/producten/op-voorraad")
    public ResponseEntity<List<ProductDto>> getInStockProducts() {
        List<ProductDto> dtos = productService.getInStockProducts();

        return ResponseEntity.ok().body(dtos);
    }

    // CRUD Requests -- DELETE Requests
    @DeleteMapping("/producten/{id}")
    public ResponseEntity<Object> deleteProduct(
            @PathVariable("id") Long id
    ) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
