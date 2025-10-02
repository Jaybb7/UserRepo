package com.eCommerce.UserModule.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eCommerce.UserModule.Config.JwtUtil;
import com.eCommerce.UserModule.DTO.UserDTO;
import com.eCommerce.UserModule.Model.AuthRequest;
import com.eCommerce.UserModule.Model.SignUpResponse;
import com.eCommerce.UserModule.Service.UserDetailServiceImpl;
import com.eCommerce.UserModule.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PublicController {

    private static final Logger logger = LogManager.getLogger(PublicController.class);

    private final UserService userService;
    private final UserDetailServiceImpl userDetailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        logger.info("Signup attempt for username: {}", userDTO.getUsername());
        try {
            if(!userService.isUsernameExist(userDTO.getUsername())){
                var savedUser = userService.saveUser(userDTO).join();
                if (savedUser != null) {
                    logger.info("User {} signed up successfully with ID {}", savedUser.getUsername(), savedUser.getId());
                    SignUpResponse signUpResponse = new SignUpResponse();
                    signUpResponse.setId(savedUser.getId());
                    signUpResponse.setUsername(savedUser.getUsername());
                    return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponse);
                } else {
                    logger.error("Signup failed for username: {}", userDTO.getUsername());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }else{
                logger.error("User {} already exists", userDTO.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            }
        } catch (Exception ex) {
            logger.error("Error during signup for username: {}", userDTO.getUsername(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest){
        try {
            logger.info("Login attempt for username: {}", authRequest.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails = userDetailService.loadUserByUsername(authRequest.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            logger.info("User {} logged in successfully. Token generated.", authRequest.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.error("Login failed for username: {}", authRequest.getUsername(), e);
            return status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
