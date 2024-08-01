package com.techspine.cms.repository.user;

import com.techspine.cms.entity.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImpl implements UserDetailsService {

    private final StudentRepository userRepository;

    public CustomUserServiceImpl(StudentRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Student user = userRepository.findByEmail(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found for email " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }
}
