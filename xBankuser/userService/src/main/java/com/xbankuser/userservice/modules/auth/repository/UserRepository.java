package com.xbankuser.userservice.modules.auth.repository;

import com.xbankuser.userservice.modules.auth.entiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String email);
}
