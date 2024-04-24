package mhl.gochariot.repository;

import mhl.gochariot.model.DriverName;
import mhl.gochariot.service.DriverNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface DriverNameRepository extends JpaRepository<DriverName, Integer> {
    @Query("SELECT NEW mhl.gochariot.service.DriverNameDTO(d.driverIdPGO, d.firstName, d.lastName, u.userId, d.firstSeen, d.lastSeen) FROM DriverName d LEFT JOIN d.driver u WHERE d.firstName = :firstName AND d.lastName = :lastName")
    DriverNameDTO findByFirstAndLastNameDTO(@Param("firstName") String firstName, @Param("lastName") String lastName);

    Optional<DriverName> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT NEW mhl.gochariot.service.DriverNameDTO(d.driverIdPGO, d.firstName, d.lastName, u.userId, d.firstSeen, d.lastSeen) FROM DriverName d LEFT JOIN d.driver u")
    List<DriverNameDTO> findAllDriverNames();

    Optional<DriverName> findBydriverIdPGO(Integer driverIdPGO);
}
