package com.xbank.walletservice.modules.wallet.service;

import com.xbank.walletservice.modules.wallet.entity.Wallet;
import com.xbank.walletservice.modules.wallet.repository.WalletRepository;
import com.xbank.walletservice.shared.exception.CredentialExistsException;
import com.xbank.walletservice.shared.utils.ResponseHandler;
import io.grpc.stub.StreamObserver;
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
        new ResponseHandler<SayHelloResponse>().sendSync(responseObserver, SayHelloResponse.newBuilder().setMessage("Hello World").build() );
    }

    @Override
    public void createWallet(CreateWalletRequest request, StreamObserver<CreateWalletResponse> responseObserver) {
        var userWallet = this.walletRepository.findByUserId(UUID.fromString(request.getUserId()));

        if(userWallet.isPresent()) throw new CredentialExistsException(("User wallet exists."));

        var walletData = new Wallet();

        walletData.setUserId(UUID.fromString(request.getUserId()));
        Wallet wallet = this.walletRepository.save(walletData);

        var responseBuild = CreateWalletResponse.newBuilder().setAccountNumber(wallet.getAccountNumber()).build();

        new ResponseHandler<CreateWalletResponse>().sendSync(responseObserver, responseBuild );
    }
}
