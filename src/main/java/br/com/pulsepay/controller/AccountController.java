
package br.com.pulsepay.controller;

import br.com.pulsepay.entity.Account;
import br.com.pulsepay.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Cadastrar uma conta
    @PostMapping
    public Account createAccount(
            @Valid @RequestBody AccountRequest request) {

        Account account = new Account();
        account.setName(request.name());
        account.setCpf(request.cpf());
        account.setEmail(request.email());
        account.setPassword(request.password());

        return accountService.createAccount(account);
    }

    // Consultar somente a própria conta
    @GetMapping("/{id}")
    public Account findById(
            @PathVariable Long id,
            Authentication authentication) {

        Long authenticatedId = getAuthenticatedAccountId(authentication);

        if (!authenticatedId.equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para acessar esta conta."
            );
        }

        return accountService.findById(authenticatedId);
    }

    // Listar somente a própria conta
    @GetMapping
    public List<Account> findAll(Authentication authentication) {

        Long authenticatedId = getAuthenticatedAccountId(authentication);

        return List.of(accountService.findById(authenticatedId));
    }

    // Realizar depósito somente na própria conta
    @PostMapping("/{id}/deposit")
    public Account deposit(
            @PathVariable Long id,
            @RequestBody DepositRequest request,
            Authentication authentication) {

        Long authenticatedId = getAuthenticatedAccountId(authentication);

        if (!authenticatedId.equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não pode depositar em outra conta."
            );
        }

        return accountService.deposit(authenticatedId, request.amount());
    }

    // Realizar transferência entre contas
    @PostMapping("/transfer")
    public Account transfer(
            @RequestBody TransferRequest request,
            Authentication authentication) {

        Long originId = getAuthenticatedAccountId(authentication);

        return accountService.transfer(
                originId,
                request.destinationId(),
                request.amount()
        );
    }

    // Recuperar o ID da conta autenticada
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

    // Dados recebidos no cadastro
    public record AccountRequest(

            @NotBlank(message = "O nome é obrigatório.")
            @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
            String name,

            @NotBlank(message = "O CPF é obrigatório.")
            @Pattern(
                    regexp = "\\d{11}",
                    message = "O CPF deve conter exatamente 11 números."
            )
            String cpf,

            @NotBlank(message = "O e-mail é obrigatório.")
            @Email(message = "Informe um e-mail válido.")
            @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
            String email,

            @NotBlank(message = "A senha é obrigatória.")
            @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres.")
            String password

    ) {
    }

    // Dados recebidos no depósito
    public record DepositRequest(BigDecimal amount) {
    }

    // Dados recebidos na transferência
    public record TransferRequest(
            Long destinationId,
            BigDecimal amount
    ) {
    }
}
