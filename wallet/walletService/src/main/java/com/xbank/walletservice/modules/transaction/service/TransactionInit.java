package com.xbank.walletservice.modules.transaction.service;

import com.xbank.walletservice.modules.transaction.entity.Transaction;
import com.xbank.walletservice.modules.transaction.entity.TransactionType;
import com.xbank.walletservice.modules.transaction.repository.TransactionRepository;
import com.xbank.walletservice.modules.wallet.entity.Wallet;
import com.xbank.walletservice.modules.wallet.repository.WalletRepository;
import com.xbank.walletservice.shared.exception.EntityNotFoundException;
import com.xbank.walletservice.shared.exception.InsufficientBalanceException;
import com.xbank.walletservice.shared.exception.TransactionDeclinedException;
import com.xbank.walletservice.shared.mapper.TransactionDataMapper;
import com.xbank.walletservice.shared.utils.ResponseHandler;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
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
    @Transactional(dontRollbackOn = {InsufficientBalanceException.class, TransactionDeclinedException.class})
    public void fundTransfer(FundTransferRequest request, StreamObserver<FundDepositResponse> responseObserver) {
        String processId = UUID.randomUUID().toString();

        // Save a session in memory / redis here to show tht start of a process with process id and request.getSenderId()

        Wallet senderWallet = this.walletRepository.findByUserIdForLock(UUID.fromString(request.getSenderId())).orElseThrow(()-> new EntityNotFoundException("Sender Wallet Not Found."));

        Wallet receiverWallet = this.walletRepository.getByAccountNumberOrIdForLock(request.getReceiverAccountNumber()).orElseThrow(()-> new EntityNotFoundException("Receiver Wallet Not Found."));

        if(senderWallet.getBalance().compareTo(BigDecimal.ZERO) <= 0 || senderWallet.getBalance().compareTo(BigDecimal.valueOf(request.getAmount()))< 0) {
            var newTransactionData = TransactionDataMapper.mapProtoToTransactionData(
                    TransactionType.FUND_TRANSFER,
                    request.getSourceCode(),
                    request.getAmount(),
                    receiverWallet,
                    senderWallet,
                    "Insufficient Balance.",
                    com.xbank.walletservice.modules.transaction.entity.TransactionStatus.DECLINED
            );

            this.transactionRepository.save(newTransactionData);

            throw new InsufficientBalanceException("Insufficient Balance.");
        }

        if(request.getAmount() <= 0){
            var newTransactionData = TransactionDataMapper.mapProtoToTransactionData(
                    TransactionType.FUND_TRANSFER,
                    request.getSourceCode(),
                    request.getAmount(),
                    receiverWallet,
                    senderWallet,
                    "Invalid Amount.",
                    com.xbank.walletservice.modules.transaction.entity.TransactionStatus.DECLINED
                    );

            this.transactionRepository.save(newTransactionData);

            throw new TransactionDeclinedException("Amount not allowed.");
        }

        if(senderWallet.getAccountNumber().equals(request.getReceiverAccountNumber())){
            Transaction newTransactionData = TransactionDataMapper.mapProtoToTransactionData(
                    TransactionType.FUND_TRANSFER,
                    request.getSourceCode(),
                    request.getAmount(),
                    receiverWallet,
                    senderWallet,
                    "Declined due to Self Transfer Attempt.",
                    com.xbank.walletservice.modules.transaction.entity.TransactionStatus.DECLINED
            );

            this.transactionRepository.save(newTransactionData);

            throw new TransactionDeclinedException("Cannot transfer to Self.");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(BigDecimal.valueOf(request.getAmount())));

        receiverWallet.setBalance(receiverWallet.getBalance().add(BigDecimal.valueOf(request.getAmount())));

        // Do a session Check here to see if the saved process id and request.getSenderId() in the opened session is the current process id and request.getSenderId() in the running process
        // If false, then a new session has overridden the current process. Throw error.

        walletRepository.save(receiverWallet);

        walletRepository.save(senderWallet);

        Transaction newTransactionData = TransactionDataMapper.mapProtoToTransactionData(
                TransactionType.FUND_TRANSFER,
                request.getSourceCode(),
                request.getAmount(),
                receiverWallet,
                senderWallet,
                "Approved.",
                com.xbank.walletservice.modules.transaction.entity.TransactionStatus.APPROVED
        );


        saveAndRespondToClient(responseObserver, newTransactionData);
    }

    @Override
    @Transactional(dontRollbackOn = TransactionDeclinedException.class)
    public void fundWallet(FundWalletRequest request, StreamObserver<FundDepositResponse> responseObserver) {
        var wallet = this.walletRepository.findByUserIdForLock(UUID.fromString(request.getUserId())).orElseThrow(()-> new EntityNotFoundException("Wallet Not Found."));

        if(request.getAmount() <= 0){
            Transaction newTransactionData = TransactionDataMapper.mapProtoToTransactionData(
                    TransactionType.FUNDING,
                    "000",
                    request.getAmount(),
                    wallet,
                    wallet,
                    "Invalid Amount.",
                    com.xbank.walletservice.modules.transaction.entity.TransactionStatus.DECLINED
            );

            this.transactionRepository.save(newTransactionData);

            throw new TransactionDeclinedException("Amount not allowed.");
        }

        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(request.getAmount())));

        walletRepository.save(wallet);

        Transaction newTransactionData = TransactionDataMapper.mapProtoToTransactionData(
                TransactionType.FUNDING,
                "000",
                request.getAmount(),
                wallet,
                wallet,
                "Approved.",
                com.xbank.walletservice.modules.transaction.entity.TransactionStatus.APPROVED
        );

        saveAndRespondToClient(responseObserver, newTransactionData);
    }

    private void saveAndRespondToClient(StreamObserver<FundDepositResponse> responseObserver, Transaction newTransactionData) {
        this.transactionRepository.save(newTransactionData);

        String buildTrfRef = newTransactionData.getTrfRef();

        FundDepositResponse buildResponse = FundDepositResponse.newBuilder().setStatus(TransactionStatus.APPROVED).setTrfRef(buildTrfRef).build();

        new ResponseHandler<FundDepositResponse>().sendSync(responseObserver, buildResponse );
    }
}
