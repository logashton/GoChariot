package mhl.gochariot.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Entity
@Table(name = "Student")
public class Student {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    private User user;

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
        return "Student{" +
                "studentId=" + studentId +
                ", rides=" + rides +
                ", fullName='" + fullName + '\'' +
                '}';
    }

}
