package mhl.gochariot.service;

import mhl.gochariot.repository.DriverNameRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.DriverName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DriverNameService {
    @Autowired
    DriverNameRepository driverNameRepository;

    public DriverNameDTO findDriverByFirstLast(String firstName, String lastName) {
        return driverNameRepository.findByFirstAndLastNameDTO(firstName, lastName);
    }

    public DriverName saveDriverName(DriverName driverName) {
        return driverNameRepository.save(driverName);
    }

    public void updateLastSeen(Integer id, Timestamp lastSeen) {
        Optional<DriverName> optionalDriverName = driverNameRepository.findBydriverIdPGO(id);

        if (optionalDriverName.isPresent()) {
            DriverName driverName = optionalDriverName.get();
            driverName.setLastSeen(new Timestamp(System.currentTimeMillis()));
            driverNameRepository.save(driverName);
        }

    }

    public List<DriverNameDTO> findAllDriverNames() {
        return driverNameRepository.findAllDriverNames();
    }

    public Optional<DriverName> findByDriverIdPGO(Integer id) {
        return driverNameRepository.findBydriverIdPGO(id);
    }

    public void deleteDriverName(DriverName driverName) {
        driverNameRepository.delete(driverName);
    }
}
