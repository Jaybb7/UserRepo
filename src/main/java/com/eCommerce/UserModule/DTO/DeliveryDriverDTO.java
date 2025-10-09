package com.eCommerce.UserModule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDriverDTO {

    private Long driverId;
    private String driverName;
    private Double ratings;
    private String driverPhone;
    private String driverEmail;
    private String driverAddress;

}
