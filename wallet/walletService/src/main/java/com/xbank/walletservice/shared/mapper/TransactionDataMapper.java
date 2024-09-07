package com.xbank.walletservice.shared.mapper;


import com.xbank.walletservice.modules.transaction.entity.Transaction;
import com.xbank.walletservice.modules.transaction.entity.TransactionStatus;
import com.xbank.walletservice.modules.wallet.entity.Wallet;
import com.xbank.walletservice.shared.utils.ReferenceGenerator;
import org.jetbrains.annotations.NotNull;
//import proto.transaction.proto.TransactionStatus;
import proto.transaction.proto.TransactionType;

import java.math.BigDecimal;

public class TransactionDataMapper {
    public static proto.transaction.proto.Transaction mapTransactionToProtobuf(Transaction transaction){
        var trxStatus = transaction.getStatus().ordinal() == 0 ?
                proto.transaction.proto.TransactionStatus.APPROVED:  transaction.getStatus().ordinal() == 1 ?
                proto.transaction.proto.TransactionStatus.DECLINED : proto.transaction.proto.TransactionStatus.PENDING;

        var trxType = transaction.getTransactionType().ordinal() == 0? TransactionType.FUND_TRANSFER : TransactionType.FUNDING;

        return proto.transaction.proto.Transaction.newBuilder()
                .setAmount(String.valueOf(transaction.getAmount()))
                .setSourceCode(transaction.getSourceCode())
                .setTrfRef(transaction.getTrfRef())
                .setTransactionType(trxType)
                .setReceiverAccountNumber(transaction.getReceiverAccountNumber())
                .setSenderAccountNumber(transaction.getSenderAccountNumber())
                .setCreatedAt(String.valueOf(transaction.getCreatedAt()))
                .setUpdatedAt(String.valueOf(transaction.getCreatedAt()))
                .setId(String.valueOf(transaction.getId()))
                .setStatus(trxStatus)
                .setStatusDescription(transaction.getStatusDescription())
                .setSenderName(transaction.getSender_name() == null?"NA": transaction.getSender_name())
                .setReceiverName(transaction.getReceiver_name() == null?"NA": transaction.getReceiver_name())
                .build();
    }

    @NotNull
    public static Transaction mapProtoToTransactionData(
            com.xbank.walletservice.modules.transaction.entity.TransactionType transactionType,
            String sourceCode,
            Long amount,
            Wallet receiverWallet,
            Wallet senderWallet,
            String statusDescription,
            com.xbank.walletservice.modules.transaction.entity.TransactionStatus status
            ) {
        var newTransactionData = new Transaction();

        newTransactionData.setTransactionType(transactionType);
        newTransactionData.setSourceCode(sourceCode);
        newTransactionData.setAmount(BigDecimal.valueOf(amount));
        newTransactionData.setReceiverWallet(receiverWallet);
        newTransactionData.setReceiverAccountNumber(receiverWallet.getAccountNumber());
        newTransactionData.setStatus(status);
        newTransactionData.setTrfRef(ReferenceGenerator.generateUniqueReference());
        newTransactionData.setStatusDescription(statusDescription);
        newTransactionData.setSenderAccountNumber(senderWallet.getAccountNumber());
        newTransactionData.setSenderWallet(senderWallet);

        return newTransactionData;
    }
}
