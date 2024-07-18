package com.xbankuser.userservice.modules.auth.service;

import com.xbankuser.userservice.modules.auth.entiy.Role;
import com.xbankuser.userservice.modules.auth.repository.UserRepository;
import com.xbankuser.userservice.shared.exception.UserNotFoundException;
import com.xbankuser.userservice.shared.mapper.UserDataMapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.*;
import proto.getUser.proto.GetAllUsersRequest;
import proto.getUser.proto.GetAllUsersResponse;
import proto.getUser.proto.GetUserByUniqueFieldRequest;
import proto.getUser.proto.User;
import proto.service.proto.GetUserServiceGrpc;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@GrpcService
public class GetUser extends GetUserServiceGrpc.GetUserServiceImplBase {
    private final UserRepository userRepository;
    @Override
    public void getUserByUniqueField(GetUserByUniqueFieldRequest request, StreamObserver<User> responseObserver) {
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

        probe.setLastName(filter.getLastName());
        probe.setFirstName(filter.getFirstName());
        probe.setEmail(filter.getEmail());
        probe.setPhoneNumber(filter.getPhoneNumber());
        if(!filter.getRole().isEmpty()) probe.setRole(Role.valueOf(filter.getRole()));

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "emailVerified")
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();
//                .withIncludeNullValues();

        Example<com.xbankuser.userservice.modules.auth.entiy.User> ex = Example.of(probe, matcher);

        Page<com.xbankuser.userservice.modules.auth.entiy.User> users = this.userRepository.findAll(ex, pageable);

        List<User> userList = new ArrayList<>();

        for(var user: users) {
            User userBuild = UserDataMapper.mapUserToProtobuf(user);
            userList.add(userBuild);
        }

        responseObserver.onNext(GetAllUsersResponse.newBuilder().addAllUsers(userList).build());
        responseObserver.onCompleted();
    }
}
