package com.iongroup.data.user;

import com.iongroup.data.BaseEntity;
import com.iongroup.data.user.type.UserTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@ToString(callSuper = true)
@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String login;

    @ToString.Exclude
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String telephone;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_user_type", nullable = false)
    private UserTypeEntity type;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + type.getValue().toString()));
    }

    @Override
    public String getUsername() {
        return login;
    }

}
