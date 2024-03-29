package com.coinskash.service;

import com.coinskash.model.AppUser;
import com.coinskash.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class ImplUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser userData = userRepository.findByUsername(username);
        if (userData == null){
            throw new UsernameNotFoundException(username);
        }

        log.info("I come here everytime you try to login {}");
        boolean enabled = userData.isVerified();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userData.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        });

        return new User(userData.getUsername(),userData.getPassword(),enabled,true,true,true, authorities);
    }


}
