package com.example.backend.controller;

import com.example.backend.dto.request.AttributeValueDes;
import com.example.backend.dto.request.ProductDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.CategoryRes;
import com.example.backend.dto.response.ProductRes;
import com.example.backend.dto.response.ProductResponseDTO;
import com.example.backend.entity.Category;
import com.example.backend.entity.Product;
import com.example.backend.repository.AttributeValueRepo;
import com.example.backend.repository.ProductAttributeValueRepo;
import com.example.backend.repository.ProductRepo;
import com.example.backend.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping(value = "/products")
public class ProductController {
    @Autowired
    private ProductAttributeValueRepo productAttributeValueRepo;

    @Autowired
    private AttributeValueRepo attributeValueRepo;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepo productRepo;

    // Tạo mới Product
    @PostMapping(value = "/create",
            consumes = {"multipart/form-data"},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> createProduct(
            @RequestPart("files") MultipartFile[] files,
            @RequestPart("name") String name,
            @RequestPart("categoryId") String categoryId,
            @RequestPart("brandId") String brandId,
            @RequestPart("productLineId") String couponId,
            @RequestPart("price") String price,
            @RequestPart("quantity") String quantity,
            @RequestPart("description") String description,
            @RequestPart("is_hot") String is_hot,
            @RequestPart("specs_summary") String specs_summary,
            @RequestPart("attributes") List<AttributeValueDes> attributeValueDes

    ) {
        try {
            boolean isHotBoolean = Boolean.parseBoolean(is_hot);

            ProductDTO data = new ProductDTO(name, categoryId, brandId, couponId, price, quantity, description, isHotBoolean,specs_summary, attributeValueDes );
            ResponseEntity<?> productRes = productService.createProduct(data, files);

//            ApiResponse<Product> response = new ApiResponse<>("Product created successfully", HttpStatus.CREATED.value(), productRes);
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return null;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping(value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") String productId,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @RequestPart("name") String name,
            @RequestPart("categoryId") String categoryId,
            @RequestPart("brandId") String brandId,
            @RequestPart("productLineId") String productLineId,
            @RequestPart("price") String price,
            @RequestPart("quantity") String quantity,
            @RequestPart("description") String description,
            @RequestPart("is_hot") String is_hot, // Nhận là String thay vì boolean
            @RequestPart("specs_summary") String specs_summary,
            @RequestPart("existingImages") String existingImagesJson,
            @RequestPart("attributes") List<AttributeValueDes> attributeValueDes
    ) {
        try {
            // Chuyển đổi String "true"/"false" thành boolean
            boolean isHotBoolean = Boolean.parseBoolean(is_hot);

            ProductDTO data = new ProductDTO(name, categoryId, brandId, productLineId, price, quantity, description,
                    isHotBoolean, specs_summary, attributeValueDes);
            ResponseEntity<?> productRes = productService.updateProduct(productId, data, files, existingImagesJson);

            ApiResponse<?> response = new ApiResponse<>("Product updated successfully", HttpStatus.OK.value(), productRes.getBody());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/upload-multiple")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files1") MultipartFile[] files) {
        try {
            List<String> urls = productService.uploadMultipleFiles(files);
            return ResponseEntity.ok(urls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/check-quantity")
    public ResponseEntity<Boolean> checkQuantity(@RequestParam String productId,
                                                @RequestParam int quantity) {
        boolean available = productService.checkQuantityAvailable(productId, quantity);
        return ResponseEntity.ok(available);
    }
//    // Cập nhật Product theo ID
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<ProductRes>> updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO) {
//        ProductRes updatedProduct = productService.updateProduct(id, productDTO);
//        ApiResponse<ProductRes> response = new ApiResponse<>("Product updated successfully", HttpStatus.OK.value(), updatedProduct);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    // Xóa Product theo ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
//        productService.deleteProduct(id);
//        ApiResponse<Void> response = new ApiResponse<>("Product deleted successfully", HttpStatus.OK.value(), null);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    // Lấy Product theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductRes>> getProductById(@PathVariable String id) {
        ProductRes productRes = productService.getProductById(id);
        ApiResponse<ProductRes> response = new ApiResponse<>("Product retrieved successfully", HttpStatus.OK.value(), productRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
//
//    // Lấy tất cả Product
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<ProductRes>>> getAllProducts() {
//        List<ProductRes> products = productService.getAllProducts();
//        ApiResponse<List<ProductRes>> response = new ApiResponse<>("All products retrieved successfully", HttpStatus.OK.value(), products);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
@GetMapping
public List<ProductResponseDTO> getAllProducts() {
    return productService.getAllProducts();
}
}