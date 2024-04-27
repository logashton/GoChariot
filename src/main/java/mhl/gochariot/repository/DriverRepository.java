package mhl.gochariot.repository;

import mhl.gochariot.model.DriverName;
import mhl.gochariot.model.User;
import mhl.gochariot.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByUser(User user);

    Optional<Driver> findByDriverIdPGO(DriverName DriverIdPGO);
}
