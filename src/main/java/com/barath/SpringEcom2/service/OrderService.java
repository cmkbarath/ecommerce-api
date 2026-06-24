package com.barath.SpringEcom2.service;

import com.barath.SpringEcom2.model.Order;
import com.barath.SpringEcom2.model.OrderItem;
import com.barath.SpringEcom2.model.Product;
import com.barath.SpringEcom2.model.dto.OrderItemRequest;
import com.barath.SpringEcom2.model.dto.OrderItemResponse;
import com.barath.SpringEcom2.model.dto.OrderRequest;
import com.barath.SpringEcom2.model.dto.OrderResponse;
import com.barath.SpringEcom2.repo.OrderRepo;
import com.barath.SpringEcom2.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo proRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {


        Order order = new Order();

        String orderId = "ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);

        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> items = new ArrayList<>();

        for(OrderItemRequest req : request.items()){

            Product p = proRepo.findById(req.productId()).
                    orElseThrow(() -> new RuntimeException("Product not found with ID: " + req.productId()));


            int updatedStock = p.getStockQuantity()-req.quantity();
            p.setStockQuantity(updatedStock);

            if(updatedStock==0){
                p.setProductAvailable(false);
            }

            proRepo.save(p);


            OrderItem items1 = OrderItem.builder()
                    .product(p)
                    .quantity(req.quantity())
                    .totalPrice(p.getPrice().multiply(BigDecimal.valueOf(req.quantity())))
                    .order(order)
                    .build();

            items.add(items1);
        }

        order.setOrderItems(items);

        Order SavedList =  orderRepo.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for(OrderItem item2 : items){
            OrderItemResponse res2 = new OrderItemResponse(
                    item2.getProduct().getName(),
                    item2.getQuantity(),
                    item2.getTotalPrice()
            );
            itemResponses.add(res2);
        }

        OrderResponse response = new OrderResponse(
                SavedList.getOrderId(),
                SavedList.getCustomerName(),
                SavedList.getEmail(),
                SavedList.getStatus(),
                SavedList.getOrderDate(),
                itemResponses
        );

        return response;
    }

    public List<OrderResponse> getAllOrderResponses() {

        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> responses = new ArrayList<>();

        for(Order order : orders){

            List<OrderItemResponse> itemResponseList = new ArrayList<>();

            for(OrderItem items : order.getOrderItems()){
                OrderItemResponse itemResponse = new OrderItemResponse(
                        items.getProduct().getName(),
                        items.getQuantity(),
                        items.getTotalPrice()
                );
                itemResponseList.add(itemResponse);
            }

            OrderResponse response = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponseList
            );

            responses.add(response);
        }
        return responses;
    }

}
