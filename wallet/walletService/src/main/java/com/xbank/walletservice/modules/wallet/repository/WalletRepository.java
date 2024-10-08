package com.xbank.walletservice.modules.wallet.repository;

import com.xbank.walletservice.modules.wallet.entity.Wallet;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Query("SELECT wallet FROM Wallet wallet WHERE wallet.accountNumber = :uniqueData OR cast(wallet.id as string) = :uniqueData")
    Optional<Wallet> getByAccountNumberOrId(String uniqueData);

    Optional<Wallet> findByUserId(@NotNull UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT wallet FROM Wallet wallet WHERE wallet.accountNumber = :uniqueData OR cast(wallet.id as string) = :uniqueData")
    Optional<Wallet> getByAccountNumberOrIdForLock(String uniqueData);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT wallet FROM Wallet wallet WHERE wallet.userId = :userId")
    Optional<Wallet> findByUserIdForLock(@NotNull UUID userId);
}
