package com.eCommerce.UserModule.Service;

import com.eCommerce.UserModule.DTO.UserDTO;
import com.eCommerce.UserModule.DTO.UserPhoneProjection;
import com.eCommerce.UserModule.Entity.User;
import com.eCommerce.UserModule.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public CompletableFuture<UserDTO> saveUser(UserDTO userDTO) {
        logger.info("Attempting to save user with username: {}", userDTO.getUsername());
        try {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            User savedUser = userRepository.save(user);
            userDTO.setId(savedUser.getId());
            userDTO.setPassword(null);
            logger.info("User saved successfully with ID: {}", savedUser.getId());
            return CompletableFuture.completedFuture(userDTO);
        } catch (BeansException e) {
            logger.error("Error saving user with username: {}", userDTO.getUsername(), e);
            throw new RuntimeException(e);
        }
    }

    public UserDTO findByUserName(String username) {
        logger.info("Searching for user with username: {}", username);
        User user = userRepository.findByUsername(username);
        if(user != null){
            logger.info("User {} found with ID: {}", user.getUsername(), user.getId());
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }else{
            logger.warn("User {} not found.", username);
            return null;
        }
    }

    public void deleteUser(String username) {
        logger.info("Delete request received for username: {}", username);
        User user = userRepository.findByUsername(username);
        if(user != null){
            logger.info("User {} found. Proceeding with deletion.", username);
            userRepository.delete(user);
            logger.info("User {} deleted successfully.", username);
        }else{
            logger.error("Delete request failed. User {} not found.", username);
            throw new RuntimeException("User not found");
        }
    }

    public List<UserDTO> findAllUsersByCountryCode(String countryCode) {
        logger.info("Fetching users by country code: {}", countryCode);
        List<UserPhoneProjection> users = userRepository.findByPhoneStartingWith(countryCode);
        List<UserDTO> userDTOs = new ArrayList<>();
        if(!users.isEmpty()){
            logger.info("Found {} users with country code: {}", users.size(), countryCode);
            for(UserPhoneProjection user : users){
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
                userDTO.setPassword(null);
                userDTOs.add(userDTO);
            }
            return userDTOs;
        }else{
            logger.warn("No users found with country code: {}", countryCode);
            return userDTOs;
        }
    }

    public boolean isUsernameExist(String username) {
        logger.info("Searching for user with username: {}", username);
        User user = userRepository.findByUsername(username);
        if(user != null){
            logger.info("User {} found with ID: {}", user.getUsername(), user.getId());
            return true;
        }else{
            logger.warn("User {} not found.", username);
            return false;
        }
    }

}
