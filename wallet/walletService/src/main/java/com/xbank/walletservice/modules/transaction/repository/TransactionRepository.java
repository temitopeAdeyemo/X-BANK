package com.xbank.walletservice.modules.transaction.repository;

import com.xbank.walletservice.modules.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("select transaction from Transaction transaction where cast(transaction.id as string ) = :uniqueData OR transaction.trfRef = :uniqueData")
    Optional<Transaction> findByReferenceOrId(String uniqueData);
}
