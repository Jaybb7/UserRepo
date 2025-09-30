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
    public ResponseEntity<SignUpResponse> signup(@RequestBody UserDTO userDTO) {
        logger.info("Signup attempt for username: {}", userDTO.getUsername());
        UserDTO user = userService.saveUser(userDTO);
        if(user != null){
            logger.info("User {} signed up successfully with ID {}", user.getUsername(), user.getId());
            SignUpResponse signUpResponse = new SignUpResponse();
            signUpResponse.setId(user.getId());
            signUpResponse.setUsername(user.getUsername());
            return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
        }else{
            logger.error("Signup failed for username: {}", userDTO.getUsername());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
