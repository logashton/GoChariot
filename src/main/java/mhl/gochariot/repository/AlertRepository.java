package mhl.gochariot.repository;

import mhl.gochariot.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Integer> {
    @Query("SELECT a FROM Alert a ORDER BY a.createdAt DESC")
    List<Alert> findAlertsRecent();
}
