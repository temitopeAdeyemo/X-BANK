package com.xbankuser.userservice.modules.auth.service;

import com.xbankuser.userservice.modules.auth.entiy.Role;
import com.xbankuser.userservice.modules.auth.repository.UserRepository;
import com.xbankuser.userservice.shared.exception.UserNotFoundException;
import com.xbankuser.userservice.shared.mapper.UserDataMapper;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import proto.getUser.proto.GetAllUsersRequest;
import proto.getUser.proto.GetAllUsersResponse;
import proto.getUser.proto.GetUserByUniqueFieldRequest;
import proto.getUser.proto.User;
import proto.service.proto.GetUserServiceGrpc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@GrpcService
public class GetUser extends GetUserServiceGrpc.GetUserServiceImplBase {
    private final UserRepository userRepository;
    @Override
    public void getUserByUniqueField(GetUserByUniqueFieldRequest request, StreamObserver<User> responseObserver) {
        System.out.println("......................"+ request.getValue());
        var user = this.userRepository.findByUniqueData(request.getValue()).orElseThrow(()->new UserNotFoundException("User Not Found"));

        User userBuild = UserDataMapper.mapUserToProtobuf(user);

        responseObserver.onNext(userBuild);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsers(GetAllUsersRequest request, StreamObserver<GetAllUsersResponse> responseObserver) {
        int page = request.getPage() < 1? 0 : request.getPage();
        int size = request.getSize() < 1? 5 : request.getSize();

        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var probe = new com.xbankuser.userservice.modules.auth.entiy.User();
        User filter = request.getFilter();
//        System.out.println(Role.valueOf(filter.getRole())+".................."+probe.getRole());

        probe.setLastName(filter.getLastName());
        probe.setFirstName(filter.getFirstName());
        probe.setEmail(filter.getEmail());
        probe.setPhoneNumber(filter.getPhoneNumber());
        if(!filter.getRole().isEmpty()) probe.setRole(Role.valueOf(filter.getRole()));

//        if(!filter.getLastName().isEmpty()) probe.setLastName(filter.getLastName());
//        if(!filter.getFirstName().isEmpty()) probe.setFirstName(filter.getFirstName());
////        if(!filter.getId().isEmpty()) probe.setId(UUID.fromString(filter.getId()));
//        if(!filter.getEmail().isEmpty()) probe.setEmail(filter.getEmail());
//        if(!filter.getPhoneNumber().isEmpty()) probe.setPhoneNumber(filter.getPhoneNumber());
//        if(!filter.getRole().isEmpty()) probe.setRole(Role.valueOf(filter.getRole()));

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "emailVerified")
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();
//                .withIncludeNullValues();


        Example<com.xbankuser.userservice.modules.auth.entiy.User> ex = Example.of(probe, matcher);

//        System.out.println("..."+ex.getProbe());

//        Page<com.xbankuser.userservice.modules.auth.entiy.User> users = this.userRepository.findAllByEmailOrLastNameOrPhoneNumberOrRoleOrEmailVerified(
//                filter.getEmail(),
//                filter.getLastName(),
//                filter.getPhoneNumber(),
////                UUID.fromString(filter.getId()),
//                Role.valueOf(filter.getRole()),
//                filter.getEmailVerified(),
//                pageable
//                );

        System.out.println("::::::::::::::::::::::::");
        Page<com.xbankuser.userservice.modules.auth.entiy.User> users = this.userRepository.findAll(ex, pageable);

        List<User> userList = new ArrayList<>();

        for(var user: users) {
            User userBuild = UserDataMapper.mapUserToProtobuf(user);
            userList.add(userBuild);
        }
        System.out.println(Arrays.toString(userList.toArray()));
        responseObserver.onNext(GetAllUsersResponse.newBuilder().addAllUsers(userList).build());
        responseObserver.onCompleted();
    }
}
