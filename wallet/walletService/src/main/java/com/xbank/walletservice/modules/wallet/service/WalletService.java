package com.xbank.walletservice.modules.wallet.service;

import com.xbank.walletservice.modules.wallet.entity.Wallet;
import com.xbank.walletservice.modules.wallet.repository.WalletRepository;
import com.xbank.walletservice.shared.utils.AccountNumberGenerator;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.service.proto.WalletServiceGrpc;
import proto.wallet.proto.CreateWalletRequest;
import proto.wallet.proto.CreateWalletResponse;
import proto.wallet.proto.SayHelloResponse;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class WalletService extends WalletServiceGrpc.WalletServiceImplBase {
    private final WalletRepository walletRepository;
    @Override
    public void sayHelloService(proto.wallet.proto.SayHello request, StreamObserver<SayHelloResponse> responseObserver) {
        responseObserver.onNext(SayHelloResponse.newBuilder().setMessage("Hello World").build());
        responseObserver.onCompleted();
    }

    @Override
    public void createWallet(CreateWalletRequest request, StreamObserver<CreateWalletResponse> responseObserver) {

        var walletData = new Wallet();
        walletData.setUserId(UUID.fromString(request.getUserId()));
        Wallet wallet = this.walletRepository.save(walletData);

        var responseBuild = CreateWalletResponse.newBuilder().setAccountNumber(wallet.getAccountNumber()).build();

        responseObserver.onNext(responseBuild);
        responseObserver.onCompleted();
    }
}
