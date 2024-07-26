package com.xbank.walletservice.modules.transaction.entity;

import com.xbank.walletservice.modules.wallet.entity.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "xbank_transactions", indexes = {
        @Index(name = "idx_trans_id", columnList = "id"),
        @Index(name = "idx_receiver_account_number_transaction_type", columnList = "transactionType, receiver_account_number"),
        @Index(name = "idx_receiver_account_number", columnList = "receiver_account_number"),
        @Index(name = "idx_sender_account_number_transaction_type", columnList = "transactionType, sender_account_number"),
        @Index(name = "idx_sender_account_number", columnList = "sender_account_number"),
        @Index(name = "idx_transaction_created_at", columnList = "created_at")
})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Nullable
    UUID id = UUID.randomUUID();

    @Nonnegative
    private BigDecimal amount;

    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "receiver_account_number")
    private String receiverAccountNumber;

    @Column(name = "sender_account_number")
    private String senderAccountNumber;

    @Column(name = "source_code")
    private String sourceCode;

    @Column(name = "trf_ref", length = 24)
    private String trfRef;

    private TransactionStatus status;

//    @ManyToOne
//    @JoinColumn(name = "wallet")
//    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "sender_wallet")
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet")
    private Wallet receiverWallet;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}

