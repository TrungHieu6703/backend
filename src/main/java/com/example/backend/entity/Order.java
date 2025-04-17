package com.example.backend.entity;

import com.example.backend.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    StatusEnum status;

    BigDecimal total;

    @Column(columnDefinition = "TEXT")
    String shippingInfo;

    @Column(name = "created_date")
    LocalDateTime createdDate;

    @OneToMany(mappedBy = "order")
    Set<OrderDetail> orderDetails = new HashSet<>();

    @PrePersist
    public void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
