package com.example.banvemaybay.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table( name ="nguoi_dat")
public class NguoiDat implements UserDetails {

    @Id // tu dong tang len 1
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ho;

    private String ten;

    @Column(name = "so_dien_thoai", nullable = false)
    private String soDienThoai;

    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "provider")
    private String provider;

    private String activationCode;

    private LocalDateTime activationCodeExpiry;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "nguoidat_roles",
            joinColumns = @JoinColumn(name = "nguoidat_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) return List.of();
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password != null ? password : "";
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(enable);
    }
}
