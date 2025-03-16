package com.example.backend.service;

import com.example.backend.dto.request.OrderDetailDTO;
import com.example.backend.dto.response.OrderDetailRes;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderDetail;
import com.example.backend.entity.Product;
import com.example.backend.repository.OrderDetailRepo;
import com.example.backend.repository.OrderRepo;
import com.example.backend.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepo orderDetailRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    public OrderDetailRes createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepo.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productRepo.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(orderDetailDTO.getQuantity());
        orderDetail.setPrice(orderDetailDTO.getPrice());

        OrderDetail savedOrderDetail = orderDetailRepo.save(orderDetail);

        return new OrderDetailRes(savedOrderDetail.getId(),
                order.getId(), product.getId(), savedOrderDetail.getQuantity(), savedOrderDetail.getPrice());
    }

    public OrderDetailRes updateOrderDetail(String id, OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));

        Order order = orderRepo.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productRepo.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(orderDetailDTO.getQuantity());
        orderDetail.setPrice(orderDetailDTO.getPrice());

        OrderDetail updatedOrderDetail = orderDetailRepo.save(orderDetail);

        return new OrderDetailRes(updatedOrderDetail.getId(),
                order.getId(), product.getId(), updatedOrderDetail.getQuantity(), updatedOrderDetail.getPrice());
    }

    public void deleteOrderDetail(String id) {
        OrderDetail orderDetail = orderDetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));
        orderDetailRepo.delete(orderDetail);
    }

    public OrderDetailRes getOrderDetailById(String id) {
        OrderDetail orderDetail = orderDetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));
        return new OrderDetailRes(orderDetail.getId(),
                orderDetail.getOrder().getId(),
                orderDetail.getProduct().getId(),
                orderDetail.getQuantity(),
                orderDetail.getPrice());
    }

    public List<OrderDetailRes> getAllOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailRepo.findAll();
        return orderDetails.stream()
                .map(orderDetail -> new OrderDetailRes(orderDetail.getId(),
                        orderDetail.getOrder().getId(),
                        orderDetail.getProduct().getId(),
                        orderDetail.getQuantity(),
                        orderDetail.getPrice()))
                .collect(Collectors.toList());
    }
}
