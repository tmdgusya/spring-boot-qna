package com.codessquad.qna.repository;

import com.codessquad.qna.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    public void save(User user);

    public List<User> getUsers();

    public Optional<User> getUser(String userId);

    public void remove(String userId);

    public int size();
}
