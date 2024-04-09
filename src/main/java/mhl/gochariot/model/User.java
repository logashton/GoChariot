package mhl.gochariot.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Integer userId;

    @Getter
    @Setter
    @NonNull
    @Column(name = "Email", unique = true, nullable = false)
    private String email;

    @Getter
    @Setter
    @NonNull
    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @Getter
    @Setter
    @NonNull
    @Column(name = "PasswordSalt", nullable = false)
    private String passwordSalt;

    @Getter
    @Setter
    @NonNull
    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Getter
    @Setter
    @NonNull
    @Column(name = "UpdatedAt", nullable = false)
    private Timestamp updatedAt;

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
