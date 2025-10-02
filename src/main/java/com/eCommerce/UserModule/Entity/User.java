package com.eCommerce.UserModule.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String password;
    @Column(unique = true, nullable = false)
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;

}
