package mhl.gochariot.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;


@Getter
@Setter
@Entity
@Table(name = "DriverName")
public class DriverName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NonNull
    @Column(name = "FirstName")
    private String firstName;

    @NonNull
    @Column(name = "LastName")
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserId", nullable = true)
    private User driver;

    @NonNull
    @Column(name = "FirstSeen")
    private Timestamp firstSeen;

    @NonNull
    @Column(name = "LastSeen")
    private Timestamp lastSeen;

    @Override
    public String toString() {
        return "DriverName{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", driver=" + driver +
                ", firstSeen=" + firstSeen +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
