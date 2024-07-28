package com.xbank.walletservice.shared.service.xbank;

import com.xbank.walletservice.shared.exception.UpstreamlServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.getUser.proto.GetUserByUniqueFieldRequest;
import proto.getUser.proto.GetUserUniqueFields;
import proto.getUser.proto.User;
import proto.service.proto.GetUserServiceGrpc;

@Service
public class UserClient {

    private final GetUserServiceGrpc.GetUserServiceBlockingStub synchronousUserClient;

    @Autowired
    public UserClient(GetUserServiceGrpc.GetUserServiceBlockingStub synchronousUserClient){
        this.synchronousUserClient = synchronousUserClient;
    }

    public User getUserByUniqueField(String user_id){
        try {
            return this.synchronousUserClient.getUserByUniqueField(GetUserByUniqueFieldRequest.newBuilder().setFilterBy(GetUserUniqueFields.ID).setValue(user_id).build());
        }catch (Exception e){
            throw new UpstreamlServiceException(e.getMessage());
//            return User.newBuilder().build();
        }
    }
}
