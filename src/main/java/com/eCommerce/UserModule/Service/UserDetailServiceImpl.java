package com.eCommerce.UserModule.Service;

import com.eCommerce.UserModule.Entity.User;
import com.eCommerce.UserModule.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
                    .password(user.getPassword()).build();
        }
        throw new UsernameNotFoundException("User not found");
    }

}