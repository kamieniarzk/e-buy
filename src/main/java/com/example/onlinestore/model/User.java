package com.example.onlinestore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_profile")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    @NotNull
    @Length(min = 2, max = 20)
    private String username;
    @NotNull
    private String password;
    @NotNull
    @Length(min = 2, max = 20)
    private String firstName;
    @NotNull
    @Length(min = 2, max = 20)
    private String lastName;
    private String role;
    private String imageUrl;
    @Column(columnDefinition = "TEXT")
    private String aboutMe;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public User(String username, String password, String firstName, String lastName, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired,
                boolean isEnabled, Set<SimpleGrantedAuthority> grantedAuthorities) {
        this.username = username;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.grantedAuthorities = grantedAuthorities;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns=@JoinColumn(name="id"))
    private Set<SimpleGrantedAuthority> grantedAuthorities;

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

}
