package com.project.ordermakanan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.ordermakanan.models.User;
import com.project.ordermakanan.models.UserDetail;
import com.project.ordermakanan.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    UserRepository usersRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByUsername(username).get();
        return UserDetail.build(user);
    }
}
