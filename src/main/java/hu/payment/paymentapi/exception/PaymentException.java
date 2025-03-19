package hu.payment.paymentapi.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentException extends RuntimeException{
    private int errorCode; //possible extension in many projects this is used
    private String message;

    public PaymentException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * By default, if there is no code added, it is 500, internal error
     * @param message
     */
    public PaymentException(String message) {
        super(message);
        this.errorCode = 500;
        this.message = message;
    }
}
