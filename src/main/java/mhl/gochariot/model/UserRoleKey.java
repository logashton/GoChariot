package mhl.gochariot.model;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class UserRoleKey implements Serializable {

    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "RoleId")
    private Integer roleId;

    @Override
    public String toString() {
        return "UserRoleKey{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}