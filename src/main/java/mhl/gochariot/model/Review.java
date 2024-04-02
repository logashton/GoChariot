package mhl.gochariot.model;

import jakarta.persistence.*;
import mhl.gochariot.model.Driver;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Entity
@Table(name = "Reviews")
public class Review {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewId")
    private Integer reviewId;

    @ManyToOne
    @Getter
    @Setter
    @NonNull
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @ManyToOne
    @Getter
    @Setter
    @NonNull
    @JoinColumn(name = "DriverId", nullable = false)
    private Driver driver;

    @Getter
    @Setter
    @NonNull
    @Column(name = "Rating", nullable = false)
    private Double rating;

    @Getter
    @Setter
    @NonNull
    @Column(name = "Content")
    private String content;
    
    @Getter
    @Setter
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
