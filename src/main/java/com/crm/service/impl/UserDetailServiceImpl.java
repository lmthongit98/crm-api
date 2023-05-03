package com.crm.service.impl;

import com.crm.common.enums.UserStatus;
import com.crm.model.User;
import com.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Set<GrantedAuthority> authorities = new HashSet<>();
        if(!Objects.isNull(user.getGroup())) {
           authorities = user.getGroup().getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toSet());
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

}
