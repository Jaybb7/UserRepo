package com.eCommerce.UserModule.DTO;

import com.eCommerce.UserModule.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDTO {

    private long orderId;
    private long userId;
    private OrderStatus orderStatus;


}
