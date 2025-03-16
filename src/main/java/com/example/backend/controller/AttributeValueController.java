package com.example.backend.controller;

import com.example.backend.dto.request.AttributeValueDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.AttributeValueRes;
import com.example.backend.service.AttributeValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attribute-values")
public class AttributeValueController {

    @Autowired
    private AttributeValueService attributeValueService;

    // Tạo mới AttributeValue
    @PostMapping
    public ResponseEntity<ApiResponse<AttributeValueRes>> createAttributeValue(@RequestBody AttributeValueDTO attributeValueDTO) {
        AttributeValueRes attributeValueRes = attributeValueService.createAttributeValue(attributeValueDTO);
        ApiResponse<AttributeValueRes> response = new ApiResponse<>("Attribute value created successfully", HttpStatus.CREATED.value(), attributeValueRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật AttributeValue theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttributeValueRes>> updateAttributeValue(@PathVariable String id, @RequestBody AttributeValueDTO attributeValueDTO) {
        AttributeValueRes updatedAttributeValue = attributeValueService.updateAttributeValue(id, attributeValueDTO);
        ApiResponse<AttributeValueRes> response = new ApiResponse<>("Attribute value updated successfully", HttpStatus.OK.value(), updatedAttributeValue);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa AttributeValue theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttributeValue(@PathVariable String id) {
        attributeValueService.deleteAttributeValue(id);
        ApiResponse<Void> response = new ApiResponse<>("Attribute value deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy AttributeValue theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttributeValueRes>> getAttributeValueById(@PathVariable String id) {
        AttributeValueRes attributeValueRes = attributeValueService.getAttributeValueById(id);
        ApiResponse<AttributeValueRes> response = new ApiResponse<>("Attribute value retrieved successfully", HttpStatus.OK.value(), attributeValueRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả AttributeValues
    @GetMapping
    public ResponseEntity<ApiResponse<List<AttributeValueRes>>> getAllAttributeValues() {
        List<AttributeValueRes> attributeValues = attributeValueService.getAllAttributeValues();
        ApiResponse<List<AttributeValueRes>> response = new ApiResponse<>("All attribute values retrieved successfully", HttpStatus.OK.value(), attributeValues);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
