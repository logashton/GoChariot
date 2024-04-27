package mhl.gochariot.service;

import mhl.gochariot.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    RequestRepository requestRepository;

    public Page<RequestDTO> getRequestsByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("requestTime").descending());
        return requestRepository.findRequestsByUser(userId, pageable);
    }

}
