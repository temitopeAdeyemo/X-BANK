package com.xbankuser.userservice.modules.auth.repository;

import com.xbankuser.userservice.modules.auth.entiy.Role;
import com.xbankuser.userservice.modules.auth.entiy.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String email);

    @Query("SELECT user FROM User user where user.email = :uniqueData OR cast(user.id as string ) = :uniqueData OR user.phoneNumber = :uniqueData")
    Optional<User> findByUniqueData(String uniqueData);

    @NotNull
    List<User> findAll(@NotNull Specification<User> spec);

//    Page<User> findAllByEmailOrLastNameOrPhoneNumberOrRoleOrEmailVerified(String email, String lastName, String phoneNumber, Role role, boolean emailVerified, PageRequest pageable);

}
