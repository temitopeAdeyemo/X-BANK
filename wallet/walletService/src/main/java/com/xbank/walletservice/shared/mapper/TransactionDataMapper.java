package com.xbank.walletservice.shared.mapper;


import com.xbank.walletservice.modules.transaction.entity.Transaction;
import proto.transaction.proto.TransactionStatus;
import proto.transaction.proto.TransactionType;

public class TransactionDataMapper {
    public static proto.transaction.proto.Transaction mapTransactionToProtobuf(Transaction transaction){

        return proto.transaction.proto.Transaction.newBuilder().setAmount(String.valueOf(transaction.getAmount()))
                .setSenderName(String.valueOf(transaction.getSenderName()))
                .setSourceCode(transaction.getSourceCode())
                .setTrfRef(transaction.getTrfRef())
                .setTransactionType(TransactionType.forNumber(transaction.getTransactionType().ordinal()))
                .setReceiverAccountNumber(transaction.getReceiverAccountNumber())
                .setSenderAccountNumber(transaction.getSenderAccountNumber())
                .setCreatedAt(String.valueOf(transaction.getCreatedAt()))
                .setUpdatedAt(String.valueOf(transaction.getCreatedAt()))
                .setId(String.valueOf(transaction.getId()))
                .setStatus(TransactionStatus.forNumber(transaction.getStatus().ordinal()))
                .build();
    }
}
