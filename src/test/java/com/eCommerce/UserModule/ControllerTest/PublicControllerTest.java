package com.eCommerce.UserModule.ControllerTest;

import com.eCommerce.UserModule.Controller.PublicController;
import com.eCommerce.UserModule.DTO.UserDTO;
import com.eCommerce.UserModule.Model.SignUpResponse;
import com.eCommerce.UserModule.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublicControllerTest {

    private UserService userService;
    private PublicController publicController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        publicController = new PublicController(
                userService,
                null,
                null,
                null
        );
    }

    @Test
    void testSignup_Success() {
        UserDTO input = new UserDTO();
        input.setUsername("john");
        input.setPassword("password");

        UserDTO saved = new UserDTO();
        saved.setId(1L);
        saved.setUsername("john");

        when(userService.saveUser(any(UserDTO.class)))
                .thenReturn(CompletableFuture.completedFuture(saved));

        ResponseEntity<?> response = publicController.signup(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertInstanceOf(SignUpResponse.class, response.getBody());
        SignUpResponse body = (SignUpResponse) response.getBody();
        assertEquals(1L, body.getId());
        assertEquals("john", body.getUsername());
    }

    @Test
    void testSignup_Failure_NullUser(){
        UserDTO input = new UserDTO();
        input.setUsername("jane");

        when(userService.saveUser(any(UserDTO.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        ResponseEntity<?> response = publicController.signup(input);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSignup_Exception(){
        UserDTO input = new UserDTO();
        input.setUsername("errorUser");

        when(userService.saveUser(any(UserDTO.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("DB error")));

        ResponseEntity<?> response = publicController.signup(input);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
