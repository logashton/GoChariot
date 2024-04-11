package mhl.gochariot.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import org.hibernate.annotations.Fetch;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Integer userId;

    @NonNull
    @Column(name = "Email", unique = true, nullable = false)
    private String email;

    @NonNull
    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @NonNull
    @Column(name = "PasswordSalt", nullable = false)
    private String passwordSalt;

    @NonNull
    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private Timestamp createdAt;

    @NonNull
    @Column(name = "UpdatedAt", nullable = false)
    private Timestamp updatedAt;

    @NonNull
    @Column(name = "UserName", unique = true, nullable = false)
    private String username;

    @NonNull
    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @NonNull
    @Column(name = "LastName", nullable = false)
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UserRoles",
            joinColumns = @JoinColumn(name = "UserId"),
            inverseJoinColumns = @JoinColumn(name = "RoleId")
    )
    List<Role> roles;

    @Override
    public String toString() {
        return "User{"
                + "userId=" + userId
                + ", email='" + email + '\''
                + ", passwordHash='" + passwordHash + '\''
                + ", passwordSalt='" + passwordSalt + '\''
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.getPasswordHash();
    }


    //todo: add these to the schema
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
        return true;
    }
}
