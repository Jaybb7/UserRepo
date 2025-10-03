package com.eCommerce.UserModule.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private long userId;
    private String itemName;
    private int orderQuantity;

}
