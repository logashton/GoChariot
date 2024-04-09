package mhl.gochariot.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Entity
@Table(name = "UserRoles")
public class UserRole {
    @Id
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Id
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;

    @Override
    public String toString() {
        return "UserRole{" +
                "user=" + user +
                ", role=" + role +
                '}';
    }
}
