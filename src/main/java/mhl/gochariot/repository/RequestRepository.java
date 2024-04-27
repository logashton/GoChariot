package mhl.gochariot.repository;

import mhl.gochariot.model.Request;
import mhl.gochariot.service.RequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RequestRepository  extends JpaRepository<Request, Integer> {
    @Query("SELECT NEW mhl.gochariot.service.RequestDTO(r.id, r.route, r.pickUp, r.dropOff, r.status, u.userId, u.username, d.driverId, d.driverIdPGO.driverIdPGO, du.firstName, du.lastName) FROM Request r JOIN r.user u JOIN r.driver d JOIN d.user du WHERE u.userId = ?1")
    Page<RequestDTO> findRequestsByUser(Integer id, Pageable pageable);
}
