package com.xbank.walletservice.shared.mapper;


import com.xbank.walletservice.modules.transaction.entity.Transaction;
import com.xbank.walletservice.modules.wallet.entity.Wallet;
import com.xbank.walletservice.shared.utils.ReferenceGenerator;
import org.jetbrains.annotations.NotNull;
import proto.transaction.proto.FundWalletRequest;
import proto.transaction.proto.TransactionStatus;
import proto.transaction.proto.TransactionType;

import java.math.BigDecimal;

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

    @NotNull
    public static Transaction mapProtoToTransactionData(FundWalletRequest request, Wallet wallet) {
        var newTransactionData = new Transaction();

        newTransactionData.setTransactionType(com.xbank.walletservice.modules.transaction.entity.TransactionType.CREDIT);
        newTransactionData.setSourceCode("000");
        newTransactionData.setTransactionType(com.xbank.walletservice.modules.transaction.entity.TransactionType.FUNDING);
        newTransactionData.setAmount(BigDecimal.valueOf(Long.parseLong(request.getAmount())));
        newTransactionData.setReceiverWallet(wallet);
        newTransactionData.setReceiverAccountNumber(wallet.getAccountNumber());
        newTransactionData.setStatus(com.xbank.walletservice.modules.transaction.entity.TransactionStatus.APPROVED);
        newTransactionData.setTrfRef(ReferenceGenerator.generateUniqueReference());
        newTransactionData.setSenderName("SELF");

        return newTransactionData;
    }
}
