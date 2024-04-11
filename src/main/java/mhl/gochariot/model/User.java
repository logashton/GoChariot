package mhl.gochariot.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class User {

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


    @OneToMany(fetch = FetchType.EAGER)
    List<Role> roleList;

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
}
