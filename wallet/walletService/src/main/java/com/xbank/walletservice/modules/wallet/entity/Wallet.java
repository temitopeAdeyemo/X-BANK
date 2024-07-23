package com.xbank.walletservice.modules.wallet.entity;

import com.xbank.walletservice.modules.transaction.entity.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "xbank_wallet", indexes = {
        @Index(name = "idx_wallet_id", columnList = "id"),
        @Index(name = "idx_account_number", columnList = "account_number"),
        @Index(name = "idx_wallet_created_at", columnList = "created_at")
})
public class Wallet {
    @Id
    @Column(name = "id")
    @Nullable
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "account_number", unique = true)
    private String accountNumber;

    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "wallet_type")
    private String walletType = String.valueOf(1);

    @NotNull
    @Column(name = "user_id", unique = true)
    private UUID userId;

//    @OneToMany(mappedBy = "wallet", orphanRemoval = true, cascade = {CascadeType.ALL, /*CascadeType.DETACH,*/ CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
//    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "sender_wallet", orphanRemoval = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH /*, CascadeType.DETACH*/}, fetch = FetchType.LAZY)
    private Set<Transaction> debit_transactions = new HashSet<>();

    @OneToMany(mappedBy = "receiver_wallet", orphanRemoval = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH /*, CascadeType.DETACH*/}, fetch = FetchType.LAZY)
    private Set<Transaction> credit_transactions = new HashSet<>();


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}