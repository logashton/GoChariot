package mhl.gochariot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Getter
@Setter
@Entity
@Table(name = "Driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "driverIDPGO", unique = true)
    private DriverName driverIdPGO;

    @NonNull
    private Integer hoursClocked;

    @NonNull
    private Integer rides;

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", user=" + user +
                ", driverIdPGO=" + driverIdPGO +
                ", hoursClocked=" + hoursClocked +
                ", rides=" + rides +
                '}';
    }
}
