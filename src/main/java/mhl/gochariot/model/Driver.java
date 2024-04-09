package mhl.gochariot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Entity
@Table(name = "Driver")
public class Driver {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

    @OneToOne
    @Getter
    @Setter
    @JoinColumn(name = "userId", unique = true)
    private User user;

    @Getter
    @Setter
    @NonNull
    private Integer hoursClocked;

    @Getter
    @Setter
    @NonNull
    private Integer rides;

    @Getter
    @Setter
    @NonNull
    private String fullName;

    @Override
    public String toString() {
        return "Driver{"
                + "driverId=" + driverId
                + ", hoursClocked=" + hoursClocked
                + ", rides=" + rides
                + ", fullName='" + fullName + '\''
                + '}';
    }
}
