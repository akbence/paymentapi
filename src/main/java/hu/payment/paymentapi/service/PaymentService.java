package hu.payment.paymentapi.service;

import hu.payment.paymentapi.dto.SendMoneyRequest;
import hu.payment.paymentapi.exception.PaymentException;
import hu.payment.paymentapi.kafka.KafkaProducerService;
import hu.payment.paymentapi.model.Currency;
import hu.payment.paymentapi.model.MoneyAccount;
import hu.payment.paymentapi.model.Transaction;
import hu.payment.paymentapi.model.UserAccount;
import hu.payment.paymentapi.repository.MoneyRepository;
import hu.payment.paymentapi.repository.TransactionRepository;
import hu.payment.paymentapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final MoneyRepository moneyRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;
    private final TransactionRepository transactionRepository;

    public void sendMoney(SendMoneyRequest sendMoneyRequest) {
        transferMoney(sendMoneyRequest.getSender(),sendMoneyRequest.getCurrency(),
                sendMoneyRequest.getAmount(),sendMoneyRequest.getRecipient(),sendMoneyRequest.getTransactionId());
    }

    // Method to transfer money between two users' accounts
    @Transactional
    public void transferMoney(String senderUsername, Currency currency, BigDecimal transferAmount,
                              String recipientUsername, String incomingTransactionId) {

        //Check if the transaction already processed
        Optional<Transaction> transactionOptional = transactionRepository.findByincomingTransactionalId(incomingTransactionId);
        if (transactionOptional.isPresent()){
            throw new PaymentException("TRANSACTION_ALREADY_PROCESSED");
        }

        //Get the sender
        Optional<UserAccount> senderUserOptional = userRepository.findByUsername(senderUsername);
        if (senderUserOptional.isEmpty()){
            throw new PaymentException("SENDER_USER_NOT_EXISTS");
        }
        //Get the recipient
        Optional<UserAccount> recipientUserOptional = userRepository.findByUsername(recipientUsername);
        if (recipientUserOptional.isEmpty()){
            throw new PaymentException("RECIPIENT_USER_NOT_EXISTS");
        }
        UserAccount senderUser = senderUserOptional.get();
        UserAccount recipientUser = recipientUserOptional.get();

        //Get sender moneyAcc
        Optional<MoneyAccount> senderAccountOptional = moneyRepository.findByIdAndCurrencyWithLock(senderUser.getId(), currency);
        if (senderAccountOptional.isEmpty()) {
            throw new PaymentException("SENDER_USER_NOT_HAVE_" + currency + "_ACCOUNT");
        }
        MoneyAccount senderMoneyAccount = senderAccountOptional.get();
        // Check if the account has sufficient balance
        if(senderMoneyAccount.getAmount().compareTo(transferAmount) < 0){
            throw new PaymentException("SENDER_USER_NOT_HAVE_ENOUGH_BALLANCE");
        }

        //Get receiver moneyAcc
        Optional<MoneyAccount> recipientAccountOptional = moneyRepository.findByIdAndCurrencyWithLock(recipientUser.getId(), currency);
        if (recipientAccountOptional.isEmpty()) {
            throw new PaymentException("RECIPIENT_USER_NOT_HAVE_" + currency + "_ACCOUNT");
        }
        MoneyAccount recipientMoneyAccount = recipientAccountOptional.get();

        // Deduct the amount from the sender's account
        senderMoneyAccount.setAmount(senderMoneyAccount.getAmount().subtract(transferAmount));
        // Add the amount to the receiver's account
        recipientMoneyAccount.setAmount(recipientMoneyAccount.getAmount().add(transferAmount));
        moneyRepository.saveAll(Set.of(senderMoneyAccount,recipientMoneyAccount));  // Update accounts balance
        //create a transaction history
        Transaction transaction = createTransaction(currency, transferAmount, incomingTransactionId, recipientUser, senderUser);
        transactionRepository.save(transaction);
        // Send Kafka message asynchronously
        String message = String.format("Transfer successful: %s sent %s %s to %s",
                senderUser.getUsername(), transferAmount, currency, recipientUser.getUsername());
        logger.info("Sending to Kafka " + message);

        kafkaProducerService.sendTransactionNotification(recipientUser.getUsername(), message);
    }

    private static Transaction createTransaction(Currency currency, BigDecimal transferAmount, String incomingTransactionId, UserAccount recipientUser, UserAccount senderUser) {
        Transaction transaction = new Transaction();
        transaction.setReceiverAccount(recipientUser);
        transaction.setSenderAccount(senderUser);
        transaction.setIncomingTransactionalId(incomingTransactionId);
        transaction.setCurrency(currency);
        transaction.setAmount(transferAmount);
        return transaction;
    }
}
