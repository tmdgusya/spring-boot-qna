package com.codessquad.qna.repository;

import com.codessquad.qna.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUserId(String id);

    void deleteUserById(Long id);

    List<User> findAll();

}
