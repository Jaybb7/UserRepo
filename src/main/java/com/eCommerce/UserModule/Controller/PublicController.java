package com.eCommerce.UserModule.Controller;

import com.eCommerce.UserModule.Config.JwtUtil;
import com.eCommerce.UserModule.DTO.UserDTO;
import com.eCommerce.UserModule.Model.AuthRequest;
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

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PublicController {

    private final UserService userService;
    private final UserDetailServiceImpl userDetailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody UserDTO userDTO){
        UserDTO user = userService.saveUser(userDTO);
        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails = userDetailService.loadUserByUsername(authRequest.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
