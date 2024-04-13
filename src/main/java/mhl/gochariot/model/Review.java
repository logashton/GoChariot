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
    @Column(name = "ReviewId")
    private Integer reviewId;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @ManyToOne
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
                "reviewId=" + reviewId +
                ", user=" + user +
                ", driver=" + driver +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
