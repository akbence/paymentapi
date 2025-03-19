package hu.payment.paymentapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MoneyAccount extends AuditEntityBase{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    private BigDecimal amount;
}
