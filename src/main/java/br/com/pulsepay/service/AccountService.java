package br.com.pulsepay.service;

import br.com.pulsepay.entity.Account;
import br.com.pulsepay.entity.Transfer;
import br.com.pulsepay.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final OperationService operationService;
    private final TransferService transferService;
    private final PasswordEncoder passwordEncoder;

    public AccountService(
            AccountRepository accountRepository,
            OperationService operationService,
            TransferService transferService,
            PasswordEncoder passwordEncoder) {

        this.accountRepository = accountRepository;
        this.operationService = operationService;
        this.transferService = transferService;
        this.passwordEncoder = passwordEncoder;
    }

    // Cadastrar uma conta
    @Transactional
    public Account createAccount(Account account) {

        // Validar CPF
        if (!isValidCpf(account.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        // Verificar CPF duplicado
        if (accountRepository.existsByCpf(account.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        // Verificar e-mail duplicado
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        // Criptografar a senha antes de salvar
        if (account.getPassword() == null ||
                account.getPassword().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }

        account.setPassword(
                passwordEncoder.encode(account.getPassword())
        );

        account.setBalance(BigDecimal.ZERO);

        Account savedAccount = accountRepository.save(account);

        operationService.register(
                "CADASTRO",
                savedAccount.getId(),
                null,
                null,
                "Conta cadastrada"
        );

        return savedAccount;
    }

    // Consultar todas as contas
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    // Consultar conta pelo ID
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Conta não encontrada."));
    }

    // Realizar depósito
    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor do depósito deve ser maior que zero."
            );
        }

        Account account = findById(accountId);
        account.setBalance(account.getBalance().add(amount));

        Account savedAccount = accountRepository.save(account);

        operationService.register(
                "DEPOSITO",
                savedAccount.getId(),
                null,
                amount,
                "Depósito realizado"
        );

        return savedAccount;
    }

    // Realizar transferência entre contas
    @Transactional
    public Account transfer(
            Long originId,
            Long destinationId,
            BigDecimal amount) {

        Transfer transfer = transferService.makeTransfer(
                originId,
                destinationId,
                amount
        );

        return transfer.getSender();
    }

    // Validar CPF e seus dígitos verificadores
    private boolean isValidCpf(String cpf) {

        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        // Rejeitar CPFs com todos os números iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;

        // Calcular primeiro dígito verificador
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }

        int remainder = sum % 11;
        int firstDigit = remainder < 2 ? 0 : 11 - remainder;

        if (firstDigit != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        sum = 0;

        // Calcular segundo dígito verificador
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }

        remainder = sum % 11;
        int secondDigit = remainder < 2 ? 0 : 11 - remainder;

        return secondDigit == Character.getNumericValue(cpf.charAt(10));
    }
}
