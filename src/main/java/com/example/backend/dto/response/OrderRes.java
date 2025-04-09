package com.example.backend.dto.response;

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
public class OrderRes {
    private String id;
    private String userId;
    private StatusEnum status;
    private BigDecimal total;
    private List<OrderDetailRes> orderDetails;

    // Constructor without orderDetails for backward compatibility
    public OrderRes(String id, String userId, StatusEnum status, BigDecimal total) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.total = total;
    }
}
