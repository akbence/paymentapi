package hu.payment.paymentapi.model;

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

    private String username;

    @OneToMany(mappedBy = "userAccount")
    private Set<MoneyAccount> accountList;
}
