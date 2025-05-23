package com.example.backend.dto.request;


import com.example.backend.enums.StatusEnum;

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
public class OrderDTO {
    String user_id;

    StatusEnum status;

    BigDecimal total;

    String payment_method;

    String shippingInfo;

    List<OrderItemDTO> items;

}
