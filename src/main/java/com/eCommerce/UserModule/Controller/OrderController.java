package com.eCommerce.UserModule.Controller;

import com.eCommerce.UserModule.Model.OrderRequest;
import com.eCommerce.UserModule.Service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/createOrder")
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody OrderRequest order){
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order created successfully");
        orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
