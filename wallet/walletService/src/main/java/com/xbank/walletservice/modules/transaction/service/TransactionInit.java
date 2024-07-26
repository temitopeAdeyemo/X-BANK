package com.xbank.walletservice.modules.transaction.service;

import com.xbank.walletservice.modules.transaction.entity.Transaction;
import com.xbank.walletservice.modules.transaction.entity.TransactionType;
import com.xbank.walletservice.modules.transaction.repository.TransactionRepository;
import com.xbank.walletservice.modules.wallet.entity.Wallet;
import com.xbank.walletservice.modules.wallet.repository.WalletRepository;
import com.xbank.walletservice.shared.exception.EntityNotFoundException;
import com.xbank.walletservice.shared.mapper.TransactionDataMapper;
import com.xbank.walletservice.shared.utils.ReferenceGenerator;
import com.xbank.walletservice.shared.utils.ResponseHandler;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jetbrains.annotations.NotNull;
import proto.service.proto.TransactionInitServiceGrpc;
import proto.transaction.proto.*;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@GrpcService
public class TransactionInit extends TransactionInitServiceGrpc.TransactionInitServiceImplBase {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    @Override
    public void fundTransfer(FundTransferRequest request, StreamObserver<FundTransferResponse> responseObserver) {
        // track session with user id for fund transfer. If user id session is still available during fund transfer, decline transaction
    }

    @Override
    @Transactional
    public void fundWallet(FundWalletRequest request, StreamObserver<StatusResponse> responseObserver) {
        // track session with user id for fund Wallet. If user id session is still available during fund Wallet, decline transaction
        var wallet = this.walletRepository.findByUserId(UUID.fromString(request.getUserId())).orElseThrow(()-> new EntityNotFoundException("Wallet Not Found."));

        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(Long.parseLong(request.getAmount()))));
        
        walletRepository.save(wallet);

        var newTransactionData = TransactionDataMapper.mapProtoToTransactionData(request, wallet);

        this.transactionRepository.save(newTransactionData);

        TransactionStatus buildStatus = TransactionStatus.forNumber(newTransactionData.getStatus().ordinal());

        String buildTrfRef = newTransactionData.getTrfRef();

        var buildResponse = StatusResponse.newBuilder().setStatus(buildStatus).setTrfRef(buildTrfRef).build();

        new ResponseHandler<StatusResponse>().respond(responseObserver, buildResponse );
    }
}
