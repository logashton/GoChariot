package mhl.gochariot.repository;

import mhl.gochariot.model.DriverName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DriverNameRepository extends JpaRepository<DriverName, Integer>{
    @Query("SELECT d, u.userId FROM DriverName d JOIN d.driver u WHERE d.firstName = :firstName AND d.lastName = :lastName")
    Object[] findByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT d.id, d.firstName, d.lastName, u.userId FROM DriverName d JOIN d.driver u")
    List<Object[]> findAllDriverNames();

}
