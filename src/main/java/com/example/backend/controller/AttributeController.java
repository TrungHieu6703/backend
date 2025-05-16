package com.example.backend.controller;

import com.example.backend.dto.request.AttributeDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.AttributeRes;
import com.example.backend.entity.Attribute;
import com.example.backend.repository.AttributeRepo;
import com.example.backend.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/attributes")
public class AttributeController {

    @Autowired
    private AttributeRepo attributeRepo;

    @Autowired
    private AttributeService attributeService;

    // Tạo mới Attribute
    @PostMapping
    public ResponseEntity<ApiResponse<AttributeRes>> createAttribute(@RequestBody AttributeDTO attributeDTO) {
        AttributeRes attributeRes = attributeService.createAttribute(attributeDTO);
        ApiResponse<AttributeRes> response = new ApiResponse<>("Attribute created successfully", HttpStatus.CREATED.value(), attributeRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật Attribute theo ID
    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<ApiResponse<AttributeRes>> updateAttribute(@PathVariable String id, @RequestBody AttributeDTO attributeDTO) {
        AttributeRes updatedAttribute = attributeService.updateAttribute(id, attributeDTO);
        ApiResponse<AttributeRes> response = new ApiResponse<>("Attribute updated successfully", HttpStatus.OK.value(), updatedAttribute);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    // Xóa Attribute theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttribute(@PathVariable String id) {
        attributeService.deleteAttribute(id);
        ApiResponse<Void> response = new ApiResponse<>("Attribute deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy Attribute theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttributeRes>> getAttributeById(@PathVariable String id) {
        AttributeRes attributeRes = attributeService.getAttributeById(id);
        ApiResponse<AttributeRes> response = new ApiResponse<>("Attribute retrieved successfully", HttpStatus.OK.value(), attributeRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả Attributes
    @GetMapping
    public ResponseEntity<ApiResponse<List<AttributeRes>>> getAllAttributes() {
        List<AttributeRes> attributes = attributeService.getAllAttributes();
        ApiResponse<List<AttributeRes>> response = new ApiResponse<>("All attributes retrieved successfully", HttpStatus.OK.value(), attributes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getListAttributeById")
    public ResponseEntity<?> getListAttributeById(){
        List<String> ids = Arrays.asList("dfb3f8e9-f4dc-11ef-8faf-0242ac110002",
                "dfb414ad-f4dc-11ef-8faf-0242ac110002",
                "dfb417fa-f4dc-11ef-8faf-0242ac110002",
                "dfb41a45-f4dc-11ef-8faf-0242ac110002"
        );
        List<Attribute> attributes = attributeRepo.findAllById(ids);
        attributes.stream().map(
                (e)->e.getName()
        ).toList();
        return ResponseEntity.ok(attributes);
    }
}
