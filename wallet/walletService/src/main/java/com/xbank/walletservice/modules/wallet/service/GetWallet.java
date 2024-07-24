package com.xbank.walletservice.modules.wallet.service;

import com.xbank.walletservice.modules.wallet.entity.Wallet;
import com.xbank.walletservice.modules.wallet.repository.WalletRepository;
import com.xbank.walletservice.shared.mapper.WalletDataMapper;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.*;
import proto.service.proto.GetWalletServiceGrpc;
import proto.wallet.proto.GetSingleWalletRequest;
import proto.wallet.proto.GetWalletsRequest;
import proto.wallet.proto.GetWalletsResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@GrpcService
@RequiredArgsConstructor
public class GetWallet extends GetWalletServiceGrpc.GetWalletServiceImplBase {
    private final WalletRepository walletRepository;

    @Override
    public void getSingleWallet(GetSingleWalletRequest request, StreamObserver<proto.wallet.proto.Wallet> responseObserver) {
        String uniqueData = request.getAccountNumber().isEmpty()?request.getId():request.getAccountNumber();

        var wallet = this.walletRepository.getByAccountNumberOrId(uniqueData).orElseThrow(()->new RuntimeException("Error")); // Implement Wallet Not Found exception

        var walletBuild = WalletDataMapper.mapWalletToProtobuf(wallet);

        responseObserver.onNext(walletBuild);
        responseObserver.onCompleted();
    }

    @Override
    public void getWallets(GetWalletsRequest request, StreamObserver<GetWalletsResponse> responseObserver) {
        int page = request.getPage() < 1? 0 : request.getPage();
        int size = request.getSize() < 1? 5 : request.getSize();

        PageRequest pageData = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var wallet = new Wallet();

        wallet.setWalletType(request.getFilter().getWalletType());
        wallet.setUserId(UUID.fromString(request.getFilter().getUserId()));

        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnorePaths("id")
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

        Example<Wallet> walletEx = Example.of(wallet, exampleMatcher);

        var wallets = this.walletRepository.findAll(walletEx, pageData);

        List<proto.wallet.proto.Wallet> walletList = new ArrayList<>();

        for(Wallet data : wallets){
            walletList.add(WalletDataMapper.mapWalletToProtobuf(data));
        }

        var walletListBuild = GetWalletsResponse.newBuilder().addAllWallets(walletList).build();

        responseObserver.onNext(walletListBuild);
        responseObserver.onCompleted();
    }
}
