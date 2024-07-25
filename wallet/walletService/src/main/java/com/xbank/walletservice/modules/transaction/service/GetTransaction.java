package com.xbank.walletservice.modules.transaction.service;

import com.xbank.walletservice.modules.transaction.repository.TransactionRepository;
import com.xbank.walletservice.modules.transaction.repository.TransactionSpecification;
import com.xbank.walletservice.shared.exception.EntityNotFoundException;
import com.xbank.walletservice.shared.mapper.TransactionDataMapper;
import com.xbank.walletservice.shared.utils.ResponseHandler;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.*;
import proto.service.proto.GetTransactionServiceGrpc;
import proto.transaction.proto.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@GrpcService
public class GetTransaction extends GetTransactionServiceGrpc.GetTransactionServiceImplBase {
    private final TransactionRepository transactionRepository;

    @Override
    public void getSingleTransaction(GetSingleTransactionRequest request, StreamObserver<Transaction> responseObserver) {
        String uniqueData = request.getId().isEmpty()? request.getTrfRef(): request.getId();

        var transaction = this.transactionRepository.findByReferenceOrId(uniqueData).orElseThrow(()->new EntityNotFoundException("Transaction not found."));

        var responseBuild = TransactionDataMapper.mapTransactionToProtobuf(transaction);

        new ResponseHandler<Transaction>().respond(responseObserver, responseBuild);
    }

    @Override
    public void getAllTransactions(GetAllTransactionsRequest request, StreamObserver<GetAllTransactionsResponse> responseObserver) {
        int page = request.getPage() < 1? 0 : request.getPage();
        int size = request.getSize() < 1? 5 : request.getSize();
        PageRequest pageData = PageRequest.of(page, size, Sort.by("created_at").descending());

        var filter = request.getFilterBy();
        var transaction = new com.xbank.walletservice.modules.transaction.entity.Transaction();

        transaction.setTransactionType(com.xbank.walletservice.modules.transaction.entity.TransactionType.CREDIT);

        if(filter.hasSpecificType()){
            transaction.setTransactionType(com.xbank.walletservice.modules.transaction.entity.TransactionType.valueOf(filter.getSpecificType().toString()));
        }

        transaction.setSourceCode(filter.getSourceCode());
//        transaction.setReceiverAccountNumber(filter.getAccountNumber());

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("id")
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

        Example<com.xbank.walletservice.modules.transaction.entity.Transaction> transactionExample = Example.of(transaction, exampleMatcher);
        var transactions = transactionRepository.findAll(transactionExample, pageData);

        List<Transaction> transactionList = new ArrayList<>();

        for(var trans: transactions) {
            Transaction transactionBuild = TransactionDataMapper.mapTransactionToProtobuf(trans);
            transactionList.add(transactionBuild);
        }

        var responseBuild = GetAllTransactionsResponse.newBuilder().addAllTransactions(transactionList).build();

        new ResponseHandler<GetAllTransactionsResponse>().respond(responseObserver, responseBuild);
    }

    public void getAllTransactions1(GetAllTransactionsRequest request, StreamObserver<GetAllTransactionsResponse> responseObserver) {
        int page = request.getPage() < 1? 0 : request.getPage();
        int size = request.getSize() < 1? 5 : request.getSize();
        PageRequest pageData = PageRequest.of(page, size, Sort.by("created_at").descending());

        var filter = request.getFilterBy();

        var transactionSpec = TransactionSpecification.createSpecification(filter);

        var transactions = transactionRepository.findAll(transactionSpec, pageData);

        List<Transaction> transactionList = new ArrayList<>();

        for(var trans: transactions) {
            Transaction transactionBuild = TransactionDataMapper.mapTransactionToProtobuf(trans);
            transactionList.add(transactionBuild);
        }

        var responseBuild = GetAllTransactionsResponse.newBuilder().addAllTransactions(transactionList).build();

        new ResponseHandler<GetAllTransactionsResponse>().respond(responseObserver, responseBuild);
    }
}
