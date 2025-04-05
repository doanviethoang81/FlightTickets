package com.example.banvemaybay.services;

import com.example.banvemaybay.repositorys.NguoiDatRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final NguoiDatRepository nguoiDatRepository;

    public CustomUserDetailService(NguoiDatRepository userRepository) {
        this.nguoiDatRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.example.banvemaybay.models.NguoiDat user=nguoiDatRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities // Gán danh sách role vào UserDetails
        );
    }
}
