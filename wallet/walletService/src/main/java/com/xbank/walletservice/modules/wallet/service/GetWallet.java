package com.xbank.walletservice.modules.wallet.service;

import com.xbank.walletservice.modules.wallet.entity.Wallet;
import com.xbank.walletservice.modules.wallet.repository.WalletRepository;
import com.xbank.walletservice.shared.exception.EntityNotFoundException;
import com.xbank.walletservice.shared.mapper.WalletDataMapper;
import com.xbank.walletservice.shared.service.xbank.UserClient;
import com.xbank.walletservice.shared.utils.ResponseHandler;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.*;
import proto.getUser.proto.User;
import proto.service.proto.GetWalletServiceGrpc;
import proto.wallet.proto.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class GetWallet extends GetWalletServiceGrpc.GetWalletServiceImplBase {
    private final WalletRepository walletRepository;
    private final UserClient userClient;

    @Override
    @Transactional
    public void getSingleWallet(GetSingleWalletRequest request, StreamObserver<proto.wallet.proto.Wallet> responseObserver) {
        String uniqueData = request.getAccountNumber().isEmpty()?request.getId():request.getAccountNumber();

        Wallet wallet = this.walletRepository.getByAccountNumberOrId(uniqueData).orElseThrow(()->new EntityNotFoundException("Wallet not found."));

        User user =  this.userClient.getUserByUniqueField(wallet.getUserId().toString());

         proto.wallet.proto.Wallet walletBuild = WalletDataMapper.mapWalletToProtobuf(wallet, user);

        new ResponseHandler<proto.wallet.proto.Wallet>().sendSync(responseObserver,walletBuild);
    }

    @Override
    @Transactional
    public void getWallets(GetWalletsRequest request, StreamObserver<GetWalletsResponse> responseObserver) {
        int page = request.getPage() < 1? 0 : request.getPage();
        int size = request.getSize() < 1? 5 : request.getSize();

        PageRequest pageData = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Wallet wallet = new Wallet();

        wallet.setWalletType(request.getFilter().getWalletType());

        if(!request.getFilter().getUserId().isEmpty())wallet.setUserId(UUID.fromString(request.getFilter().getUserId()));

        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnorePaths("id", "accountNumber", "balance")
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

        Example<Wallet> walletEx = Example.of(wallet, exampleMatcher);


        Page<Wallet> wallets = this.walletRepository.findAll(walletEx, pageData);

        List<proto.wallet.proto.Wallet> walletList = new ArrayList<>();

        for(Wallet data : wallets){
            walletList.add(WalletDataMapper.mapWalletToProtobuf(data));
        }

        var walletListBuild = GetWalletsResponse.newBuilder().addAllWallets(walletList).build();

        new ResponseHandler<GetWalletsResponse>().sendSync(responseObserver, walletListBuild);
    }

    @Override
    public void getBalance(GetBalanceRequest request, StreamObserver<GetBalanceResponse> responseObserver) {
        var data = request.getAccountNumber().isEmpty()?request.getId() : request.getAccountNumber();

        var wallet = this.walletRepository.getByAccountNumberOrId(data).orElseThrow(()->new EntityNotFoundException("Wallet not found."));

        var responseBuild = GetBalanceResponse.newBuilder().setBalance(String.valueOf(wallet.getBalance())).build();

        new ResponseHandler<GetBalanceResponse>().sendSync(responseObserver, responseBuild);
    }
}
