package mhl.gochariot.service;

import mhl.gochariot.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlertService {
    @Autowired
    AlertRepository alertRepository;

    public List<Alert> findAllAlerts() {
        return alertRepository.findAlertsRecent();
    }
}
