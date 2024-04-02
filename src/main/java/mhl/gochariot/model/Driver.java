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
    @Column(name = "DriverId")
    private Integer driverId;

    @Getter
    @Setter
    @NonNull
    @Column(name = "HoursClocked", nullable = false)
    private Integer hoursClocked;

    @Getter
    @Setter
    @NonNull
    @Column(name = "Rides", nullable = false)
    private Integer rides;

    @Getter
    @Setter
    @NonNull
    @Column(name = "FullName", nullable = false)
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
