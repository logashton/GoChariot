package mhl.gochariot.repository;

import mhl.gochariot.model.User;
import mhl.gochariot.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByUser(User user);
}
