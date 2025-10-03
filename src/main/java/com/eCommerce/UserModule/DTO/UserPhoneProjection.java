package com.eCommerce.UserModule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneProjection {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;

}
