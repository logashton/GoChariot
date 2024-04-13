package mhl.gochariot.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Getter
@Setter
@Entity
@Table(name = "Student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    private User user;

    @NonNull
    private Integer rides;

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", rides=" + rides +
                '}';
    }

}
