package hu.payment.paymentapi.repository;

import hu.payment.paymentapi.model.Currency;
import hu.payment.paymentapi.model.MoneyAccount;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface MoneyRepository extends CrudRepository<MoneyAccount, UUID> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Transactional
    @Query("SELECT ma FROM MoneyAccount ma WHERE ma.userAccount.id = :userId AND ma.currency = :currency")
    Optional<MoneyAccount> findByIdAndCurrencyWithLock(UUID userId, Currency currency);
}
