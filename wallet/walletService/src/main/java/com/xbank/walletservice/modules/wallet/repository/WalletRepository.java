package com.xbank.walletservice.modules.wallet.repository;

import com.xbank.walletservice.modules.wallet.entity.Wallet;
import org.springframework.data.repository.Repository;

import java.util.UUID;

public interface WalletRepository extends Repository<Wallet, UUID> {

}
