package mhl.gochariot.service;

import mhl.gochariot.model.Role;
import mhl.gochariot.model.UserRole;
import mhl.gochariot.repository.RoleRepository;
import mhl.gochariot.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getRole(Integer id) {
        Optional<Role> opRole = roleRepository.findById(id);

        return opRole.get();
    }

}