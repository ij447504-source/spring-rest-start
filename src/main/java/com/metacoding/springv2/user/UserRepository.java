package com.metacoding.springv2.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//findByid, findAll, save, deleteByid, count
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.username = :username")
    public Optional<User> findByUsername(@Param("username") String username);
}