package com.barath.SpringEcom2.controller;

import com.barath.SpringEcom2.model.dto.OrderRequest;
import com.barath.SpringEcom2.model.dto.OrderResponse;
import com.barath.SpringEcom2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderservice;

    @PostMapping("orders/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request){
        OrderResponse orderres = orderservice.placeOrder(request);
        return new ResponseEntity<>(orderres, HttpStatus.CREATED);
    }

    @GetMapping("orders")
    public ResponseEntity<List<OrderResponse>> getAllOrder(){
        List<OrderResponse> responses = orderservice.getAllOrderResponses();
        return new ResponseEntity<>(responses,HttpStatus.OK);
    }

}
