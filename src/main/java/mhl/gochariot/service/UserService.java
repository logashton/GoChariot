package mhl.gochariot.service;

import mhl.gochariot.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    
    public UserService() {
    
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return toUserDetails(user);
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles(user.getRoleList().stream().map(r -> r.getRoleName()).toArray(String[]::new))
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /*
    public List getAllUsers() {
        System.out.println(UserRepository.findAll());
        return UserRepository.findAll();
    }
    
    /**
     * Finds a user by id
     * 
     * @param userId
     * @return a specified user

    public User getUserById(int userId) {
        return UserRepository.findByUserId(userId);
    }
    */
}
