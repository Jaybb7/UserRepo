package com.eCommerce.UserModule.Service;

import com.eCommerce.UserModule.DTO.UserDTO;
import com.eCommerce.UserModule.Entity.User;
import com.eCommerce.UserModule.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserDTO saveUser(UserDTO userDTO) {
        try {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            User savedUser = userRepository.save(user);
            userDTO.setId(savedUser.getId());
            userDTO.setPassword(savedUser.getPassword());
            return userDTO;
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUsername(username);
        if(user != null){
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }else{
            return null;
        }
    }
}
