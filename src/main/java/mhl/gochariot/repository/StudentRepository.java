package mhl.gochariot.repository;

import mhl.gochariot.model.Student;
import mhl.gochariot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer>{
    Optional<Student> findByUser(User user);
}
