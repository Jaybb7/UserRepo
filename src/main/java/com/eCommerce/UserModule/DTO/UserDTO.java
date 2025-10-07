package com.eCommerce.UserModule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private long id;
    private String password;
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String role = "USER";

}
