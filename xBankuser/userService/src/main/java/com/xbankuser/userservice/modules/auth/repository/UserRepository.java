package com.xbankuser.userservice.modules.auth.repository;

import com.xbankuser.userservice.modules.auth.entiy.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String email);

    @Query("SELECT user FROM User user where user.email = :uniqueData OR cast(user.id as string ) = :uniqueData OR user.phoneNumber = :uniqueData")
    Optional<User> findByUniqueData(String uniqueData);

//    @NotNull
//    @Override
//    <S extends User> S save(@NotNull S entity);
}
