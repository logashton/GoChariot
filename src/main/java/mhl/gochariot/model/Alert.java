package mhl.gochariot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import jakarta.persistence.*;
import mhl.gochariot.model.Driver;
import mhl.gochariot.model.User;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "Alert")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    private User user;

    @NonNull
    @Column(name = "Content")
    private String content;

    @NonNull
    @Column(name = "Title")
    private String title;

    @NonNull
    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}


