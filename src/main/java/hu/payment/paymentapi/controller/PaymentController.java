package hu.payment.paymentapi.controller;

import hu.payment.paymentapi.dto.SendMoneyRequest;
import hu.payment.paymentapi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/sendMoney")
    public void postSendMoney(@RequestBody SendMoneyRequest sendMoneyRequest){
        paymentService.sendMoney(sendMoneyRequest);
    }

}
