package com.example.backend.controller;

import com.example.backend.dto.request.BrandDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.BrandRes;
import com.example.backend.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    // Tạo mới Brand
    @PostMapping
    public ResponseEntity<ApiResponse<BrandRes>> createBrand(@RequestBody BrandDTO brandDTO) {
        BrandRes brandRes = brandService.createBrand(brandDTO);
        ApiResponse<BrandRes> response = new ApiResponse<>("Brand created successfully", HttpStatus.CREATED.value(), brandRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật Brand theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandRes>> updateBrand(@PathVariable String id, @RequestBody BrandDTO brandDTO) {
        BrandRes updatedBrand = brandService.updateBrand(id, brandDTO);
        ApiResponse<BrandRes> response = new ApiResponse<>("Brand updated successfully", HttpStatus.OK.value(), updatedBrand);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa Brand theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable String id) {
        brandService.deleteBrand(id);
        ApiResponse<Void> response = new ApiResponse<>("Brand deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy Brand theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandRes>> getBrandById(@PathVariable String id) {
        BrandRes brandRes = brandService.getBrandById(id);
        ApiResponse<BrandRes> response = new ApiResponse<>("Brand retrieved successfully", HttpStatus.OK.value(), brandRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả Brand
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandRes>>> getAllBrands() {
        List<BrandRes> brands = brandService.getAllBrands();
        ApiResponse<List<BrandRes>> response = new ApiResponse<>("All brands retrieved successfully", HttpStatus.OK.value(), brands);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllBrandsWithProductLines")
    public ResponseEntity<List<com.example.backend.dto.response.BrandDTO>> getAllBrandsWithProductLines() {
        return ResponseEntity.ok(brandService.getAllBrandsWithProductLines());
    }
}
