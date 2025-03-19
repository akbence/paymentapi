package hu.payment.paymentapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAccount extends AuditEntityBase{


    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "userAccount")
    private Set<MoneyAccount> accountList;

    // Relationship to the sender transactions
    @OneToMany(mappedBy = "senderAccount")
    private Set<Transaction> sentTransactions;

    // Relationship to the receiver transactions
    @OneToMany(mappedBy = "receiverAccount")
    private Set<Transaction> receivedTransactions;

}
