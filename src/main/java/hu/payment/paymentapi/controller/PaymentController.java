package hu.payment.paymentapi.controller;

import hu.payment.paymentapi.dto.SendMoneyRequest;
import hu.payment.paymentapi.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "Payment API", description = "Endpoints for managing payments")
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Transfer money from one to another", description = "Transfers a sum of money from a user to another." +
            "If successful, sends a notification through Kafka.")
    @ApiResponse(responseCode = "201", description = "Payment transferred successfully")
    @PostMapping("/sendMoney")
    public ResponseEntity<String> postSendMoney(@Valid @RequestBody SendMoneyRequest sendMoneyRequest){
        paymentService.sendMoney(sendMoneyRequest);
        return new ResponseEntity<>("Transaction created successfully", HttpStatus.CREATED);
    }

}
