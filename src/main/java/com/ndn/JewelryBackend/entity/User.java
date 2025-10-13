package com.ndn.JewelryBackend.entity;

import com.ndn.JewelryBackend.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password không được để trống")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean emailConfirmation;

    private String confirmationCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserVoucher> userVouchers = new ArrayList<>();


    // ---------------- Spring Security ----------------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role == null ? List.of() : List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Luôn active, hoặc custom logic nếu muốn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Luôn không khóa, hoặc custom logic nếu muốn
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Luôn hợp lệ, hoặc custom logic nếu muốn
    }

    @Override
    public boolean isEnabled() {
        return this.emailConfirmation; // Chỉ enable khi email đã xác thực
    }
}
