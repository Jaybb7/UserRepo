package com.eCommerce.UserModule.Repository;

import com.eCommerce.UserModule.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT * FROM `user` u WHERE u.phone LIKE CONCAT(:countryCode, '%')", nativeQuery = true)
    List<User> findByPhoneStartingWith(@Param("countryCode") String countryCode);

}
