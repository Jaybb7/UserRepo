package com.eCommerce.UserModule.Repository;

import com.eCommerce.UserModule.DTO.UserPhoneProjection;
import com.eCommerce.UserModule.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT u.id AS id, u.email AS email, u.first_name AS firstName, u.last_name AS lastName FROM `user` u WHERE u.phone LIKE CONCAT(:countryCode, '%')", nativeQuery = true)
    List<UserPhoneProjection> findByPhoneStartingWith(@Param("countryCode") String countryCode);

}
