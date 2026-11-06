
package br.com.pulsepay.controller;

import br.com.pulsepay.entity.Operation;
import br.com.pulsepay.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class OperationController {

    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    // Consultar somente o histórico da própria conta
    @GetMapping
    public List<Operation> findMyOperations(Authentication authentication) {

        Long accountId = getAuthenticatedAccountId(authentication);

        return operationService.findByAccountId(accountId);
    }

    // Identificar a conta do usuário autenticado
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
}
