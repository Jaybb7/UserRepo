package com.eCommerce.UserModule.ControllerTest;

import com.eCommerce.UserModule.Config.JwtUtil;
import com.eCommerce.UserModule.Controller.PublicController;
import com.eCommerce.UserModule.DTO.UserDTO;
import com.eCommerce.UserModule.Service.UserDetailServiceImpl;
import com.eCommerce.UserModule.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicController.class)
@AutoConfigureMockMvc(addFilters = false)
class PublicControllerHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDetailServiceImpl userDetailService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void testSignupReturnsCreated() throws Exception {
        UserDTO savedUser = new UserDTO();
        savedUser.setId(1L);
        savedUser.setUsername("john");

        when(userService.saveUser(any(UserDTO.class)))
                .thenReturn(CompletableFuture.completedFuture(savedUser));

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\", \"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void testSignupReturnsBadRequestWhenUserIsNull() throws Exception {
        when(userService.saveUser(any(UserDTO.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\", \"password\":\"password\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSignupReturnsInternalServerErrorOnException() throws Exception {
        when(userService.saveUser(any(UserDTO.class)))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\", \"password\":\"password\"}"))
                .andExpect(status().isInternalServerError());
    }
}
