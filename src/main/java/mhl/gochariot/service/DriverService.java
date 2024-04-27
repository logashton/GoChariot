package mhl.gochariot.service;

import mhl.gochariot.model.DriverName;
import mhl.gochariot.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    @Autowired
    DriverRepository driverRepository;

    @Autowired
    DriverNameService driverNameService;

    public boolean findByDriverIdPGO(Integer id) {
        Optional<DriverName> driverName = driverNameService.findByDriverIdPGO(id);
        if (driverName.isPresent()) {
            return driverRepository.findByDriverIdPGO(driverName.get()).isPresent();
        }

        return false;
    }
}
