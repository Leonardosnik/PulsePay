package br.com.pulsepay.service;

import br.com.pulsepay.entity.Account;
import br.com.pulsepay.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class DepositService {

    private final AccountRepository accountRepository;
    private final OperationService operationService;
    private final NotificationService notificationService;

    public DepositService(
            AccountRepository accountRepository,
            OperationService operationService,
            NotificationService notificationService) {

        this.accountRepository = accountRepository;
        this.operationService = operationService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Account makeDeposit(Long accountId, BigDecimal amount) {

        if (accountId == null) {
            throw new IllegalArgumentException(
                    "O número da conta é obrigatório.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor do depósito deve ser maior que zero.");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Conta não encontrada."));

        account.setBalance(account.getBalance().add(amount));

        Account savedAccount = accountRepository.save(account);

        operationService.register(
                "DEPOSITO",
                accountId,
                null,
                amount,
                "Depósito realizado"
        );

        // Notificar a conta sobre o depósito
        notificationService.notifyDepositReceived(account, amount);

        return savedAccount;
    }
}
