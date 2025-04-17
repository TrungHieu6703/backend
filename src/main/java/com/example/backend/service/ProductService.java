package com.example.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.backend.dto.request.ProductDTO;
import com.example.backend.dto.response.ProductRes;
import com.example.backend.dto.response.ProductResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductLineRepo productLineRepo;

    @Autowired
    private ProductAttributeValueRepo  productAttributeValueRepo;

    @Autowired
    private AttributeValueRepo attributeValueRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private BrandRepo brandRepo;

    @Autowired
    private CouponRepo couponRepo;

    @Autowired
    private Cloudinary cloudinary;


    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }


    public List<String> uploadMultipleFiles(MultipartFile[] files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = uploadFile(file);
            urls.add(url);
        }
        return urls;
    }

    public ResponseEntity<?> createProduct(ProductDTO productDTO, MultipartFile[] files) throws IOException {
        Category category = categoryRepo.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Brand brand = brandRepo.findById(productDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Coupon coupon = couponRepo.findById(productDTO.getCouponId())
                .orElse(null);


        List<String> imageUrls = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String url = uploadFile(file);
                System.out.println(url);
                imageUrls.add(url);
            }
        }


        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(category);
        product.setBrand(brand);
        product.setCoupon(coupon);
        product.setPrice(new BigDecimal(productDTO.getPrice()));
        product.setQuantity(Integer.parseInt(productDTO.getQuantity()));
        product.setImages(imageUrls);
        product.setDescription(productDTO.getDescription());

        Product newProduct = productRepo.save(product);

        productDTO.getAttributeValueDes().stream().map(
                (attribute -> {
                    AttributeValue attributeValue = attributeValueRepo.findById(attribute.getAttributeValueId()).orElseThrow(
                            ()-> new EntityNotFoundException("attributeValue not found")
                    );
                    ProductAttributeValue productAttributeValue = new ProductAttributeValue();
                    productAttributeValue.setProduct(newProduct);
                    productAttributeValue.setAttributeValue(attributeValue);
                    productAttributeValue.setValue(attribute.getValue());
                    productAttributeValueRepo.save(productAttributeValue);
                    return null;
                })
        ).toList();

        return ResponseEntity.ok("ok");




//        return new ProductRes(savedProduct.getId(), savedProduct.getName(),
//                category.getId(), brand.getId(),
//                coupon != null ? coupon.getId() : null,
//                savedProduct.getPrice(), savedProduct.getQuantity(),
//                savedProduct.getImages(), savedProduct.getDescription());
    }

    @Transactional
    public void deleteProduct(String id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Xóa các thuộc tính liên quan đến sản phẩm
        productAttributeValueRepo.deleteByProductId(id);

        // Xóa sản phẩm
        productRepo.delete(product);
    }
//    public ProductRes updateProduct(String id, ProductDTO productDTO) {
//        Product product = productRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        Category category = categoryRepo.findById(productDTO.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//        Brand brand = brandRepo.findById(productDTO.getBrandId())
//                .orElseThrow(() -> new RuntimeException("Brand not found"));
//        Coupon coupon = couponRepo.findById(productDTO.getCouponId())
//                .orElse(null);
//        product.setName(productDTO.getName());
//        product.setCategory(category);
//        product.setBrand(brand);
//        product.setCoupon(coupon);
//        product.setPrice(productDTO.getPrice());
//        product.setQuantity(productDTO.getQuantity());
//        product.setImage(productDTO.getImage());
//        product.setDescription(productDTO.getDescription());
//
//        Product updatedProduct = productRepo.save(product);
//
//        return new ProductRes(updatedProduct.getId(), updatedProduct.getName(),
//                category.getId(), brand.getId(),
//                coupon != null ? coupon.getId() : null,
//                updatedProduct.getPrice(), updatedProduct.getQuantity(),
//                updatedProduct.getImage(), updatedProduct.getDescription());
//    }
//
//    public void deleteProduct(String id) {
//        Product product = productRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        productRepo.delete(product);
//    }
//
    public ProductRes getProductById(String id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductRes(product.getId(), product.getName(),
                product.getCategory().getId(),
                product.getBrand().getId(),
                product.getCoupon() != null ? product.getCoupon().getId() : null,
                product.getPrice(), product.getQuantity(),
                product.getImages(), product.getDescription());
    }
//
//    public List<ProductRes> getAllProducts() {
//        List<Product> products = productRepo.findAll();
//        return products.stream()
//                .map(product -> new ProductRes(product.getId(), product.getName(),
//                        product.getCategory().getId(),
//                        product.getBrand().getId(),
//                        product.getCoupon() != null ? product.getCoupon().getId() : null,
//                        product.getPrice(), product.getQuantity(),
//                        product.getImage(), product.getDescription()))
//                .collect(Collectors.toList());
//    }
public List<ProductResponseDTO> getAllProducts() {
    return productRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
}

    private ProductResponseDTO convertToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory() != null ? product.getCategory().getName() : null);
        dto.setBrand(product.getBrand() != null ? product.getBrand().getName() : null);
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setAvatar(product.getImages() != null && !product.getImages().isEmpty() ? product.getImages().get(0) : null);
        return dto;
    }
}
