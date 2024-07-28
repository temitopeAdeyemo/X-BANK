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
                .and(filter.hasAll()? setBothTransType(): hasTransactionType(filter.getSpecificType().toString(), filter.getAccountNumber()) )
                .and(filter.getStatus().equals(proto.transaction.proto.TransactionStatus.ALL) ? setAllStatus(): hasTransactionStatus(filter.getStatus().toString()) );
    }

    private static Specification<Transaction> hasSourceCode(String sourceCode) {
        return (root, criteriaQuery, criteriaBuilder)->{
            if(sourceCode.isEmpty()) return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(root.get("source_code"), sourceCode);
        };
    }

    private static Specification<Transaction> hasTransactionType(String transactionType, String accountNumber) {
        return (root, criteriaQuery, criteriaBuilder)-> {
            if(transactionType.isEmpty()) return criteriaBuilder.conjunction();

            Predicate xx = criteriaBuilder.conjunction();

            if(transactionType.equals(String.valueOf(proto.transaction.proto.TransactionType.CREDIT)) && !accountNumber.isEmpty()) {
                xx = criteriaBuilder.equal(root.get("receiverAccountNumber"), accountNumber);
            }

            if(transactionType.equals(String.valueOf(proto.transaction.proto.TransactionType.DEBIT)) && !accountNumber.isEmpty()) {
                xx = criteriaBuilder.equal(root.get("senderAccountNumber"), accountNumber);
            }

            if(transactionType.equals(String.valueOf(proto.transaction.proto.TransactionType.FUND_TRANSFER)) && !accountNumber.isEmpty()) {
                var cc = criteriaBuilder.equal(root.get("senderAccountNumber"), accountNumber);
                var dd = criteriaBuilder.equal(root.get("receiverAccountNumber"), accountNumber);
                Predicate transfer = criteriaBuilder.equal(root.get("transactionType"), TransactionType.FUND_TRANSFER);

                var ff = criteriaBuilder.or(cc, dd);
                xx = criteriaBuilder.and(ff, transfer);
            }

            if(transactionType.equals(String.valueOf(proto.transaction.proto.TransactionType.FUNDING)) && !accountNumber.isEmpty()) {
                var dd = criteriaBuilder.equal(root.get("receiverAccountNumber"), accountNumber);
                Predicate transfer = criteriaBuilder.equal(root.get("transactionType"), TransactionType.FUNDING);

                xx = criteriaBuilder.and( dd, transfer);
            }

            if(transactionType.equals(String.valueOf(proto.transaction.proto.TransactionType.FUNDING)) && accountNumber.isEmpty()) {
                 xx = criteriaBuilder.equal(root.get("transactionType"), TransactionType.FUNDING);
            }

            if(transactionType.equals(String.valueOf(proto.transaction.proto.TransactionType.FUND_TRANSFER)) && accountNumber.isEmpty()) {
                xx = criteriaBuilder.equal(root.get("transactionType"), TransactionType.FUND_TRANSFER);
            }

            return xx;
        };
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

            Predicate receiverAccNum = criteriaBuilder.equal(root.get("receiverAccountNumber"), accountNumber);
            Predicate senderAccNum = criteriaBuilder.equal(root.get("senderAccountNumber"), accountNumber);

            return criteriaBuilder.or(receiverAccNum, senderAccNum);
        };
    }

    private static Specification<Transaction> setBothTransType() {
        return (root, criteriaQuery, criteriaBuilder)->{

            Predicate transfer = criteriaBuilder.equal(root.get("transactionType"), TransactionType.FUND_TRANSFER);
            Predicate funding = criteriaBuilder.equal(root.get("transactionType"), TransactionType.FUNDING);

            return criteriaBuilder.and(transfer, funding);
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
        return (root, criteriaQuery, criteriaBuilder)-> criteriaBuilder.equal(root.get("status"), TransactionStatus.valueOf(transactionStatus));
    }
}
