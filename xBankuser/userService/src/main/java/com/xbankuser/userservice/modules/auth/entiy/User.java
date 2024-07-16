package com.xbankuser.userservice.modules.auth.entiy;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "xbank_user")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @Nullable
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id = UUID.randomUUID();

    @NotBlank
    @NotNull
    @Column
    @Email
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 11, max = 14, message = "Phone number cannot be less than 14 digits and lesser than 10 digits")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank
    @NotNull
    @Column
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one number, and one special character")
    private String password;

    @NotBlank
    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @NotNull
    @Column(name = "first_name", nullable = false)
    private  String firstName;

    @Column(name = "email_verified" )
    private Boolean emailVerified = false;

    @Enumerated(EnumType.STRING) // EnumType.ORDINAL means you get 0, 1, 2 but string get the string value.
    private Role role;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
