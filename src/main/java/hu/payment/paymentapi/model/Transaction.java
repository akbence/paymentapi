package hu.payment.paymentapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction extends AuditEntityBase{

    @ManyToOne
    @JoinColumn(name = "sender_account_id", referencedColumnName = "id", nullable = false)
    private UserAccount senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id", referencedColumnName = "id", nullable = false)
    private UserAccount receiverAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(unique = true)
    private String incomingTransactionalId;

    //more advanced: transaction status
}
