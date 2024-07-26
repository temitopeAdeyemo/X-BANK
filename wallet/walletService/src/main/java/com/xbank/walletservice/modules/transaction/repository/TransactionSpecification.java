package com.xbank.walletservice.modules.transaction.repository;

import com.xbank.walletservice.modules.transaction.entity.Transaction;
import com.xbank.walletservice.modules.transaction.entity.TransactionStatus;
import com.xbank.walletservice.modules.transaction.entity.TransactionType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import proto.transaction.proto.GetAllTransactionsFilter;

public class TransactionSpecification {
    public static Specification<Transaction> createSpecification(GetAllTransactionsFilter filter){

        return Specification.where(hasAccountNumber(filter.getAccountNumber()))
                .and(hasSourceCode(filter.getSourceCode()))
                .and(hasSenderName(filter.getSenderName()))
                .and(filter.hasAll()? setBothTransType(): hasTransactionType(filter.getSpecificType().toString()) )
                .and(filter.hasAllStatus()? setAllStatus(): hasTransactionStatus(filter.getStatus().toString()) );
    }

    private static Specification<Transaction> hasSourceCode(String sourceCode) {
        return (root, criteriaQuery, criteriaBuilder)->{
            if(sourceCode.isEmpty()) return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(root.get("source_code"), sourceCode);
        };
    }

    private static Specification<Transaction> hasTransactionType(String transactionType) {
        return (root, criteriaQuery, criteriaBuilder)-> criteriaBuilder.equal(root.get("transaction_type"), transactionType);
    }

    private static Specification<Transaction> hasSenderName(String senderName) {
        return (root, criteriaQuery, criteriaBuilder)->{
            if(senderName.isEmpty()) return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(root.get("sender_name"), senderName);
        };
    }

    private static Specification<Transaction> hasAccountNumber(String accountNumber) {
        return (root, criteriaQuery, criteriaBuilder)->{
            if(accountNumber.isEmpty()) return criteriaBuilder.conjunction();

            Predicate receiverAccNum = criteriaBuilder.equal(root.get("receiver_account_number"), accountNumber);
            Predicate senderAccNum = criteriaBuilder.equal(root.get("sender_account_number"), accountNumber);

            return criteriaBuilder.or(receiverAccNum, senderAccNum);
        };
    }

    private static Specification<Transaction> setBothTransType() {
        return (root, criteriaQuery, criteriaBuilder)->{
            Predicate credit = criteriaBuilder.equal(root.get("transaction_type"), TransactionType.CREDIT);
            Predicate debit = criteriaBuilder.equal(root.get("transaction_type"), TransactionType.DEBIT);
            Predicate funding = criteriaBuilder.equal(root.get("transaction_type"), TransactionType.FUNDING);

            return criteriaBuilder.or(credit, debit, funding);
        };
    }

    private static Specification<Transaction> setAllStatus() {
        return (root, criteriaQuery, criteriaBuilder)->{
            Predicate approved = criteriaBuilder.equal(root.get("status"), TransactionStatus.APPROVED);
            Predicate declined = criteriaBuilder.equal(root.get("status"), TransactionStatus.DECLINED);
            Predicate pending = criteriaBuilder.equal(root.get("status"), TransactionStatus.PENDING);

            return criteriaBuilder.or(approved, declined, pending);
        };
    }

    private static Specification<Transaction> hasTransactionStatus(String transactionStatus) {
        return (root, criteriaQuery, criteriaBuilder)-> criteriaBuilder.equal(root.get("status"), transactionStatus);
    }
}
