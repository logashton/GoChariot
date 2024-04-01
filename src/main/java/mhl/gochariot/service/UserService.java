package mhl.gochariot.service;

import mhl.gochariot.repository.UserRepository;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {

    @Autowired
    UserRepository UserRepository;
    
    public UserService() {
    
    }

    /**
     * Get entire list of users.
     *
     * @return the user list.
     */
    public List getAllUsers() {
        System.out.println(UserRepository.findAll());
        return UserRepository.findAll();
    }
    
    /**
     * Finds a user by id
     * 
     * @param userId
     * @return a specified user
     */
    public User getUserById(int userId) {
        return UserRepository.findByUserId(userId);
    }
}
