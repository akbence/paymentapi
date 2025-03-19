package hu.payment.paymentapi.dto;

import hu.payment.paymentapi.model.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SendMoneyRequest {

    private String sender;
    private String recipient;
    private BigDecimal amount;
    private Currency currency;
}
