package com.example.backend.service;

import com.example.backend.dto.request.ProductLineDTO;
import com.example.backend.dto.response.ProductLineRes;
import com.example.backend.entity.Brand;
import com.example.backend.entity.Product_line;
import com.example.backend.repository.BrandRepo;
import com.example.backend.repository.ProductLineRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductLineService {
    @Autowired
    private ProductLineRepo productLineRepo;

    @Autowired
    private BrandRepo brandRepo;

    public ProductLineRes createProductLine(ProductLineDTO productLineDTO) {
        Brand brand = brandRepo.findById(productLineDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Product_line productLine = new Product_line();
        productLine.setBrand(brand);
        productLine.setLine_name(productLineDTO.getLine_name());

        Product_line savedProductLine = productLineRepo.save(productLine);

        return new ProductLineRes(savedProductLine.getId(),
                brand.getId(), savedProductLine.getLine_name());
    }

    public ProductLineRes updateProductLine(String id, ProductLineDTO productLineDTO) {
        Product_line productLine = productLineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductLine not found"));

        Brand brand = brandRepo.findById(productLineDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        productLine.setBrand(brand);
        productLine.setLine_name(productLineDTO.getLine_name());

        Product_line updatedProductLine = productLineRepo.save(productLine);

        return new ProductLineRes(updatedProductLine.getId(),
                brand.getId(), updatedProductLine.getLine_name());
    }

    public void deleteProductLine(String product_lineId) {
        Product_line productLine = productLineRepo.findById(product_lineId)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + product_lineId));

        // Kiểm tra xem brand có liên kết với sản phẩm nào không
        boolean hasProductLines = productLineRepo.existsProductsByProductLineId(product_lineId);

        if (hasProductLines) {
            // Cập nhật trường is_deleted nếu có liên kết
            productLine.set_deleted(true);
            productLineRepo.save(productLine);
        } else {
            // Xóa hoàn toàn nếu không có liên kết
            productLineRepo.delete(productLine);
        }
    }

    public ProductLineRes getProductLineById(String id) {
        Product_line productLine = productLineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductLine not found"));
        return new ProductLineRes(productLine.getId(),
                productLine.getBrand().getId(), productLine.getLine_name());
    }

    public List<ProductLineRes> getAllProductLines() {
        List<Product_line> productLines = productLineRepo.findAllActiveProductLines();
        return productLines.stream()
                .map(productLine -> new ProductLineRes(productLine.getId(),
                        productLine.getBrand().getId(), productLine.getLine_name()))
                .collect(Collectors.toList());
    }
}
