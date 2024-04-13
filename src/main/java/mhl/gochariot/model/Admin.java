package mhl.gochariot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    private User user;


    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                '}';
    }
}
