package com.eCommerce.UserModule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDTO {

    private long id;
    private String itemName;
    private int quantity;
    private int price;

}
