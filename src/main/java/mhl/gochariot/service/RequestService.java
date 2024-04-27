package mhl.gochariot.service;

import mhl.gochariot.model.Driver;
import mhl.gochariot.model.User;
import mhl.gochariot.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    RequestRepository requestRepository;

    public Page<RequestDTO> getRequestsByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("requestTime").descending());
        return requestRepository.findRequestsByUser(userId, pageable);
    }

    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    public boolean driverAlreadyRequested(User user, Driver driver) {
        return !requestRepository.findByUserAndDriverAndStatus(
                user,
                driver,
                "pending"
        ).isEmpty();
    }

    public List<Request> findAllRequests() {
        return requestRepository.findAll();
    }

    public Optional<Request> findById(Integer id) {
        return requestRepository.findById(id);
    }

    public Page<RequestDTO> getRequestsByDriverIdAndStatus(Integer driverId, String status, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("requestTime").descending());
        return requestRepository.findRequestsByDriverIdAndStatus(driverId, status, pageable);
    }

    public void updateRequestStatus(Integer id, String status) {
        Optional<Request> optionalRequest = requestRepository.findById(id);
        status = status.toLowerCase();

        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            request.setStatus(status);

            if (Objects.equals(status, "accepted")) {
                request.setAcceptTime(new Timestamp(System.currentTimeMillis()));
            }

            requestRepository.save(request);
        }
    }

}
