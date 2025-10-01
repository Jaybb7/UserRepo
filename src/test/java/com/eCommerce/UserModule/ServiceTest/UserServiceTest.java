package com.eCommerce.UserModule.ServiceTest;

import com.eCommerce.UserModule.DTO.UserDTO;
import com.eCommerce.UserModule.Entity.User;
import com.eCommerce.UserModule.Repository.UserRepository;
import com.eCommerce.UserModule.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("john");
        userDTO.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john");
        savedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        CompletableFuture<UserDTO> result = userService.saveUser(userDTO);

        assertNotNull(result);
        assertEquals(1L, result.join().getId());
        assertNull(result.join().getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void testFindByUserName_UserFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("john")).thenReturn(user);

        UserDTO result = userService.findByUserName("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).findByUsername("john");
    }

    @Test
    void testFindByUserName_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        UserDTO result = userService.findByUserName("unknown");

        assertNull(result);
        verify(userRepository, times(1)).findByUsername("unknown");
    }

    @Test
    void testDeleteUser_UserFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(user);

        userService.deleteUser("john");

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser("unknown"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }
}
