package com.xbank.walletservice.modules.wallet.service;

import com.xbank.walletservice.modules.wallet.entity.Wallet;
import com.xbank.walletservice.modules.wallet.repository.WalletRepository;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import proto.service.proto.DeleteWalletServiceGrpc;
import proto.user.proto.Empty;
import proto.wallet.proto.DeleteWalletRequest;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
public class DeleteWallet extends DeleteWalletServiceGrpc.DeleteWalletServiceImplBase {
    private final WalletRepository walletRepository;
    @Override
    public void deleteWallet(DeleteWalletRequest request, StreamObserver<Empty> responseObserver) {
        this.walletRepository.deleteById(UUID.fromString(request.getId()));

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}