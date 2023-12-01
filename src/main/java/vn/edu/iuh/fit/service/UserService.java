package vn.edu.iuh.fit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.models.Post;
import vn.edu.iuh.fit.models.User;
import vn.edu.iuh.fit.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public Optional<User> validateUser(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
