
package br.com.pulsepay.controller;

import br.com.pulsepay.entity.Account;
import br.com.pulsepay.repository.AccountRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            SecurityContextRepository securityContextRepository) {

        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        if (request.cpf() == null || request.password() == null) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(
                            false,
                            "Informe CPF e senha.",
                            null,
                            null
                    ));
        }

        Account account = accountRepository
                .findByCpf(request.cpf())
                .orElse(null);

        if (account == null ||
                account.getPassword() == null ||
                !passwordEncoder.matches(
                        request.password(),
                        account.getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(
                            false,
                            "CPF ou senha incorretos.",
                            null,
                            null
                    ));
        }

        // Criar ou renovar a sessão
        HttpSession session = httpRequest.getSession(true);
        httpRequest.changeSessionId();

        session.setAttribute("accountId", account.getId());
        session.setAttribute("accountName", account.getName());

        // Registrar autenticação no Spring Security
        var authentication =
                new UsernamePasswordAuthenticationToken(
                        account.getId().toString(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context,
                httpRequest,
                httpResponse
        );

        return ResponseEntity.ok(
                new LoginResponse(
                        true,
                        "Login realizado com sucesso!",
                        account.getId(),
                        account.getName()
                )
        );
    }

    public record LoginRequest(
            String cpf,
            String password
    ) {
    }

    public record LoginResponse(
            boolean success,
            String message,
            Long accountId,
            String name
    ) {
    }
}
