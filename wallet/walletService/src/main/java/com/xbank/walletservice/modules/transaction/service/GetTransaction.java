package com.xbank.walletservice.modules.transaction.service;

import com.xbank.walletservice.modules.transaction.repository.TransactionRepository;
import com.xbank.walletservice.modules.transaction.repository.TransactionSpecification;
import com.xbank.walletservice.shared.exception.EntityNotFoundException;
import com.xbank.walletservice.shared.mapper.TransactionDataMapper;
import com.xbank.walletservice.shared.service.xbank.UserClient;
import com.xbank.walletservice.shared.utils.ResponseHandler;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.*;
import proto.service.proto.GetTransactionServiceGrpc;
import proto.transaction.proto.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@GrpcService
public class GetTransaction extends GetTransactionServiceGrpc.GetTransactionServiceImplBase {
    private final TransactionRepository transactionRepository;
    private final UserClient userClient;

    @Override
    @Transactional
    public void getSingleTransaction(GetSingleTransactionRequest request, StreamObserver<Transaction> responseObserver) {
        String uniqueData = request.getId().isEmpty()? request.getTrfRef(): request.getId();

        var transaction = this.transactionRepository.findByReferenceOrId(uniqueData).orElseThrow(()->new EntityNotFoundException("Transaction not found."));

        var senderUserDetails = this.userClient.getUserByUniqueField(transaction.getSenderWallet().getUserId().toString());
        var receiverUserDetails = this.userClient.getUserByUniqueField(transaction.getReceiverWallet().getUserId().toString());

        transaction.setSender_name(senderUserDetails.getFirstName()+ " " + senderUserDetails.getLastName());
        transaction.setReceiver_name(receiverUserDetails.getFirstName()+ " " + receiverUserDetails.getLastName());

        var responseBuild = TransactionDataMapper.mapTransactionToProtobuf(transaction);

        new ResponseHandler<Transaction>().sendSync(responseObserver, responseBuild);
    }

    @Override
    @Transactional
    public void getAllTransactions(GetAllTransactionsRequest request, StreamObserver<GetAllTransactionsResponse> responseObserver) {
        int page = request.getPage() < 1? 0 : request.getPage();
        int size = request.getSize() < 1? 5 : request.getSize();
        PageRequest pageData = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var filter = request.getFilterBy();
        var transactionSpec = TransactionSpecification.createSpecification(filter);

        var transactions = transactionRepository.findAll(transactionSpec, pageData);

        List<Transaction> transactionList = new ArrayList<>();

        for(var trans: transactions) {
            Transaction transactionBuild = TransactionDataMapper.mapTransactionToProtobuf(trans);
            transactionList.add(transactionBuild);
        }

        var responseBuild = GetAllTransactionsResponse.newBuilder().addAllTransactions(transactionList).build();

        new ResponseHandler<GetAllTransactionsResponse>().sendSync(responseObserver, responseBuild);
    }
}
