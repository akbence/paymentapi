package hu.payment.paymentapi.repository;

import hu.payment.paymentapi.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends CrudRepository<Transaction, UUID> {


    Optional<Transaction> findByincomingTransactionalId(String incomingTransactionalId);
}
