package com.codessquad.qna.service;

import com.codessquad.qna.entity.User;
import com.codessquad.qna.repository.UserRepository;
import com.codessquad.qna.repository.UserRepositoryMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository = new UserRepositoryMap(new HashMap<>());

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUser(String userId) {
        return userRepository.getUser(userId);
    }

}