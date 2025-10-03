package com.eCommerce.UserModule.Controller;

import com.eCommerce.UserModule.Model.OrderRequest;
import com.eCommerce.UserModule.Service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest order){
        logger.info("Create Order");
        orderService.createOrder(order);
        return new ResponseEntity<>("Order created", HttpStatus.CREATED);
    }

}
