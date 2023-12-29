package com.university.post.security;

import com.university.post.model.User;
import com.university.post.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Kiểm tra xem user có tồn tại trong database không?
        User user = userRepository.findByUserId(Integer.valueOf(username));
        if (user == null) {
            throw new UsernameNotFoundException("UserId không tìm thấy");
        }
        return new CustomUserDetails(user);
    }
}