package com.xbank.walletservice.modules.wallet.repository;

import com.xbank.walletservice.modules.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Query("select wallet from Wallet wallet where wallet.id = :uniqueData OR cast(wallet.id as string ) = :uniqueData")
    Wallet getByAccountNumberOrId(String uniqueData);
}
