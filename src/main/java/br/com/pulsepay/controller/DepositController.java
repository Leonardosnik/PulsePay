
package br.com.pulsepay.controller;

import br.com.pulsepay.entity.Account;
import br.com.pulsepay.service.DepositService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping
    public Account makeDeposit(
            @RequestBody DepositRequest request,
            Authentication authentication) {

        Long accountId = getAuthenticatedAccountId(authentication);

        return depositService.makeDeposit(
                accountId,
                request.getAmount()
        );
    }

    private Long getAuthenticatedAccountId(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuário não autenticado."
            );
        }

        try {
            return Long.parseLong(authentication.getName());

        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Identificação do usuário inválida."
            );
        }
    }

    public static class DepositRequest {

        private BigDecimal amount;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
