package com.xbank.walletservice.modules.transaction.repository;

import com.xbank.walletservice.modules.transaction.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import proto.transaction.proto.GetAllTransactionsFilter;
import proto.transaction.proto.GetAllTransactionsRequest;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public CriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Transaction> findByFilter(GetAllTransactionsRequest transactionRequestData){
        GetAllTransactionsFilter transactionFilter = transactionRequestData.getFilterBy();

        CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);

        Root<Transaction> transactionRoot = criteriaQuery.from(Transaction.class);

        Predicate predicate = getPredicate(transactionFilter, transactionRoot);

        criteriaQuery.where(predicate);

        setOrder( criteriaQuery, transactionRoot);

        TypedQuery<Transaction> typedQuery = entityManager.createQuery(criteriaQuery);

        int page = transactionRequestData.getPage() < 1? 0 : transactionRequestData.getPage();
        int size = transactionRequestData.getSize() < 1? 5 : transactionRequestData.getSize();

        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        var pageable = getPageable(page, size);

        Long transactionCount = getCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, transactionCount);
    }

    public Predicate  getPredicate(GetAllTransactionsFilter transactionFilter, Root<Transaction> transactionRoot){
        List<Predicate> predicates = new ArrayList<>();

        if(!transactionFilter.getSenderName().isEmpty()){
            predicates.add(criteriaBuilder.like(transactionRoot.get("sender_name"), "%" + transactionFilter.getSenderName() + "%"));
        }

        if(!transactionFilter.getAccountNumber().isEmpty()){
            var receiverAccountNumPredicate = criteriaBuilder.equal(transactionRoot.get("receiver_account_number"), transactionFilter.getAccountNumber());
            var senderAccountNumPredicate = criteriaBuilder.equal(transactionRoot.get("sender_account_number"), transactionFilter.getAccountNumber());
            predicates.add(criteriaBuilder. or(receiverAccountNumPredicate, senderAccountNumPredicate));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(CriteriaQuery<Transaction> criteriaQuery, Root<Transaction> transactionRoot) {
        criteriaQuery.orderBy(criteriaBuilder.desc(transactionRoot.get("created_at")));
    }

    private Pageable getPageable( int page, int size){
        return  PageRequest.of(page, size, Sort.by("created_at").descending());
    }

    private Long getCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root<Transaction> countRoot = countQuery.from(Transaction.class);

        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);

        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
