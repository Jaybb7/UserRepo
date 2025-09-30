package com.eCommerce.UserModule.Controller;

import com.eCommerce.UserModule.DTO.UserDTO;
import com.eCommerce.UserModule.Service.UserService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @PutMapping("/updatePassword")
    public ResponseEntity<UserDTO> updateUserPasswords(@RequestBody UserDTO userDTO){
        logger.info("Password update request received.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO user = userService.findByUserName(username);
        if(user != null){
            user.setPassword(userDTO.getPassword());
            UserDTO savedUser = userService.saveUser(user);
            logger.info("Password updated successfully for username: {}", username);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }else{
            logger.error("Password update failed. User {} not found.", username);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("Delete request received for username: {}", username);
        UserDTO user = userService.findByUserName(username);
        if (user != null) {
            try {
                userService.deleteUser(username);
                logger.info("User {} deleted successfully.", username);
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error deleting user {}: {}", username, e.getMessage(), e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            logger.error("Delete request failed. User {} not found.", username);
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/findUsersByCountry")
    public ResponseEntity<List<UserDTO>> findUsersByCountry(@RequestParam("countryCode") String countryCode) {
        logger.info("Fetching users with country code: {}", countryCode);
        List<UserDTO> userDTOS = userService.findAllUsersByCountryCode(countryCode);
        logger.info("Found {} users with country code: {}", userDTOS.size(), countryCode);
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

}
