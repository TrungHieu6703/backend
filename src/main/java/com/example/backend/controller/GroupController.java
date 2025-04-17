package com.example.backend.controller;

import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.AttributeRes;
import com.example.backend.entity.Attribute;
import com.example.backend.entity.AttributeValue;
import com.example.backend.repository.AttributeValueRepo;
import com.example.backend.repository.CategoryAttributeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public class GroupController {
    @Autowired
    private CategoryAttributeRepo categoryAttributeRepo;

    @Autowired
    private AttributeValueRepo attributeValueRepo;

    @GetMapping("categories/{id}/attribute")
    public List<Attribute> getAttributeById(@PathVariable String id) {
        List<Attribute> Attributes = categoryAttributeRepo.findAttributesByCategoryId(id);
        return Attributes;
    }

    @GetMapping("attrbutes/{id}/value")
    public ResponseEntity<?> getAttributeValueByAttributeId(@PathVariable String id) {
        try {
            List<Map<String, Object>> result = attributeValueRepo.findByAttribute_Id(id)
                    .stream()
                    .map(av -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", av.getId());
                        map.put("value", av.getValue());
                        return map;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

//    @GetMapping("categories/{id}/attributes")
//    public List<Attribute> getAttributeById(@PathVariable String id) {
//        List<Attribute> Attributes = categoryAttributeRepo.findAttributesByCategoryId(id);
//        return Attributes;
//    }
//
//    @GetMapping("attributes/{id}/values")
//    public List<String> getAttributeValuesByAttributeId(@PathVariable String id) {
//        return attributeValueRepo.findByAttribute_Id(id)
//                .stream()
//                .map(AttributeValue::getValue) // Chỉ lấy giá trị value
//                .collect(Collectors.toList());
//    }
}
