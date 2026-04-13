package com.example.journal.service;

import com.example.journal.entity.User;
import com.example.journal.exception.UserAlreadyExistsException;
import com.example.journal.repository.UserRepository;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    public User updateUser(User updateUserName) {
        return userRepository.save(updateUserName);
    }

    public String deleteUser(String id){
        userRepository.deleteById(id);
        return "User deleted";
    }

}
