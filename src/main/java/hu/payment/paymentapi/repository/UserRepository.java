package hu.payment.paymentapi.repository;

import hu.payment.paymentapi.model.UserAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserAccount, UUID> {

    Optional<UserAccount> findByUsername(String username);
}
