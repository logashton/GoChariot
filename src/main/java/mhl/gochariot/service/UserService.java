package mhl.gochariot.service;

import mhl.gochariot.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.User;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    // for when a type of UserDetails is not wanted
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
