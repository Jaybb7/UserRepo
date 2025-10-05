package com.eCommerce.UserModule.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private Long userId;
    private Long itemId;
    private int orderQuantity;

}


