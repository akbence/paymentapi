package hu.payment.paymentapi.service;

import hu.payment.paymentapi.dto.SendMoneyRequest;
import hu.payment.paymentapi.model.Currency;
import hu.payment.paymentapi.model.MoneyAccount;
import hu.payment.paymentapi.model.UserAccount;
import hu.payment.paymentapi.repository.MoneyRepository;
import hu.payment.paymentapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MoneyRepository moneyRepository;
    private final UserRepository userRepository;

    public void sendMoney(SendMoneyRequest sendMoneyRequest) {

    }



    // Method to transfer money between two users' accounts
    @Transactional
    public void transferMoney(String senderUsername, Currency currency, BigDecimal transferAmount,
                                 String recipientUsername) {

        //Get the sender
        Optional<UserAccount> senderUserOptional = userRepository.findByUsername(senderUsername);
        if (senderUserOptional.isEmpty()){
            //todo: throw error // user doesnt exist
            return ;
        }
        //Get the recipient
        Optional<UserAccount> recipientUserOptional = userRepository.findByUsername(recipientUsername);
        if (recipientUserOptional.isEmpty()){
            //todo: throw error // user doesnt exist
            return ;
        }
        UserAccount senderUser = senderUserOptional.get();
        UserAccount recipientUser = recipientUserOptional.get();

        //Get sender moneyAcc
        Optional<MoneyAccount> senderAccountOptional = moneyRepository.findByIdAndCurrency(senderUser.getId(), currency);
        if (senderAccountOptional.isEmpty()) {
            //todo: throw error // user does not have an account with the specified currency
            return ;
        }
        MoneyAccount senderMoneyAccount = senderAccountOptional.get();
        // Check if the account has sufficient balance
        if(senderMoneyAccount.getAmount().compareTo(transferAmount) < 0){
            //Not enough money
            return;
        }

        //Get receiver moneyAcc
        Optional<MoneyAccount> recipientAccountOptional = moneyRepository.findByIdAndCurrency(recipientUser.getId(), currency);
        if (recipientAccountOptional.isEmpty()) {
            //todo: throw error // user does not have an account with the specified currency
            return ;
        }
        MoneyAccount recipientMoneyAccount = recipientAccountOptional.get();

        // Deduct the amount from the sender's account
        senderMoneyAccount.setAmount(senderMoneyAccount.getAmount().subtract(transferAmount));
        // Add the amount to the receiver's account
        recipientMoneyAccount.setAmount(recipientMoneyAccount.getAmount().add(transferAmount));

        moneyRepository.saveAll(Set.of(senderMoneyAccount,recipientMoneyAccount));  // Update accounts balance


    }
}
