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
    @Column(name = "StudentId")
    private Integer studentId;

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
        return "Student{" +
                "studentId=" + studentId +
                ", rides=" + rides +
                ", fullName='" + fullName + '\'' +
                '}';
    }

}
