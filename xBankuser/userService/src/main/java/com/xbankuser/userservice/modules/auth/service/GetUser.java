package com.xbankuser.userservice.modules.auth.service;

import com.xbankuser.userservice.modules.auth.entiy.Role;
import com.xbankuser.userservice.modules.auth.repository.UserRepository;
import com.xbankuser.userservice.shared.exception.UserNotFoundException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import proto.getUser.proto.GetAllUsersRequest;
import proto.getUser.proto.GetAllUsersResponse;
import proto.getUser.proto.GetUserByUniqueFieldRequest;
import proto.getUser.proto.User;
import proto.service.proto.GetUserServiceGrpc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetUser extends GetUserServiceGrpc.GetUserServiceImplBase {
    private final UserRepository userRepository;

    @Override
    public void getUserByUniqueField(GetUserByUniqueFieldRequest request, StreamObserver<User> responseObserver) {
        var user = this.userRepository.findByUniqueData(request.getValue());

        if(user.isEmpty())throw new UserNotFoundException("User Not Found");

        var userInfo = user.get();

        User userBuild = User.newBuilder()
                .setId(String.valueOf(userInfo.getId()))
                .setEmail(userInfo.getEmail())
                .setPhoneNumber(userInfo.getPhoneNumber())
                .setEmailVerified(userInfo.getEmailVerified())
                .setRole(userInfo.getRole().name())
                .setFirstName(userInfo.getFirstName())
                .setLastName(userInfo.getLastName())
                .build();

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

        if(!filter.getLastName().isEmpty()) probe.setLastName(filter.getLastName());
        if(!filter.getFirstName().isEmpty()) probe.setFirstName(filter.getFirstName());
        if(!filter.getId().isEmpty()) probe.setId(UUID.fromString(filter.getId()));
        if(!filter.getEmail().isEmpty()) probe.setEmail(filter.getEmail());
        if(!filter.getPhoneNumber().isEmpty()) probe.setPhoneNumber(filter.getPhoneNumber());
        if(!filter.getRole().isEmpty()) probe.setRole(Role.valueOf(filter.getRole()));

        Example<com.xbankuser.userservice.modules.auth.entiy.User> ex = Example.of(probe);

        Page<com.xbankuser.userservice.modules.auth.entiy.User> users = this.userRepository.findAll(ex, pageable);

        List<User> userList = new ArrayList<>();

        for(var user: users) {
            User userBuild = User.newBuilder()
                    .setId(String.valueOf(user.getId()))
                    .setEmail(user.getEmail())
                    .setPhoneNumber(user.getPhoneNumber())
                    .setEmailVerified(user.getEmailVerified())
                    .setRole(user.getRole().name())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .build();
            userList.add(userBuild);
        }

        responseObserver.onNext(GetAllUsersResponse.newBuilder().addAllUsers(userList).build());
        responseObserver.onCompleted();
    }
}
