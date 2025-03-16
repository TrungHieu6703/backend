package com.example.backend.service;

import com.example.backend.dto.request.OrderDTO;
import com.example.backend.dto.response.OrderRes;
import com.example.backend.entity.Order;
import com.example.backend.entity.User;
import com.example.backend.enums.StatusEnum;
import com.example.backend.repository.OrderRepo;
import com.example.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    public OrderRes createOrder(OrderDTO orderDTO) {
        User user = userRepo.findById(orderDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(StatusEnum.PENDING);
        order.setTotal(orderDTO.getTotal());

        Order savedOrder = orderRepo.save(order);

        return new OrderRes(savedOrder.getId(), savedOrder.getUser().getId(), savedOrder.getStatus(), savedOrder.getTotal());
    }

    public OrderRes updateOrder(String id, OrderDTO orderDTO) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = userRepo.findById(orderDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        order.setUser(user);
        order.setStatus(orderDTO.getStatus());
        order.setTotal(orderDTO.getTotal());

        Order updatedOrder = orderRepo.save(order);

        return new OrderRes(updatedOrder.getId(), updatedOrder.getUser().getId(), updatedOrder.getStatus(), updatedOrder.getTotal());
    }

    public void deleteOrder(String id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepo.delete(order);
    }

    public OrderRes getOrderById(String id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return new OrderRes(order.getId(), order.getUser().getId(), order.getStatus(), order.getTotal());
    }

    public List<OrderRes> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return orders.stream()
                .map(order -> new OrderRes(order.getId(), order.getUser().getId(), order.getStatus(), order.getTotal()))
                .collect(Collectors.toList());
    }
}
