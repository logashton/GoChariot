package mhl.gochariot.service;

import mhl.gochariot.repository.DriverNameRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.DriverName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DriverNameService {
    @Autowired
    DriverNameRepository DriverNameRepository;

    public DriverNameDTO findDriverByFirstLast(String firstName, String lastName) {
        return DriverNameRepository.findByFirstAndLastName(firstName, lastName);
    }

    public List<DriverNameDTO> findAllDriverNames() {
        return DriverNameRepository.findAllDriverNames();
    }
}
