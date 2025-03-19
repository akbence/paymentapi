package hu.payment.paymentapi.repository;

import hu.payment.paymentapi.model.Currency;
import hu.payment.paymentapi.model.MoneyAccount;
import hu.payment.paymentapi.model.UserAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface MoneyRepository extends JpaRepository<MoneyAccount, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MoneyAccount> findByIdAndCurrency(UUID userId, Currency currency);
}
