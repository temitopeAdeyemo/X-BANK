package com.xbank.walletservice.modules.transaction.repository;

import com.xbank.walletservice.modules.transaction.entity.Transaction;
import org.springframework.data.repository.Repository;

import java.util.UUID;

public interface TransactionRepository extends Repository<Transaction, UUID> {
}
