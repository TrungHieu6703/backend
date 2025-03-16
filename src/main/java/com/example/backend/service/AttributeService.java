package com.example.backend.service;

import com.example.backend.dto.request.AttributeDTO;
import com.example.backend.dto.response.AttributeRes;
import com.example.backend.entity.Attribute;
import com.example.backend.repository.AttributeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeService {
    @Autowired
    private AttributeRepo attributeRepo;

    public AttributeRes createAttribute(AttributeDTO AttributeDTO) {
        Attribute Attribute = new Attribute();
        Attribute.setName(AttributeDTO.getName());

        Attribute result = attributeRepo.save(Attribute);

        return new AttributeRes(
                result.getId(),
                result.getName());
    }

    public AttributeRes updateAttribute(String id ,AttributeDTO AttributeDTO) {
        Attribute Attribute = attributeRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Attribute not found")));

        Attribute.setName(AttributeDTO.getName());

        Attribute updatedAttribute = attributeRepo.save(Attribute);

        return new AttributeRes(
                updatedAttribute.getId(),
                updatedAttribute.getName());
    }

    public void deleteAttribute(String id) {
        Attribute Attribute = attributeRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Attribute not found")));
        attributeRepo.delete(Attribute);
    }

    public AttributeRes getAttributeById(String id) {
        Attribute Attribute = attributeRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Attribute not found")));
        return new AttributeRes(
                Attribute.getId(),
                Attribute.getName()
        );
    }

    public List<AttributeRes> getAllAttributes() {
        List<Attribute> Attributes = attributeRepo.findAll();

        return Attributes.stream()
                .map(Attribute -> new AttributeRes(
                        Attribute.getId(),
                        Attribute.getName()
                )).collect(Collectors.toList());
    }
}
