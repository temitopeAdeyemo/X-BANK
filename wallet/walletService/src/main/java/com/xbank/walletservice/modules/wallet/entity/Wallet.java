package com.xbank.walletservice.modules.wallet.entity;

import com.xbank.walletservice.modules.transaction.entity.Transaction;
import com.xbank.walletservice.shared.utils.AccountNumberGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private String accountNumber = AccountNumberGenerator.generate();

    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "wallet_type")
    private String walletType = String.valueOf(1);

    @NotNull
    @Column(name = "user_id", unique = true)
    private UUID userId;

    @OneToMany(mappedBy = "senderWallet", orphanRemoval = true, cascade = { CascadeType.ALL /*CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH*/ /*, CascadeType.DETACH*/}, fetch = FetchType.LAZY)
    private Set<Transaction> debitTransactions = new HashSet<>();

    @OneToMany(mappedBy = "receiverWallet", orphanRemoval = true, cascade = { CascadeType.ALL/*CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH*/ /*, CascadeType.DETACH*/}, fetch = FetchType.LAZY)
    private Set<Transaction> creditTransactions = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date updatedAt;
}