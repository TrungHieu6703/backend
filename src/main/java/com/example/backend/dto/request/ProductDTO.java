package com.example.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
     String name;

     String categoryId;

     String brandId;

     String couponId;

     String price;

     String quantity;

     String description;

     @JsonIgnore
     List<AttributeValueDes> attributeValueDes;
}
