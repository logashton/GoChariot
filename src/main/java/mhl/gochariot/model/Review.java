package mhl.gochariot.model;

import jakarta.persistence.*;
import mhl.gochariot.model.Driver;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Getter
@Setter
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @NonNull
    @Column(name = "DriverFirstName", nullable = false)
    private String driverFirstName;

    @NonNull
    @Column(name = "DriverLastName", nullable = false)
    private String driverLastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DriverIdPGO")
    private DriverName driverIdPGO;

    @NonNull
    @Column(name = "Rating", nullable = false)
    private Double rating;

    @NonNull
    @Column(name = "Content")
    private String content;

    @NonNull
    @Column(name = "CreatedAt", nullable = false)
    private Timestamp createdAt;

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", driverFirstName='" + driverFirstName + '\'' +
                ", driverLastName='" + driverLastName + '\'' +
                ", driverIdPGO=" + driverIdPGO +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
