package com.example.backend.service;

import com.example.backend.dto.request.OrderDTO;
import com.example.backend.dto.request.OrderItemDTO;
import com.example.backend.dto.response.OrderDetailRes;
import com.example.backend.dto.response.OrderRes;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderDetail;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.enums.StatusEnum;
import com.example.backend.repository.OrderDetailRepo;
import com.example.backend.repository.OrderRepo;
import com.example.backend.repository.ProductRepo;
import com.example.backend.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailRepo orderDetailRepo;

    @Autowired
    private ProductRepo productRepo;

    @Transactional
    public OrderRes createOrder(OrderDTO orderDTO) {
        System.out.println(orderDTO.getShippingInfo());
        User user = userRepo.findById(orderDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo order mới
        Order order = new Order();
        order.setUser(user);
        order.setStatus(orderDTO.getStatus() != null ? orderDTO.getStatus() : StatusEnum.PENDING);

        // Tính tổng tiền từ các items nếu không được cung cấp
        if (orderDTO.getTotal() == null) {
            BigDecimal total = BigDecimal.ZERO;
            for (OrderItemDTO item : orderDTO.getItems()) {
                total = total.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            }
            order.setTotal(total);
        } else {
            order.setTotal(orderDTO.getTotal());
        }

        // Lưu order
        Order savedOrder = orderRepo.save(order);

        // Tạo các order details
        List<OrderDetailRes> orderDetails = new ArrayList<>();
        if (orderDTO.getItems() != null && !orderDTO.getItems().isEmpty()) {
            for (OrderItemDTO item : orderDTO.getItems()) {
                Product product = productRepo.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with ID: " + item.getProductId()));

                // Kiểm tra số lượng hàng trong kho
                if (product.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Insufficient product quantity for product: " + product.getName());
                }

                // Giảm số lượng sản phẩm trong kho
                product.setQuantity(product.getQuantity() - item.getQuantity());
                productRepo.save(product);

                // Tạo chi tiết đơn hàng
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(savedOrder);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(item.getQuantity());
                orderDetail.setPrice(item.getPrice());

                OrderDetail savedDetail = orderDetailRepo.save(orderDetail);

                orderDetails.add(new OrderDetailRes(
                        savedDetail.getId(),
                        savedOrder.getId(),
                        product.getId(),
                        savedDetail.getQuantity(),
                        savedDetail.getPrice()
                ));
            }
        }

        return new OrderRes(
                savedOrder.getId(),
                savedOrder.getUser().getId(),
                savedOrder.getStatus(),
                savedOrder.getTotal(),
                savedOrder.getCreatedDate(),
                orderDetails
        );
    }

    @Transactional
    public OrderRes updateOrder(String id, OrderDTO orderDTO) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = userRepo.findById(orderDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        order.setUser(user);

        // Cập nhật trạng thái nếu được cung cấp
        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());
        }

        // Cập nhật tổng tiền nếu được cung cấp
        if (orderDTO.getTotal() != null) {
            order.setTotal(orderDTO.getTotal());
        }

        Order updatedOrder = orderRepo.save(order);

        // Lấy danh sách order details hiện tại
        List<OrderDetail> currentDetails = orderDetailRepo.findByOrderId(id);
        List<OrderDetailRes> orderDetailResList = currentDetails.stream()
                .map(detail -> new OrderDetailRes(
                        detail.getId(),
                        detail.getOrder().getId(),
                        detail.getProduct().getId(),
                        detail.getQuantity(),
                        detail.getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderRes(
                updatedOrder.getId(),
                updatedOrder.getUser().getId(),
                updatedOrder.getStatus(),
                updatedOrder.getTotal(),
                updatedOrder.getCreatedDate(),
                orderDetailResList
        );
    }

    @Transactional
    public OrderRes updateOrderStatus(String id, StatusEnum status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order updatedOrder = orderRepo.save(order);

        // Lấy danh sách order details
        List<OrderDetail> details = orderDetailRepo.findByOrderId(id);
        List<OrderDetailRes> orderDetailResList = details.stream()
                .map(detail -> new OrderDetailRes(
                        detail.getId(),
                        detail.getOrder().getId(),
                        detail.getProduct().getId(),
                        detail.getQuantity(),
                        detail.getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderRes(
                updatedOrder.getId(),
                updatedOrder.getUser().getId(),
                updatedOrder.getStatus(),
                updatedOrder.getTotal(),
                updatedOrder.getCreatedDate(),
                orderDetailResList
        );
    }

    @Transactional
    public void deleteOrder(String id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Xóa các order details trước
        List<OrderDetail> details = orderDetailRepo.findByOrderId(id);
        for (OrderDetail detail : details) {
            // Hoàn lại số lượng sản phẩm trong kho
            Product product = detail.getProduct();
            product.setQuantity(product.getQuantity() + detail.getQuantity());
            productRepo.save(product);

            orderDetailRepo.delete(detail);
        }

        // Xóa order
        orderRepo.delete(order);
    }

    public OrderRes getOrderById(String id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Lấy danh sách order details
        List<OrderDetail> details = orderDetailRepo.findByOrderId(id);
        List<OrderDetailRes> orderDetailResList = details.stream()
                .map(detail -> new OrderDetailRes(
                        detail.getId(),
                        detail.getOrder().getId(),
                        detail.getProduct().getId(),
                        detail.getQuantity(),
                        detail.getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderRes(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedDate(),
                orderDetailResList
        );
    }

    public List<OrderRes> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return orders.stream()
                .map(order -> {
                    // Lấy danh sách order details cho mỗi order
                    List<OrderDetail> details = orderDetailRepo.findByOrderId(order.getId());
                    List<OrderDetailRes> orderDetailResList = details.stream()
                            .map(detail -> new OrderDetailRes(
                                    detail.getId(),
                                    detail.getOrder().getId(),
                                    detail.getProduct().getId(),
                                    detail.getQuantity(),
                                    detail.getPrice()
                            ))
                            .collect(Collectors.toList());

                    return new OrderRes(
                            order.getId(),
                            order.getUser().getId(),
                            order.getStatus(),
                            order.getTotal(),
                            order.getCreatedDate(),
                            orderDetailResList
                    );
                })
                .collect(Collectors.toList());
    }

    public List<OrderRes> getOrdersByUserId(String userId) {
        List<Order> orders = orderRepo.findByUserId(userId);
        return orders.stream()
                .map(order -> {
                    // Lấy danh sách order details cho mỗi order
                    List<OrderDetail> details = orderDetailRepo.findByOrderId(order.getId());
                    List<OrderDetailRes> orderDetailResList = details.stream()
                            .map(detail -> new OrderDetailRes(
                                    detail.getId(),
                                    detail.getOrder().getId(),
                                    detail.getProduct().getId(),
                                    detail.getQuantity(),
                                    detail.getPrice()
                            ))
                            .collect(Collectors.toList());

                    return new OrderRes(
                            order.getId(),
                            order.getUser().getId(),
                            order.getStatus(),
                            order.getTotal(),
                            order.getCreatedDate(),
                            orderDetailResList
                    );
                })
                .collect(Collectors.toList());
    }
}
