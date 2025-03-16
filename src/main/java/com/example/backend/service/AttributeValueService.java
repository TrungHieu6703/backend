package com.example.backend.service;

import com.example.backend.dto.request.AttributeValueDTO;
import com.example.backend.dto.response.AttributeValueRes;
import com.example.backend.entity.Attribute;
import com.example.backend.entity.AttributeValue;
import com.example.backend.repository.AttributeRepo;
import com.example.backend.repository.AttributeValueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeValueService {
    @Autowired
    private AttributeValueRepo attributeValueRepo;

    @Autowired
    private AttributeRepo attributeRepo;

    public AttributeValueRes createAttributeValue(AttributeValueDTO attributeValueDTO) {
        Attribute attribute = attributeRepo.findById(attributeValueDTO.getAttributeId())
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setAttribute(attribute);
        attributeValue.setValue(attributeValueDTO.getValue());

        AttributeValue savedAttributeValue = attributeValueRepo.save(attributeValue);

        return new AttributeValueRes(savedAttributeValue.getId(),
                attribute.getId(), savedAttributeValue.getValue());
    }

    public AttributeValueRes updateAttributeValue(String id, AttributeValueDTO attributeValueDTO) {
        AttributeValue attributeValue = attributeValueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("AttributeValue not found"));

        Attribute attribute = attributeRepo.findById(attributeValueDTO.getAttributeId())
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        attributeValue.setAttribute(attribute);
        attributeValue.setValue(attributeValueDTO.getValue());

        AttributeValue updatedAttributeValue = attributeValueRepo.save(attributeValue);

        return new AttributeValueRes(updatedAttributeValue.getId(),
                attribute.getId(), updatedAttributeValue.getValue());
    }

    public void deleteAttributeValue(String id) {
        AttributeValue attributeValue = attributeValueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("AttributeValue not found"));
        attributeValueRepo.delete(attributeValue);
    }

    public AttributeValueRes getAttributeValueById(String id) {
        AttributeValue attributeValue = attributeValueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("AttributeValue not found"));
        return new AttributeValueRes(attributeValue.getId(),
                attributeValue.getAttribute().getId(), attributeValue.getValue());
    }

    public List<AttributeValueRes> getAllAttributeValues() {
        List<AttributeValue> attributeValues = attributeValueRepo.findAll();
        return attributeValues.stream()
                .map(attributeValue -> new AttributeValueRes(attributeValue.getId(),
                        attributeValue.getAttribute().getId(), attributeValue.getValue()))
                .collect(Collectors.toList());
    }
}
