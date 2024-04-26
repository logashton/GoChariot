package mhl.gochariot.model;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Entity
@Getter
@Setter
@Table(name = "UserRole")
public class UserRole {
    @EmbeddedId
    private UserRoleKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "UserId")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "RoleId")
    private Role role;
}
