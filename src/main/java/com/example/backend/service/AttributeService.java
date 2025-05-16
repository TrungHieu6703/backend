package com.example.backend.service;

import com.example.backend.dto.request.AttributeDTO;
import com.example.backend.dto.response.AttributeRes;
import com.example.backend.entity.Attribute;
import com.example.backend.entity.Brand;
import com.example.backend.repository.AttributeRepo;
import jakarta.persistence.EntityNotFoundException;
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

    public void deleteAttribute(String attributeId) {
        Attribute attribute = attributeRepo.findById(attributeId)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + attributeId));

        // Kiểm tra xem brand có liên kết với sản phẩm nào không
        boolean hasAttributes = attributeRepo.existsProductsByAttributeId(attributeId);

        if (hasAttributes) {
            // Cập nhật trường is_deleted nếu có liên kết
            attribute.set_deleted(true);
            attributeRepo.save(attribute);
        } else {
            // Xóa hoàn toàn nếu không có liên kết
            attributeRepo.delete(attribute);
        }
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
        List<Attribute> Attributes = attributeRepo.findAllActiveAttributes();

        return Attributes.stream()
                .map(Attribute -> new AttributeRes(
                        Attribute.getId(),
                        Attribute.getName()
                )).collect(Collectors.toList());
    }
}
