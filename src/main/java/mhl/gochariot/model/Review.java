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

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    @JoinColumn(name = "DriverId", nullable = false)
    private Driver driver;

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
                "reviewId=" + id +
                ", user=" + user +
                ", driver=" + driver +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
