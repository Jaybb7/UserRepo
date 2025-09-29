package com.eCommerce.UserModule.Repository;

import com.eCommerce.UserModule.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
